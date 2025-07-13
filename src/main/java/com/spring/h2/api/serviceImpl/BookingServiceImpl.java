package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.Service.BookingService;
import com.spring.h2.api.Service.NotificationService;
import com.spring.h2.api.Service.PaymentService;
import com.spring.h2.api.model.*;
import com.spring.h2.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BookingServiceImpl implements BookingService {

    // Singleton-like behavior with Spring's @Service annotation
    private static volatile BookingServiceImpl instance;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingSeatRepository bookingSeatRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    private static final int SEAT_LOCK_DURATION_MINUTES = 15;

    @Override
    @Transactional
    public Booking createBooking(Long userId, Long showId, List<Long> seatIds) {
        lock.lock();
        try {
            // Validate user and show
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Show show = showRepository.findById(showId)
                    .orElseThrow(() -> new RuntimeException("Show not found"));

            // Check if seats are available and lock them
            if (!lockSeats(showId, seatIds, user.getEmail())) {
                throw new RuntimeException("Some seats are not available");
            }

            // Calculate total amount
            BigDecimal totalAmount = calculateTotalAmount(showId, seatIds);

            // Create booking
            Booking booking = new Booking(user, show, totalAmount);
            booking = bookingRepository.save(booking);

            // Create booking seats
            createBookingSeats(booking, showId, seatIds);

            // Notify observers
            notificationService.notifyObservers("BOOKING_CREATED", booking);

            return booking;

        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public Booking confirmBooking(String bookingId, String paymentId) {
        lock.lock();
        try {
            Booking booking = bookingRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Verify payment
            if (!paymentService.isPaymentSuccessful(paymentId)) {
                throw new RuntimeException("Payment verification failed");
            }

            // Confirm booking
            booking.confirm();

            // Book the seats permanently
            List<BookingSeat> bookingSeats = bookingSeatRepository.findByBooking(booking);
            for (BookingSeat bookingSeat : bookingSeats) {
                bookingSeat.getShowSeat().bookSeat();
                showSeatRepository.save(bookingSeat.getShowSeat());
            }

            booking = bookingRepository.save(booking);

            // Notify observers
            notificationService.notifyObservers("BOOKING_CONFIRMED", booking);

            return booking;

        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId) {
        lock.lock();
        try {
            Booking booking = bookingRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                // Release seats
                List<BookingSeat> bookingSeats = bookingSeatRepository.findByBooking(booking);
                for (BookingSeat bookingSeat : bookingSeats) {
                    bookingSeat.getShowSeat().unlockSeat();
                    showSeatRepository.save(bookingSeat.getShowSeat());
                }

                // Process refund if payment was successful
                if (booking.getPayment() != null) {
                    paymentService.refundPayment(booking.getPayment().getPaymentId());
                }
            }

            booking.cancel();
            bookingRepository.save(booking);

            // Notify observers
            notificationService.notifyObservers("BOOKING_CANCELLED", booking);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<Booking> getBookingById(String bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findUserBookingHistory(userId);
    }

    @Override
    public List<ShowSeat> getAvailableSeats(Long showId) {
        return showSeatRepository.findAvailableSeats(showId, SeatStatus.AVAILABLE);
    }

    @Override
    @Transactional
    public boolean lockSeats(Long showId, List<Long> seatIds, String userEmail) {
        lock.lock();
        try {
            // First, unlock any expired seats
            unlockExpiredSeats();

            // Check if all seats are available
            for (Long seatId : seatIds) {
                Seat seat = seatRepository.findById(seatId)
                        .orElseThrow(() -> new RuntimeException("Seat not found"));

                Show show = showRepository.findById(showId)
                        .orElseThrow(() -> new RuntimeException("Show not found"));

                ShowSeat showSeat = showSeatRepository.findByShowAndSeat(show, seat)
                        .orElseThrow(() -> new RuntimeException("Show seat not found"));

                if (!showSeat.isAvailable()) {
                    return false;
                }
            }

            // Lock all seats
            for (Long seatId : seatIds) {
                Seat seat = seatRepository.findById(seatId).get();
                Show show = showRepository.findById(showId).get();
                ShowSeat showSeat = showSeatRepository.findByShowAndSeat(show, seat).get();

                showSeat.lockSeat(userEmail, SEAT_LOCK_DURATION_MINUTES);
                showSeatRepository.save(showSeat);
            }

            return true;

        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public void unlockSeats(Long showId, List<Long> seatIds) {
        lock.lock();
        try {
            for (Long seatId : seatIds) {
                Seat seat = seatRepository.findById(seatId).get();
                Show show = showRepository.findById(showId).get();
                ShowSeat showSeat = showSeatRepository.findByShowAndSeat(show, seat).get();

                showSeat.unlockSeat();
                showSeatRepository.save(showSeat);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public void unlockExpiredSeats() {
        showSeatRepository.unlockExpiredSeats(LocalDateTime.now());
    }

    @Override
    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    @Override
    public List<Show> getShowsByCity(Long cityId, LocalDateTime startTime, LocalDateTime endTime) {
        return showRepository.findShowsByCityAndDateRange(cityId, startTime, endTime);
    }

    @Override
    public List<Show> getUpcomingShows() {
        return showRepository.findUpcomingShows(LocalDateTime.now(), ShowStatus.SCHEDULED);
    }

    @Override
    @Transactional
    public void processExpiredBookings() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(SEAT_LOCK_DURATION_MINUTES);
        List<Booking> expiredBookings = bookingRepository.findExpiredPendingBookings(expiryTime);

        for (Booking booking : expiredBookings) {
            booking.expire();
            bookingRepository.save(booking);

            // Release locked seats
            List<BookingSeat> bookingSeats = bookingSeatRepository.findByBooking(booking);
            for (BookingSeat bookingSeat : bookingSeats) {
                bookingSeat.getShowSeat().unlockSeat();
                showSeatRepository.save(bookingSeat.getShowSeat());
            }
        }
    }

    @Override
    public boolean isBookingValid(String bookingId) {
        Optional<Booking> booking = bookingRepository.findByBookingId(bookingId);
        return booking.isPresent() && booking.get().getStatus() == BookingStatus.CONFIRMED;
    }

    // Helper methods
    private BigDecimal calculateTotalAmount(Long showId, List<Long> seatIds) {
        BigDecimal total = BigDecimal.ZERO;

        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId).get();
            Show show = showRepository.findById(showId).get();
            ShowSeat showSeat = showSeatRepository.findByShowAndSeat(show, seat).get();

            total = total.add(showSeat.getPrice());
        }

        return total;
    }

    private void createBookingSeats(Booking booking, Long showId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId).get();
            Show show = showRepository.findById(showId).get();
            ShowSeat showSeat = showSeatRepository.findByShowAndSeat(show, seat).get();

            BookingSeat bookingSeat = new BookingSeat(booking, showSeat, showSeat.getPrice());
            bookingSeatRepository.save(bookingSeat);
        }
    }
}

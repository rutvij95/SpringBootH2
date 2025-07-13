package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.model.Ticket;
import com.spring.h2.api.model.Booking;
import com.spring.h2.api.model.BookingStatus;
import com.spring.h2.api.repository.TicketRepository;
import com.spring.h2.api.repository.BookingRepository;
import com.spring.h2.api.Service.TicketService;
import com.spring.h2.api.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Ticket generateTicket(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Ticket can only be generated for confirmed bookings");
        }

        // Check if ticket already exists
        Optional<Ticket> existingTicket = ticketRepository.findByBooking(booking);
        if (existingTicket.isPresent()) {
            return existingTicket.get();
        }

        // Generate QR code
        String qrCode = generateQRCode(booking.getBookingId());

        // Create ticket
        Ticket ticket = new Ticket(booking, qrCode);
        ticket = ticketRepository.save(ticket);

        // Notify observers
        notificationService.notifyObservers("TICKET_GENERATED", booking);

        return ticket;
    }

    @Override
    public Optional<Ticket> getTicketById(String ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }

    @Override
    public Optional<Ticket> getTicketByQrCode(String qrCode) {
        return ticketRepository.findByQrCode(qrCode);
    }

    @Override
    public List<Ticket> getUserTickets(Long userId) {
        return ticketRepository.findUserTickets(userId);
    }

    @Override
    public boolean validateTicket(String ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findByTicketId(ticketId);

        if (ticketOpt.isEmpty()) {
            return false;
        }

        Ticket ticket = ticketOpt.get();
        return ticket.isValid();
    }

    @Override
    @Transactional
    public void markTicketAsUsed(String ticketId) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getIsUsed()) {
            throw new RuntimeException("Ticket has already been used");
        }

        if (!ticket.isValid()) {
            throw new RuntimeException("Ticket is not valid");
        }

        ticket.markAsUsed();
        ticketRepository.save(ticket);
    }

    @Override
    public String generateQRCode(String ticketId) {
        // Simple QR code generation using Base64 encoding
        // In a real implementation, you would use a proper QR code library
        String qrData = String.format("TICKET:%s:TIMESTAMP:%d", ticketId, System.currentTimeMillis());
        return Base64.getEncoder().encodeToString(qrData.getBytes(StandardCharsets.UTF_8));
    }
}

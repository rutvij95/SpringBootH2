package com.spring.h2.api.config;

import com.spring.h2.api.model.*;
import com.spring.h2.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Override
    public void run(String... args) throws Exception {
//        initializeData();
    }

    private void initializeData() {
        // Create sample users
        User admin = new User("admin@bookmyshow.com", "Admin User", "1234567890", UserRole.ADMIN);
        User customer1 = new User("john@example.com", "John Doe", "9876543210", UserRole.CUSTOMER);
        User customer2 = new User("jane@example.com", "Jane Smith", "8765432109", UserRole.CUSTOMER);

        userRepository.save(admin);
        userRepository.save(customer1);
        userRepository.save(customer2);

        // Create sample cities
        City mumbai = new City("Mumbai", "Maharashtra", "India");
        City delhi = new City("Delhi", "Delhi", "India");
        City bangalore = new City("Bangalore", "Karnataka", "India");

        cityRepository.save(mumbai);
        cityRepository.save(delhi);
        cityRepository.save(bangalore);

        // Create sample theaters
        Theater pvr = new Theater("PVR Cinemas", "Phoenix Mall, Mumbai", "022-12345678", mumbai);
        Theater inox = new Theater("INOX Multiplex", "DLF Mall, Delhi", "011-87654321", delhi);
        Theater cinepolis = new Theater("Cinepolis", "Forum Mall, Bangalore", "080-98765432", bangalore);

        theaterRepository.save(pvr);
        theaterRepository.save(inox);
        theaterRepository.save(cinepolis);

        // Create sample screens
        Screen screen1 = new Screen("Screen 1", 100, pvr);
        Screen screen2 = new Screen("Screen 2", 120, pvr);
        Screen screen3 = new Screen("Screen 1", 80, inox);
        Screen screen4 = new Screen("Screen 1", 150, cinepolis);

        screenRepository.save(screen1);
        screenRepository.save(screen2);
        screenRepository.save(screen3);
        screenRepository.save(screen4);

        // Create sample seats
        createSeatsForScreen(screen1);
        createSeatsForScreen(screen2);
        createSeatsForScreen(screen3);
        createSeatsForScreen(screen4);

        // Create sample movies
        Movie movie1 = new Movie("Avengers: Endgame", "Epic superhero movie", 180,
                "English", "Action", "PG-13", LocalDate.now().minusMonths(1));
        movie1.setDirector("Russo Brothers");
        movie1.setCast("Robert Downey Jr., Chris Evans, Mark Ruffalo");

        Movie movie2 = new Movie("The Batman", "Dark superhero thriller", 176,
                "English", "Action", "PG-13", LocalDate.now().minusWeeks(2));
        movie2.setDirector("Matt Reeves");
        movie2.setCast("Robert Pattinson, Zoe Kravitz");

        Movie movie3 = new Movie("RRR", "Epic period action drama", 187,
                "Telugu", "Action", "PG-13", LocalDate.now().minusMonths(3));
        movie3.setDirector("S.S. Rajamouli");
        movie3.setCast("N.T. Rama Rao Jr., Ram Charan");

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        // Create sample shows
        createShowsForMovie(movie1, screen1);
        createShowsForMovie(movie1, screen2);
        createShowsForMovie(movie2, screen3);
        createShowsForMovie(movie3, screen4);

        System.out.println("Sample data initialized successfully!");
    }

    private void createSeatsForScreen(Screen screen) {
        List<Seat> seats = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        int seatsPerRow = screen.getTotalSeats() / 10; // Assuming 10 rows

        for (int row = 0; row < rows.length; row++) {
            for (int col = 1; col <= seatsPerRow; col++) {
                SeatType seatType = getSeatType(row, col);
                BigDecimal basePrice = getBasePriceForSeatType(seatType);

                Seat seat = new Seat(
                    rows[row] + col,
                    rows[row],
                    col,
                    seatType,
                    basePrice,
                    screen
                );
                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

    private SeatType getSeatType(int row, int col) {
        // First 2 rows are VIP
        if (row < 2) return SeatType.VIP;
        // Next 2 rows are Premium
        if (row < 4) return SeatType.PREMIUM;
        // Next 3 rows are Recliner
        if (row < 7) return SeatType.RECLINER;
        // Rest are Normal
        return SeatType.NORMAL;
    }

    private BigDecimal getBasePriceForSeatType(SeatType seatType) {
        switch (seatType) {
            case VIP: return new BigDecimal("500.00");
            case PREMIUM: return new BigDecimal("400.00");
            case RECLINER: return new BigDecimal("300.00");
            default: return new BigDecimal("200.00");
        }
    }

    private void createShowsForMovie(Movie movie, Screen screen) {
        LocalDateTime today = LocalDateTime.now();

        // Create shows for next 7 days
        for (int day = 0; day < 7; day++) {
            LocalDateTime showDate = today.plusDays(day);

            // Create 3 shows per day
            createShow(movie, screen, showDate.withHour(10).withMinute(0)); // Morning show
            createShow(movie, screen, showDate.withHour(14).withMinute(30)); // Afternoon show
            createShow(movie, screen, showDate.withHour(19).withMinute(0)); // Evening show
        }
    }

    private void createShow(Movie movie, Screen screen, LocalDateTime startTime) {
        LocalDateTime endTime = startTime.plusMinutes(movie.getDurationInMinutes()).plusMinutes(30); // 30 min buffer
        BigDecimal priceMultiplier = new BigDecimal("1.0"); // Base multiplier

        Show show = new Show(startTime, endTime, priceMultiplier, movie, screen);
        show = showRepository.save(show);

        // Create show seats for all seats in the screen
        List<Seat> seats = seatRepository.findByScreen(screen);
        for (Seat seat : seats) {
            BigDecimal showSeatPrice = seat.getBasePrice().multiply(priceMultiplier);
            ShowSeat showSeat = new ShowSeat(showSeatPrice, show, seat);
            showSeatRepository.save(showSeat);
        }
    }
}

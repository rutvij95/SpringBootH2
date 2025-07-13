# Movie Booking Management System - Implementation Summary

## Overview
This is a complete Movie Booking Management System built with Spring Boot and H2 database, implementing all required functional requirements and design patterns.

## ✅ Functional Requirements Implemented

### 1. Movie & Show Management
- **Movie Entity**: Title, description, duration, language, genre, rating, release date, director, cast
- **Theater Entity**: Name, address, phone, city association
- **Screen Entity**: Screen management with seat capacity
- **Show Entity**: Show scheduling with start/end times, pricing multipliers
- **City Entity**: Multi-city support

### 2. Seat Layout & Management
- **Seat Types**: Normal, Recliner, Premium, VIP
- **Seat Entity**: Row/column positioning, pricing per seat type
- **ShowSeat Entity**: Real-time seat availability, locking mechanism
- **Seat Status**: Available, Locked, Booked

### 3. Booking Flow
- **Booking Process**: Seat selection → Availability check → Locking → Payment → Confirmation
- **Seat Locking**: 15-minute timeout with automatic unlock
- **Booking Status**: Pending, Confirmed, Cancelled, Expired
- **Concurrency Control**: Thread-safe seat locking with ReentrantLock

### 4. User Roles & Permissions
- **Admin Role**: Can manage movies, theaters, shows
- **Customer Role**: Can book tickets, view booking history
- **User Entity**: Email, name, phone, role-based access

### 5. Payment Flow (Abstract Implementation)
- **Payment Status**: Pending, Processing, Success, Failed, Refunded
- **Payment Methods**: Credit Card, PayPal, UPI
- **Payment Retry**: Support for failed payment retry
- **Refund Processing**: Automated refund on booking cancellation

### 6. Notifications (Observer Pattern)
- **Email Notifications**: Booking confirmation, payment success, cancellation
- **SMS Notifications**: Quick status updates
- **Event Types**: Booking confirmed, payment success/failure, show cancellation

### 7. Ticket Generation & QR Code
- **Ticket Entity**: Unique ticket ID, QR code generation
- **QR Code**: Base64 encoded ticket verification
- **Ticket Validation**: Time-based validity, usage tracking
- **Ticket History**: User ticket history management

## ✅ Design Patterns Implemented

### 1. Singleton Pattern
- **BookingService**: Singleton-like behavior with Spring's @Service
- **Thread-safe**: Using ReentrantLock for concurrent access control

### 2. Factory Pattern
- **PaymentGatewayFactory**: Creates appropriate payment gateway instances
- **Supported Gateways**: CreditCard, PayPal, UPI
- **Dynamic Gateway Selection**: Runtime payment method selection

### 3. Observer Pattern
- **NotificationService**: Subject that notifies observers
- **EmailNotificationObserver**: Sends email notifications
- **SMSNotificationObserver**: Sends SMS notifications
- **Event-driven**: Automatic notifications on booking events

### 4. Strategy Pattern
- **PaymentGateway Interface**: Different payment processing strategies
- **Multiple Implementations**: Each payment method has its own strategy
- **Runtime Selection**: Payment method chosen at runtime

## ✅ Proper Layering Architecture

### 1. Model Layer (Entities)
- User, Movie, Theater, Screen, Seat, Show, ShowSeat
- Booking, BookingSeat, Payment, Ticket
- City, with proper JPA relationships

### 2. Repository Layer
- Spring Data JPA repositories for all entities
- Custom queries for complex operations
- Optimized database operations

### 3. Service Layer
- Business logic implementation
- Design pattern implementations
- Transaction management

### 4. Controller Layer
- REST API endpoints
- Request/response handling
- HTTP status management

## ✅ Concurrency Control

### 1. Seat Booking Race Conditions
- **ReentrantLock**: Thread-safe seat locking
- **Optimistic Locking**: Database-level concurrency control
- **Timeout Mechanism**: Automatic seat unlock after 15 minutes

### 2. Real-time Scenarios
- **Multiple User Booking**: Handled with seat locking
- **Expired Lock Cleanup**: Scheduled cleanup of expired locks
- **Transaction Management**: @Transactional annotations

## 🏗️ System Architecture

### Database Schema
```
Users → Bookings → BookingSeats → ShowSeats → Seats
                                         ↓
Cities → Theaters → Screens → Shows → Movies
                           ↓
Bookings → Payments → Tickets
```

### API Endpoints

#### Movie Management
- `GET /api/movies` - Get all movies
- `GET /api/movies/{id}` - Get movie by ID
- `POST /api/movies` - Create movie (Admin)
- `PUT /api/movies/{id}` - Update movie (Admin)
- `DELETE /api/movies/{id}` - Delete movie (Admin)

#### Booking Management
- `POST /api/bookings` - Create booking
- `GET /api/bookings/{bookingId}` - Get booking details
- `GET /api/bookings/shows/{showId}/seats` - Get available seats
- `POST /api/bookings/shows/{showId}/seats/lock` - Lock seats
- `POST /api/bookings/{bookingId}/payment` - Process payment
- `POST /api/bookings/{bookingId}/cancel` - Cancel booking

#### Payment Management
- `POST /api/payments` - Initiate payment
- `POST /api/payments/{paymentId}/process` - Process payment
- `POST /api/payments/{paymentId}/retry` - Retry failed payment
- `GET /api/payments/{paymentId}/status` - Get payment status

#### Ticket Management
- `POST /api/bookings/{bookingId}/ticket` - Generate ticket
- `GET /api/tickets/{ticketId}` - Get ticket details
- `GET /api/tickets/{ticketId}/validate` - Validate ticket
- `POST /api/tickets/{ticketId}/use` - Mark ticket as used

## 🔧 Configuration

### Database Configuration
- H2 In-Memory Database
- Automatic schema creation
- Sample data initialization
- H2 Console enabled at `/h2-console`

### Application Features
- Scheduled cleanup tasks
- Observer pattern wiring
- Comprehensive logging
- Error handling

## 🚀 Getting Started

### Prerequisites
- Java 11+
- Maven 3.6+

### Running the Application
```bash
mvn spring-boot:run
```

### Accessing the Application
- Application: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- API Documentation: Available via REST endpoints

## 📋 Sample Data Included

### Users
- Admin user: admin@bookmyshow.com
- Customer users: john@example.com, jane@example.com

### Movies
- Avengers: Endgame (English, Action)
- The Batman (English, Action)
- RRR (Telugu, Action)

### Theaters
- PVR Cinemas (Mumbai)
- INOX Multiplex (Delhi)
- Cinepolis (Bangalore)

### Shows
- 7 days of shows for each movie
- 3 shows per day (Morning, Afternoon, Evening)
- Different seat types with varied pricing

This implementation demonstrates enterprise-level software architecture with proper design patterns, concurrency control, and comprehensive feature coverage for a movie booking management system.

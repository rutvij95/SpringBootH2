package com.spring.h2.api.Service;

import com.spring.h2.api.model.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket generateTicket(String bookingId);
    Optional<Ticket> getTicketById(String ticketId);
    Optional<Ticket> getTicketByQrCode(String qrCode);
    List<Ticket> getUserTickets(Long userId);
    boolean validateTicket(String ticketId);
    void markTicketAsUsed(String ticketId);
    String generateQRCode(String ticketId);
}

package com.spring.h2.api.controller;

import com.spring.h2.api.model.Ticket;
import com.spring.h2.api.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Get ticket by ID
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable String ticketId) {
        Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
        return ticket.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // Get ticket by QR code
    @GetMapping("/qr/{qrCode}")
    public ResponseEntity<Ticket> getTicketByQrCode(@PathVariable String qrCode) {
        Optional<Ticket> ticket = ticketService.getTicketByQrCode(qrCode);
        return ticket.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // Get user tickets
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.getUserTickets(userId);
        return ResponseEntity.ok(tickets);
    }

    // Validate ticket
    @GetMapping("/{ticketId}/validate")
    public ResponseEntity<Boolean> validateTicket(@PathVariable String ticketId) {
        boolean isValid = ticketService.validateTicket(ticketId);
        return ResponseEntity.ok(isValid);
    }

    // Mark ticket as used (for theater staff)
    @PostMapping("/{ticketId}/use")
    public ResponseEntity<Void> markTicketAsUsed(@PathVariable String ticketId) {
        try {
            ticketService.markTicketAsUsed(ticketId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Generate QR code for ticket
    @GetMapping("/{ticketId}/qr")
    public ResponseEntity<String> generateQRCode(@PathVariable String ticketId) {
        try {
            String qrCode = ticketService.generateQRCode(ticketId);
            return ResponseEntity.ok(qrCode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

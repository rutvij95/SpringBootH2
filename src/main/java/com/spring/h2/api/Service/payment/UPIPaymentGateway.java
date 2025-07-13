package com.spring.h2.api.Service.payment;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class UPIPaymentGateway implements PaymentGateway {

    @Override
    public String processPayment(String paymentId, BigDecimal amount, String paymentMethod) {
        // Simulate UPI payment processing
        try {
            Thread.sleep(1000); // Simulate processing time

            // Simulate success/failure (95% success rate)
            if (Math.random() < 0.95) {
                return "UPI_TXN_" + UUID.randomUUID().toString().substring(0, 8);
            } else {
                throw new RuntimeException("UPI payment failed");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Payment processing interrupted", e);
        }
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        // Simulate payment verification
        return transactionId != null && transactionId.startsWith("UPI_TXN_");
    }

    @Override
    public void refundPayment(String transactionId, BigDecimal amount) {
        // Simulate refund processing
        System.out.println("Processing UPI refund for transaction: " + transactionId + " amount: " + amount);
    }

    @Override
    public String getGatewayName() {
        return "UPI";
    }
}

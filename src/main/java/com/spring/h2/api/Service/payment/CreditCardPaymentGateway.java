package com.spring.h2.api.Service.payment;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CreditCardPaymentGateway implements PaymentGateway {

    @Override
    public String processPayment(String paymentId, BigDecimal amount, String paymentMethod) {
        // Simulate credit card payment processing
        try {
            Thread.sleep(2000); // Simulate processing time

            // Simulate success/failure (90% success rate)
            if (Math.random() < 0.9) {
                return "CC_TXN_" + UUID.randomUUID().toString().substring(0, 8);
            } else {
                throw new RuntimeException("Credit card payment failed");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Payment processing interrupted", e);
        }
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        // Simulate payment verification
        return transactionId != null && transactionId.startsWith("CC_TXN_");
    }

    @Override
    public void refundPayment(String transactionId, BigDecimal amount) {
        // Simulate refund processing
        System.out.println("Processing refund for transaction: " + transactionId + " amount: " + amount);
    }

    @Override
    public String getGatewayName() {
        return "CreditCard";
    }
}

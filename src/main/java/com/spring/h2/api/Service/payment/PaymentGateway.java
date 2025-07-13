package com.spring.h2.api.Service.payment;

import com.spring.h2.api.model.Payment;
import java.math.BigDecimal;

public interface PaymentGateway {
    String processPayment(String paymentId, BigDecimal amount, String paymentMethod);
    boolean verifyPayment(String transactionId);
    void refundPayment(String transactionId, BigDecimal amount);
    String getGatewayName();
}

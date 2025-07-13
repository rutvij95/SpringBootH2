package com.spring.h2.api.Service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentGatewayFactory {

    private final Map<String, PaymentGateway> gateways = new HashMap<>();

    @Autowired
    public PaymentGatewayFactory(List<PaymentGateway> paymentGateways) {
        for (PaymentGateway gateway : paymentGateways) {
            gateways.put(gateway.getGatewayName().toLowerCase(), gateway);
        }
    }

    public PaymentGateway getPaymentGateway(String paymentMethod) {
        PaymentGateway gateway = gateways.get(paymentMethod.toLowerCase());
        if (gateway == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
        return gateway;
    }

    public boolean isPaymentMethodSupported(String paymentMethod) {
        return gateways.containsKey(paymentMethod.toLowerCase());
    }

    public List<String> getSupportedPaymentMethods() {
        return gateways.keySet().stream().toList();
    }
}

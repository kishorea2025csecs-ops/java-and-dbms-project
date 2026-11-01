package com.parkease.dto;

import com.parkease.constant.PaymentMethod;
import java.math.BigDecimal;

public class ProcessExitRequest {

    private String ticketNumber;
    private PaymentMethod paymentMethod;
    private BigDecimal amountPaid;

    public ProcessExitRequest() {}

    public ProcessExitRequest(String ticketNumber, PaymentMethod paymentMethod, BigDecimal amountPaid) {
        this.ticketNumber = ticketNumber;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
    }

    public void validate() {
        if (ticketNumber == null || ticketNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket number is required.");
        }
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.CASH;
        }
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}

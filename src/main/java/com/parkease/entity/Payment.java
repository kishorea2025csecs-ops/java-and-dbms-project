package com.parkease.entity;

import com.parkease.constant.PaymentMethod;
import com.parkease.constant.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_ticket"))
    private ParkingTicket ticket;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PAID;

    @Column(name = "payment_time", updatable = false)
    private LocalDateTime paymentTime = LocalDateTime.now();

    @Column(name = "transaction_ref", length = 100)
    private String transactionRef;

    public Payment() {}

    public Payment(ParkingTicket ticket, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
        this.ticket = ticket;
        this.amount = amount;
        this.paymentMethod = paymentMethod != null ? paymentMethod.name() : "CASH";
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PAID;
        this.paymentTime = LocalDateTime.now();
        this.transactionRef = "TXN/" + this.paymentMethod + "/" + System.currentTimeMillis() % 10000000;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public void setTicket(ParkingTicket ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.paymentMethod = method != null ? method.name() : "CASH";
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}

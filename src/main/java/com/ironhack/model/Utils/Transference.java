package com.ironhack.model.Utils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity
public class Transference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private BigDecimal amount;
    @NotNull
    private Long destinationId;
    @NotNull
    private Long ownerId;

    public Transference() {
    }

    public Transference(BigDecimal amount, Long destinationId, Long ownerId) {
        this.amount = amount;
        this.destinationId = destinationId;
        this.ownerId = ownerId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}



package com.system.transaction_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TransactionState")
@Table(name = "tbl_transaction_state")
@EqualsAndHashCode(callSuper = true)
public class TransactionState extends BaseEntity implements Serializable {

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "state")
    private Boolean state;
}

package com.system.transaction_service.entity;

import com.system.common_library.enums.AccountType;
import com.system.common_library.enums.FeePayer;
import com.system.common_library.enums.Initiator;
import com.system.common_library.enums.Method;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Transaction")
@Table(name = "tbl_transaction",
        indexes = {
                @Index(name = "idx_sender_receiver_name", columnList = "account_sender_name, account_receiver_name, note")
        }
)
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction extends BaseEntity implements Serializable {

    @Column(name = "account_sender_id")
    private String accountSenderId;

    @Column(name = "account_sender")
    private String accountSender;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_sender_type")
    private AccountType accountSenderType;

    @Column(name = "account_sender_name")
    private String accountSenderName;

    @Column(name = "account_receiver_id")
    private String accountReceiverId;

    @Column(name = "account_receiver")
    private String accountReceiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_receiver_type")
    private AccountType accountReceiverType;

    @Column(name = "account_receiver_name")
    private String accountReceiverName;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "fee")
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_payer")
    private FeePayer feePayer;

    @Column(name = "note")
    private String note;

    @Column(name = "otp_code")
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "initiator")
    private Initiator initiator;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private Method method;

    @CreatedBy
    @Column(name = "creator_id", nullable = false, updatable = false)
    private String creatorId;
}

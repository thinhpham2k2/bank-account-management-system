package com.system.transaction_service.mapper;

import com.system.common_library.enums.*;
import com.system.transaction_service.dto.transaction.CreateExternalDTO;
import com.system.transaction_service.dto.transaction.TransactionDTO;
import com.system.transaction_service.dto.transaction.TransactionExtraDTO;
import com.system.transaction_service.entity.*;
import com.system.transaction_service.util.Constant;
import de.huxhorn.sulky.ulid.ULID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionDetailMapper {

    @Mapping(target = "note", source = "transaction.note")
    @Mapping(target = "direction", source = "direction", qualifiedByName = "mapEnum")
    @Mapping(target = "directionName", source = "direction", qualifiedByName = "mapEnumDescription")
    TransactionDTO entityToDTO(TransactionDetail entity);

    // External bank
    @Mapping(target = "externalBankName", source = "transaction", qualifiedByName = "mapExternalBankName")
    @Mapping(target = "externalBankShortName", source = "transaction", qualifiedByName = "mapExternalBankShortName")
    @Mapping(target = "externalBankCode", source = "transaction", qualifiedByName = "mapExternalBankCode")
    @Mapping(target = "externalBankLogo", source = "transaction", qualifiedByName = "mapExternalBankLogo")
    // External transaction
    @Mapping(target = "napasCode", source = "transaction", qualifiedByName = "mapNapasCode")
    @Mapping(target = "swiftCode", source = "transaction", qualifiedByName = "mapSwiftCode")
    // Internal transaction
    @Mapping(target = "type", source = "transaction", qualifiedByName = "mapType")
    @Mapping(target = "typeName", source = "transaction", qualifiedByName = "mapTypeDescription")
    // Transaction
    @Mapping(target = "senderAccountId", source = "transaction.senderAccountId")
    @Mapping(target = "senderAccount", source = "transaction.senderAccount")
    @Mapping(target = "senderAccountType", source = "transaction.senderAccountType", qualifiedByName = "mapEnum")
    @Mapping(target = "senderAccountTypeName", source = "transaction.senderAccountType", qualifiedByName = "mapEnumDescription")
    @Mapping(target = "senderAccountName", source = "transaction.senderAccountName")
    @Mapping(target = "receiverAccountId", source = "transaction.receiverAccountId")
    @Mapping(target = "receiverAccount", source = "transaction.receiverAccount")
    @Mapping(target = "receiverAccountType", source = "transaction.receiverAccountType", qualifiedByName = "mapEnum")
    @Mapping(target = "receiverAccountTypeName", source = "transaction.receiverAccountType", qualifiedByName = "mapEnumDescription")
    @Mapping(target = "receiverAccountName", source = "transaction.receiverAccountName")
    @Mapping(target = "transactionCode", source = "transaction.transactionCode")
    @Mapping(target = "feePayer", source = "transaction.feePayer", qualifiedByName = "mapEnum")
    @Mapping(target = "feePayerName", source = "transaction.feePayer", qualifiedByName = "mapEnumDescription")
    @Mapping(target = "note", source = "transaction.note")
    @Mapping(target = "otpCode", source = "transaction.otpCode")
    @Mapping(target = "initiator", source = "transaction.initiator", qualifiedByName = "mapEnum")
    @Mapping(target = "initiatorName", source = "transaction.initiator", qualifiedByName = "mapEnumDescription")
    @Mapping(target = "transactionType", source = "transaction.transactionType", qualifiedByName = "mapEnum")
    @Mapping(target = "transactionTypeName", source = "transaction.transactionType", qualifiedByName = "mapEnumDescription")
    @Mapping(target = "method", source = "transaction.method", qualifiedByName = "mapEnum")
    @Mapping(target = "methodName", source = "transaction.method", qualifiedByName = "mapEnumDescription")
    @Mapping(target = "creatorId", source = "transaction.creatorId")
    // Transaction state
    @Mapping(target = "transactionState", source = "transaction", qualifiedByName = "mapState")
    @Mapping(target = "transactionStateName", source = "transaction", qualifiedByName = "mapStateDescription")
    // Transaction detail
    @Mapping(target = "direction", source = "direction", qualifiedByName = "mapEnum")
    @Mapping(target = "directionName", source = "direction", qualifiedByName = "mapEnumDescription")
    TransactionExtraDTO entityToExtraDTO(TransactionDetail entity);

    @Mapping(target = "id", expression = "java(mapId())")
    @Mapping(target = "transactionType", expression = "java(mapExternalTransactionType(\"EXTERNAL\"))")
    @Mapping(target = "status", expression = "java(true)")
    ExternalTransaction createToExternalEntity(CreateExternalDTO create);

    @Named("mapId")
    default String mapId() {

        return new ULID().nextULID();
    }

    @Named("mapExternalTransactionType")
    default TransactionType mapExternalTransactionType(String type) {

        if (type.equals(TransactionType.INTERNAL.name())) {

            return TransactionType.INTERNAL;
        } else if (type.equals(TransactionType.EXTERNAL.name())) {

            return TransactionType.EXTERNAL;
        }

        return TransactionType.PAYMENT;
    }

    @Named("mapExternalBankName")
    default String mapExternalBankName(Transaction transaction) {

        if (transaction instanceof ExternalTransaction ex) {

            return ex.getExternalBank().getName();
        }

        return null;
    }

    @Named("mapExternalBankShortName")
    default String mapExternalBankShortName(Transaction transaction) {

        if (transaction instanceof ExternalTransaction ex) {

            return ex.getExternalBank().getShortName();
        }

        return null;
    }

    @Named("mapExternalBankCode")
    default String mapExternalBankCode(Transaction transaction) {

        if (transaction instanceof ExternalTransaction ex) {

            return ex.getExternalBank().getCode();
        }

        return null;
    }

    @Named("mapExternalBankLogo")
    default String mapExternalBankLogo(Transaction transaction) {

        if (transaction instanceof ExternalTransaction ex) {

            return ex.getExternalBank().getLogo();
        }

        return null;
    }

    @Named("mapNapasCode")
    default String mapNapasCode(Transaction transaction) {

        if (transaction instanceof ExternalTransaction ex) {

            return ex.getNapasCode();
        }

        return null;
    }

    @Named("mapSwiftCode")
    default String mapSwiftCode(Transaction transaction) {

        if (transaction instanceof ExternalTransaction ex) {

            return ex.getSwiftCode();
        }

        return null;
    }

    @Named("mapType")
    default String mapType(Transaction transaction) {

        if (transaction instanceof InternalTransaction in) {

            return in.getType().toString();
        }

        return null;
    }

    @Named("mapTypeDescription")
    default String mapTypeDescription(Transaction transaction) {

        if (transaction instanceof InternalTransaction in) {

            return in.getType().getDescription();
        }

        return null;
    }

    @Named("mapState")
    default String mapState(Transaction transaction) {

        return transaction.getTransactionStateList().toArray(TransactionState[]::new)[transaction.getTransactionStateList().size() - 1].getState().toString();
    }

    @Named("mapStateDescription")
    default String mapStateDescription(Transaction transaction) {

        return transaction.getTransactionStateList().toArray(TransactionState[]::new)[transaction.getTransactionStateList().size() - 1].getState().getDescription();
    }

    @Named("mapEnum")
    default String mapEnum(Object enumObject) {

        return enumObject.toString();
    }

    @Named("mapEnumDescription")
    default String mapEnumDescription(Object enumObject) {

        if (enumObject instanceof AccountType accountType) {

            return accountType.getDescription();
        } else if (enumObject instanceof Direction direction) {

            return direction.getDescription();
        } else if (enumObject instanceof FeePayer feePayer) {

            return feePayer.getDescription();
        } else if (enumObject instanceof Initiator initiator) {

            return initiator.getDescription();
        } else if (enumObject instanceof Method method) {

            return method.getDescription();
        } else if (enumObject instanceof State state) {

            return state.getDescription();
        } else if (enumObject instanceof TransactionType transactionType) {

            return transactionType.getDescription();
        } else if (enumObject instanceof Type type) {

            return type.getDescription();
        }

        return Constant.BLANK;
    }
}

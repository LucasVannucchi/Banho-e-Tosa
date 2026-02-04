package br.com.gihpet.banhoetosa.payments.domain.dto;

import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentMethod;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        UUID jobId,
        BigDecimal amount,
        BigDecimal outstandingAmount,
        PaymentMethod method,
        PaymentStatus status,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dueDate,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime paidAt,
        Integer installmentNumber,
        Integer totalInstallments,
        String transactionCode,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) { }
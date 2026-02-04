package br.com.gihpet.banhoetosa.payments.domain.dto;

import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentMethod;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentRequestDTO(
        UUID jobId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime dueDate,
        LocalDateTime paidAt,
        Integer installmentNumber,
        Integer totalInstallments,
        String transactionCode
) { }
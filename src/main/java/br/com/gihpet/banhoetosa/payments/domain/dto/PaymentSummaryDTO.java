package br.com.gihpet.banhoetosa.payments.domain.dto;

import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentSummaryDTO(
        UUID paymentId,
        UUID jobId,
        String serviceType,
        Integer installmentNumber,
        Integer totalInstallments,
        BigDecimal amount,
        BigDecimal outstandingAmount,
        LocalDateTime dueDate,
        PaymentStatus status,
        PaymentMethod method
) { }
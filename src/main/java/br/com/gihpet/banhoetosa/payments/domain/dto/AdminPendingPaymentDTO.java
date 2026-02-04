package br.com.gihpet.banhoetosa.payments.domain.dto;

import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import java.math.BigDecimal;

public record AdminPendingPaymentDTO(
        String user,
        BigDecimal pendingBalance,
        Integer installmentNumber,
        Integer totalInstallments,
        PaymentStatus status
) { }
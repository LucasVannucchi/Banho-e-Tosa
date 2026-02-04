package br.com.gihpet.banhoetosa.payments.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public record AdminFinancialSummaryDTO(
        BigDecimal billed,
        BigDecimal pending,
        List<AdminPendingPaymentDTO> pendingPayments
) { }
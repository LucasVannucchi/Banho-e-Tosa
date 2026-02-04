package br.com.gihpet.banhoetosa.payments.domain.validator;

import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentRequestDTO;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class PaymentValidator {

    public void validate(PaymentRequestDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Payment data is required.");
        if (dto.amount() == null || dto.amount().signum() <= 0)
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        if (dto.method() == null)
            throw new IllegalArgumentException("Payment method is required.");

        if (dto.dueDate() != null && dto.dueDate().isBefore(LocalDateTime.now().minusDays(1)))
            throw new IllegalArgumentException("Due date cannot be earlier than current date.");

        if (dto.status() == PaymentStatus.PAID && dto.paidAt() == null)
            throw new IllegalArgumentException("Payment date is required when status is PAID.");
    }
}
package br.com.gihpet.banhoetosa.payments.domain.mapper;

import br.com.gihpet.banhoetosa.jobs.domain.entity.Job;
import br.com.gihpet.banhoetosa.jobs.repository.JobRepository;
import br.com.gihpet.banhoetosa.payments.domain.dto.AdminPendingPaymentDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentRequestDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentResponseDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentSummaryDTO;
import br.com.gihpet.banhoetosa.payments.domain.entity.Payment;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentMethod;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    private final JobRepository jobRepository;

    public PaymentMapper(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Payment toEntity(PaymentRequestDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();

        Optional<Job> jobOpt = jobRepository.findById(dto.jobId());
        jobOpt.ifPresent(payment::setJob);

        payment.setAmount(dto.amount());
        payment.setMethod(dto.method());
        payment.setStatus(dto.status());
        payment.setDueDate(dto.dueDate());
        payment.setPaidAt(dto.paidAt());
        payment.setInstallmentNumber(dto.installmentNumber());
        payment.setTotalInstallments(dto.totalInstallments());
        payment.setTransactionCode(dto.transactionCode());

        return payment;
    }

    public PaymentResponseDTO toDTO(Payment payment, User loggedUser) {
        if (payment == null) return null;

        UUID jobId = payment.getJob() != null ? payment.getJob().getId() : null;

        return new PaymentResponseDTO(
                payment.getId(),
                jobId,
                payment.getAmount(),
                payment.getOutstandingAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getDueDate(),
                payment.getPaidAt(),
                payment.getInstallmentNumber(),
                payment.getTotalInstallments(),
                payment.getTransactionCode(),
                payment.getCreatedAt(),
                payment.getUpdatedAt(),
                payment.getCreatedBy(),
                payment.getUpdatedBy()
        );
    }

    public PaymentSummaryDTO toSummaryDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentSummaryDTO(
                payment.getId(),
                payment.getJob() != null ? payment.getJob().getId() : null,
                payment.getJob() != null ? payment.getJob().getServiceType().name() : null,
                payment.getInstallmentNumber(),
                payment.getTotalInstallments(),
                payment.getAmount(),
                payment.getOutstandingAmount(),
                payment.getDueDate(),
                payment.getStatus(),
                payment.getMethod()
        );
    }

    public List<PaymentSummaryDTO> toSummaryList(List<Payment> payments) {
        return payments.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public AdminPendingPaymentDTO toAdminPendingPaymentDTO(Payment payment) {
        if (payment == null) return null;

        return new AdminPendingPaymentDTO(
                payment.getJob() != null && payment.getJob().getUser() != null
                        ? payment.getJob().getUser().getEmail()
                        : null,
                payment.getOutstandingAmount(),
                payment.getInstallmentNumber(),
                payment.getTotalInstallments(),
                payment.getStatus()
        );
    }

    public List<AdminPendingPaymentDTO> toAdminPendingPaymentList(List<Payment> payments) {
        return payments.stream()
                .map(this::toAdminPendingPaymentDTO)
                .toList();
    }

    public Payment createPayment(Job job, User loggedUser, BigDecimal installmentValue,
                                 PaymentMethod method, PaymentStatus status,
                                 LocalDateTime dueDate, int installmentNumber, int totalInstallments) {
        Payment payment = new Payment();
        payment.setJob(job);
        payment.setAmount(installmentValue);
        payment.setOutstandingAmount(installmentValue);
        payment.setMethod(method);
        payment.setStatus(status);
        payment.setDueDate(dueDate);
        payment.setInstallmentNumber(installmentNumber);
        payment.setTotalInstallments(totalInstallments);
        payment.setCreatedBy(loggedUser.getEmail());
        payment.setUpdatedBy(loggedUser.getEmail());
        return payment;
    }
}
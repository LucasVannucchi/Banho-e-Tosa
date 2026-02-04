package br.com.gihpet.banhoetosa.payments.domain.service;

import br.com.gihpet.banhoetosa.jobs.domain.entity.Job;
import br.com.gihpet.banhoetosa.jobs.domain.enums.StatusJob;
import br.com.gihpet.banhoetosa.payments.domain.dto.AdminFinancialSummaryDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.AdminPendingPaymentDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentResponseDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentSummaryDTO;
import br.com.gihpet.banhoetosa.payments.domain.entity.Payment;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentMethod;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import br.com.gihpet.banhoetosa.payments.domain.mapper.PaymentMapper;
import br.com.gihpet.banhoetosa.payments.repository.PaymentRepository;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentMapper paymentMapper, PaymentRepository paymentRepository) {
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public List<PaymentResponseDTO> createPaymentsForJob(Job job, User loggedUser, int installments) {
        List<PaymentResponseDTO> dtos = new ArrayList<>();
        BigDecimal total = job.getPrice();
        BigDecimal installmentValue = total.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);

        for (int i = 1; i <= installments; i++) {
            LocalDateTime dueDate = job.getScheduledDate().plusMonths(i - 1);
            Payment p = paymentMapper.createPayment(job, loggedUser, installmentValue,
                    PaymentMethod.PIX, PaymentStatus.PENDING, dueDate, i, installments);
            Payment saved = paymentRepository.save(p);
            job.addPayment(saved);

            dtos.add(paymentMapper.toDTO(saved, loggedUser));
        }

        return dtos;
    }

    @Transactional
    public Payment processPayment(UUID paymentId, BigDecimal paidAmount, PaymentMethod method) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("This payment has already been paid.");
        }

        BigDecimal outstanding = payment.getOutstandingAmount();
        payment.setMethod(method);
        payment.setPaidAt(LocalDateTime.now());

        if (paidAmount.compareTo(outstanding) < 0) {
            BigDecimal remaining = outstanding.subtract(paidAmount);
            payment.setOutstandingAmount(remaining);
            payment.setStatus(PaymentStatus.PARTIAL);
        } else {
            payment.setOutstandingAmount(BigDecimal.ZERO);
            payment.setStatus(PaymentStatus.PAID);
        }

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public BigDecimal getClientBalance(User client) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getJob().getUser().getId().equals(client.getId()))
                .filter(p -> p.getStatus() == PaymentStatus.PENDING || p.getStatus() == PaymentStatus.PARTIAL)
                .map(Payment::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public AdminFinancialSummaryDTO getAdminSummary() {
        List<Payment> paidPayments = paymentRepository.findByStatus(PaymentStatus.PAID);

        List<Payment> pendingAndPartialPayments =
                paymentRepository.findByStatuses(List.of(PaymentStatus.PENDING, PaymentStatus.PARTIAL));

        BigDecimal billed = paidPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pending = pendingAndPartialPayments.stream()
                .map(Payment::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<AdminPendingPaymentDTO> pendingPayments = paymentMapper.toAdminPendingPaymentList(pendingAndPartialPayments);

        return new AdminFinancialSummaryDTO(billed, pending, pendingPayments);
    }

    @Transactional(readOnly = true)
    public List<PaymentSummaryDTO> getClientPendingPayments(User logged) {
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(p -> p.getJob().getUser().getId().equals(logged.getId()))
                .filter(p -> p.getJob().getStatusJob() == StatusJob.SCHEDULED || p.getJob().getStatusJob() == StatusJob.NO_SHOW || p.getJob().getStatusJob() == StatusJob.COMPLETED)
                .filter(p -> p.getStatus() == PaymentStatus.PENDING || p.getStatus() == PaymentStatus.PARTIAL)
                .toList();
        return paymentMapper.toSummaryList(payments);
    }

    public record AdminFinancialSummary(BigDecimal billed, BigDecimal pending) {}
}
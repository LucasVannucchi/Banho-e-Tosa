package br.com.gihpet.banhoetosa.payments.repository;

import br.com.gihpet.banhoetosa.payments.domain.entity.Payment;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {

    List<Payment> findByJob_Id(UUID jobId);

    @Query("SELECT p FROM Payment p WHERE p.status IN (:statuses)")
    List<Payment> findByStatuses(@Param("statuses") List<PaymentStatus> statuses);

    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    List<Payment> findByStatus(@Param("status") PaymentStatus status);
}
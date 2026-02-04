package br.com.gihpet.banhoetosa.jobs.domain.entity;

import br.com.gihpet.banhoetosa.common.domain.entity.History;
import br.com.gihpet.banhoetosa.jobs.domain.enums.ServiceType;
import br.com.gihpet.banhoetosa.jobs.domain.enums.StatusJob;
import br.com.gihpet.banhoetosa.jobs.domain.enums.TimeTaxiDog;
import br.com.gihpet.banhoetosa.payments.domain.entity.Payment;
import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_job")
public class Job extends History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "taxi_dog")
    private Boolean taxiDog = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    private TimeTaxiDog timeTaxiDog = TimeTaxiDog.NONE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusJob statusJob = StatusJob.PENDING;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id")
    private User responsible;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Boolean isTaxiDog() {
        return taxiDog;
    }

    public void setTaxiDog(Boolean taxiDog) {
        this.taxiDog = taxiDog;
    }

    public TimeTaxiDog getTimeTaxiDog() {
        return timeTaxiDog;
    }

    public void setTimeTaxiDog(TimeTaxiDog timeTaxiDog) {
        this.timeTaxiDog = timeTaxiDog;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public StatusJob getStatusJob() {
        return statusJob;
    }

    public void setStatusJob(StatusJob statusJob) {
        this.statusJob = statusJob;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setJob(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setJob(null);
    }
}
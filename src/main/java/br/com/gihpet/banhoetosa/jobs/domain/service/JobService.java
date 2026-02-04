package br.com.gihpet.banhoetosa.jobs.domain.service;

import br.com.gihpet.banhoetosa.jobs.domain.dto.JobRequestDTO;
import br.com.gihpet.banhoetosa.jobs.domain.dto.JobResponseDTO;
import br.com.gihpet.banhoetosa.jobs.domain.entity.Job;
import br.com.gihpet.banhoetosa.jobs.domain.enums.StatusJob;
import br.com.gihpet.banhoetosa.jobs.domain.mapper.JobMapper;
import br.com.gihpet.banhoetosa.jobs.domain.validator.JobValidator;
import br.com.gihpet.banhoetosa.jobs.repository.JobRepository;
import br.com.gihpet.banhoetosa.payments.domain.service.PaymentService;
import br.com.gihpet.banhoetosa.pets.domain.exceptions.PetNotFoundException;
import br.com.gihpet.banhoetosa.pets.domain.service.PetService;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final JobValidator jobValidator;
    private final PaymentService paymentService;
    private final PetService petService;
    private final UserService userService;

    public JobService(JobRepository jobRepository, JobMapper jobMapper, JobValidator jobValidator, PaymentService paymentService, PetService petService, UserService userService) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.jobValidator = jobValidator;
        this.paymentService = paymentService;
        this.petService = petService;
        this.userService = userService;
    }


    @Transactional
    public JobResponseDTO create(JobRequestDTO dto, UUID petId, User loggedUser) {

        jobValidator.validate(dto);

        Job job = jobMapper.toEntity(dto);

        petService.findById(petId)
                .ifPresentOrElse(job::setPet,
                        () -> { throw new PetNotFoundException("Pet not found"); });

        job.setResponsible(null);
        job.setUser(loggedUser);
        job.setCreatedBy(loggedUser.getEmail());
        job.setUpdatedBy(loggedUser.getEmail());

        jobRepository.save(job);

        int installments = (dto.installments() == null || dto.installments() <= 0)
                ? 1 : dto.installments();

        paymentService.createPaymentsForJob(job, loggedUser, installments);

        return jobMapper.toDTO(job);
    }

    @Transactional(readOnly = true)
    public List<JobResponseDTO> findAll(User loggedUser) {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobResponseDTO findById(UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado: " + id));
        return jobMapper.toDTO(job);
    }

    @Transactional
    public void statusScheduled(UUID idJob, User loggedUser){
        Job job = jobRepository.findById(idJob)
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado" + idJob));

        if (loggedUser.getRole() != TypeRole.ADMIN && loggedUser.getRole() != TypeRole.EMPLOYEE) {
            throw new IllegalArgumentException("Only ADMIN or EMPLOYEE can assign responsible.");
        }

        job.setStatusJob(StatusJob.SCHEDULED);
        job.setUpdatedBy(loggedUser.getEmail());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    @Transactional
    public void statusStarted(UUID idJob, User loggedUser){
        Job job = jobRepository.findById(idJob)
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado" + idJob));

        if (loggedUser.getRole() != TypeRole.ADMIN && loggedUser.getRole() != TypeRole.EMPLOYEE) {
            throw new IllegalArgumentException("Only ADMIN or EMPLOYEE can assign responsible.");
        }

        job.setStatusJob(StatusJob.STARTED);
        job.setUpdatedBy(loggedUser.getEmail());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    @Transactional
    public void statusCompleted(UUID idJob, User loggedUser){
        Job job = jobRepository.findById(idJob)
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado" + idJob));

        if (loggedUser.getRole() != TypeRole.ADMIN && loggedUser.getRole() != TypeRole.EMPLOYEE) {
            throw new IllegalArgumentException("Only ADMIN or EMPLOYEE can assign responsible.");
        }

        job.setStatusJob(StatusJob.COMPLETED);
        job.setUpdatedBy(loggedUser.getEmail());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    @Transactional
    public void statusNoShow(UUID idJob, User loggedUser){
        Job job = jobRepository.findById(idJob)
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado" + idJob));

        if (loggedUser.getRole() != TypeRole.ADMIN && loggedUser.getRole() != TypeRole.EMPLOYEE) {
            throw new IllegalArgumentException("Only ADMIN or EMPLOYEE can assign responsible.");
        }

        job.setStatusJob(StatusJob.NO_SHOW);
        job.setUpdatedBy(loggedUser.getEmail());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    @Transactional
    public void statusCanceled(UUID idJob, User loggedUser){
        Job job = jobRepository.findById(idJob)
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado" + idJob));

        if (loggedUser.getRole() != TypeRole.ADMIN && loggedUser.getRole() != TypeRole.EMPLOYEE) {
            throw new IllegalArgumentException("Only ADMIN or EMPLOYEE can assign responsible.");
        }

        job.setStatusJob(StatusJob.CANCELED);
        job.setUpdatedBy(loggedUser.getEmail());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    @Transactional
    public JobResponseDTO assignResponsible(UUID jobId, User loggedUser) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (loggedUser.getRole() != TypeRole.ADMIN && loggedUser.getRole() != TypeRole.EMPLOYEE) {
            throw new IllegalArgumentException("Only ADMIN or EMPLOYEE can assign responsible.");
        }

        job.setResponsible(loggedUser);
        job.setUpdatedBy(loggedUser.getEmail());
        job.setUpdatedAt(LocalDateTime.now());

        Job saved = jobRepository.save(job);

        return jobMapper.toDTO(saved);
    }
}
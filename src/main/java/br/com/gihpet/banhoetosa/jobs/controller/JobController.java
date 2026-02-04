package br.com.gihpet.banhoetosa.jobs.controller;

import br.com.gihpet.banhoetosa.jobs.domain.dto.JobRequestDTO;
import br.com.gihpet.banhoetosa.jobs.domain.dto.JobResponseDTO;
import br.com.gihpet.banhoetosa.jobs.domain.entity.Job;
import br.com.gihpet.banhoetosa.jobs.domain.enums.StatusJob;
import br.com.gihpet.banhoetosa.jobs.domain.service.JobService;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/api/jobs")
@Tag(name = "Jobs", description = "Jobs management")
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    public JobController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(UserDetails details) {
        return userService.findByEmail(details.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/pets/{idPet}/jobs")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Create a new job for the logged user and pet")
    public ResponseEntity<JobResponseDTO> create
            (@AuthenticationPrincipal UserDetails details,
             @PathVariable("idPet") UUID idPet,
             @RequestBody @Valid JobRequestDTO dto) {
        User logged = getAuthenticatedUser(details);
        JobResponseDTO response = jobService.create(dto, idPet, logged);
        return ResponseEntity.created(URI.create("/v1/api/jobs/" + response.id())).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "List all jobs")
    public ResponseEntity<List<JobResponseDTO>> findAll(@AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        return ResponseEntity.ok(jobService.findAll(logged));
    }

    @PatchMapping("/assign-responsible/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Assign logged user as responsible for a job")
    public ResponseEntity<JobResponseDTO> assignResponsible
            (@AuthenticationPrincipal UserDetails userDetails,
             @PathVariable("jobId") UUID jobId) {
        User loggedUser = getAuthenticatedUser(userDetails);
        JobResponseDTO updatedJob = jobService.assignResponsible(jobId, loggedUser);
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "Find job by id")
    public ResponseEntity<JobResponseDTO> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(jobService.findById(id));
    }

    @PatchMapping("/{id}/status/scheduled")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update job status for SCHEDULED")
    public ResponseEntity<Void> statusScheduled(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetails details) {

        User logged = getAuthenticatedUser(details);
        jobService.statusScheduled(id, logged);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status/started")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update job status for STARTED")
    public ResponseEntity<Void> statusStarted(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetails details) {

        User logged = getAuthenticatedUser(details);
        jobService.statusStarted(id, logged);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status/completed")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update job status for COMPLETED")
    public ResponseEntity<Void> statusCompleted(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetails details) {

        User logged = getAuthenticatedUser(details);
        jobService.statusCompleted(id, logged);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status/no-show")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update job status for NO SHOW")
    public ResponseEntity<Void> statusNoShow(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetails details) {

        User logged = getAuthenticatedUser(details);
        jobService.statusNoShow(id, logged);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status/canceled")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update job status for CANCELED")
    public ResponseEntity<Void> statusCanceled(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetails details) {

        User logged = getAuthenticatedUser(details);
        jobService.statusCanceled(id, logged);
        return ResponseEntity.noContent().build();
    }
}
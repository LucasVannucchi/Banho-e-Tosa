package br.com.gihpet.banhoetosa.jobs.domain.mapper;

import br.com.gihpet.banhoetosa.jobs.domain.dto.JobRequestDTO;
import br.com.gihpet.banhoetosa.jobs.domain.dto.JobResponseDTO;
import br.com.gihpet.banhoetosa.jobs.domain.entity.Job;
import br.com.gihpet.banhoetosa.jobs.domain.enums.TimeTaxiDog;
import br.com.gihpet.banhoetosa.pets.repository.PetRepository;
import br.com.gihpet.banhoetosa.users.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JobMapper {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public JobMapper(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    public Job toEntity(JobRequestDTO dto) {
        if (dto == null) return null;

        Job job = new Job();
        job.setTaxiDog(dto.taxiDog());
        if (!dto.taxiDog()) {
            job.setTimeTaxiDog(TimeTaxiDog.NONE);
        } else {
            job.setTimeTaxiDog(dto.timeTaxiDog());
        }
        job.setServiceType(dto.serviceType());
        job.setPrice(dto.serviceType().getPrice());
        job.setStatusJob(dto.statusJob());
        job.setScheduledDate(dto.scheduledDate());
        if (dto.notes() != null ) {
            job.setNotes(dto.notes());
        } else {
            job.setNotes("N/A");
        }
        return job;
    }

    public JobResponseDTO toDTO(Job job) {
        if (job == null) return null;

        List<UUID> paymentIds = job.getPayments().stream()
                .map(p -> p.getId())
                .toList();

        return new JobResponseDTO(
                job.getId(),
                job.getUser() != null ? job.getUser().getId() : null,
                job.getPet() != null ? job.getPet().getId() : null,
                job.getResponsible() != null ? job.getResponsible().getId() : null,
                job.isTaxiDog(),
                job.getTimeTaxiDog(),
                job.getServiceType(),
                job.getStatusJob(),
                job.getPrice(),
                job.getScheduledDate(),
                job.getNotes(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getCreatedBy(),
                job.getUpdatedBy(),
                paymentIds
        );
    }
}
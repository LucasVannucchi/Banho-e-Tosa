package br.com.gihpet.banhoetosa.jobs.domain.validator;

import br.com.gihpet.banhoetosa.jobs.domain.dto.JobRequestDTO;
import br.com.gihpet.banhoetosa.jobs.domain.enums.TimeTaxiDog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JobValidator {

    public void validate(JobRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Job data is required.");
        }

        if (dto.serviceType() == null) {
            throw new IllegalArgumentException("Service type is required.");
        }

        if (dto.scheduledDate() == null) {
            throw new IllegalArgumentException("Scheduled date is required.");
        }

        if (dto.scheduledDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date must be in the future.");
        }

        if (!dto.taxiDog() && dto.timeTaxiDog() != TimeTaxiDog.NONE) {
            throw new IllegalArgumentException("If TaxiDog is false, TimeTaxiDog must be NONE.");
        }

        if (dto.taxiDog() && (dto.timeTaxiDog() == null || dto.timeTaxiDog() == TimeTaxiDog.NONE)) {
            throw new IllegalArgumentException("If TaxiDog is true, TimeTaxiDog must be provided (TAKE/BACK/TAKE_BACK).");
        }
    }
}
package br.com.gihpet.banhoetosa.jobs.domain.dto;

import br.com.gihpet.banhoetosa.jobs.domain.enums.ServiceType;
import br.com.gihpet.banhoetosa.jobs.domain.enums.StatusJob;
import br.com.gihpet.banhoetosa.jobs.domain.enums.TimeTaxiDog;

import java.time.LocalDateTime;

public record JobRequestDTO(
        boolean taxiDog,
        TimeTaxiDog timeTaxiDog,
        ServiceType serviceType,
        StatusJob statusJob,
        LocalDateTime scheduledDate,
        String notes,
        Integer installments
) { }
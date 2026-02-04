package br.com.gihpet.banhoetosa.jobs.domain.dto;

import br.com.gihpet.banhoetosa.jobs.domain.enums.ServiceType;
import br.com.gihpet.banhoetosa.jobs.domain.enums.StatusJob;
import br.com.gihpet.banhoetosa.jobs.domain.enums.TimeTaxiDog;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JobResponseDTO(
        UUID id,
        UUID userId,
        UUID petId,
        UUID responsibleId,
        Boolean taxiDog,
        TimeTaxiDog timeTaxiDog,
        ServiceType serviceType,
        StatusJob statusJob,
        BigDecimal price,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime scheduledDate,
        String notes,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy,
        List<UUID> paymentIds
) { }
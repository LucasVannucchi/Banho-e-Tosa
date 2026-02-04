package br.com.gihpet.banhoetosa.jobs.domain.enums;

import java.math.BigDecimal;

public enum ServiceType {

    // Tosa Máquina
    MACHINE_TRIMMING_SMALL("Machine Trimming - Small Size", new BigDecimal("90.00")),
    MACHINE_TRIMMING_MEDIUM("Machine Trimming - Medium Size", new BigDecimal("110.00")),
    MACHINE_TRIMMING_LARGE("Machine Trimming - Large Size", new BigDecimal("130.00")),

    // Tosa Tesoura
    SCISSOR_TRIMMING_SMALL("Scissor Trimming - Small Size", new BigDecimal("110.00")),
    SCISSOR_TRIMMING_MEDIUM("Scissor Trimming - Medium Size", new BigDecimal("130.00")),
    SCISSOR_TRIMMING_LARGE("Scissor Trimming - Large Size", new BigDecimal("150.00")),

    // Tosa Verão
    SUMMER_TRIMMING("Summer Trimming", new BigDecimal("95.00")),

    // Tosa Higienica
    HYGIENIC_TRIMMING("Hygienic Trimming", new BigDecimal("10.00")),

    // Banho
    BATH_SMALL("Bath - Small Size", new BigDecimal("60.00")),
    BATH_MEDIUM("Bath - Medium Size", new BigDecimal("80.00")),
    BATH_LARGE("Bath - Large Size", new BigDecimal("120.00")),

    // Hidratação
    HYDRATION_SMALL_MEDIUM("Hydration - Small and Medium Size", new BigDecimal("70.00")),
    HYDRATION_LARGE("Hydration - Large Size", new BigDecimal("100.00")),

    // Pacote Mensal
    MONTHLY_PACKAGE_SMALL("Monthly Package - Small Size", new BigDecimal("210.00")),
    MONTHLY_PACKAGE_MEDIUM("Monthly Package - Medium Size", new BigDecimal("310.00")),
    MONTHLY_PACKAGE_LARGE("Monthly Package - Large Size", new BigDecimal("450.00"));

    private final String description;
    private final BigDecimal price;

    ServiceType(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
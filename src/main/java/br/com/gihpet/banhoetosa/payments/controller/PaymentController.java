package br.com.gihpet.banhoetosa.payments.controller;

import br.com.gihpet.banhoetosa.payments.domain.dto.AdminFinancialSummaryDTO;
import br.com.gihpet.banhoetosa.payments.domain.dto.PaymentSummaryDTO;
import br.com.gihpet.banhoetosa.payments.domain.enums.PaymentMethod;
import br.com.gihpet.banhoetosa.payments.domain.service.PaymentService;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("v1/api/payments")
@Tag(name = "Payments", description = "Payments and financial summaries")
public class PaymentController {

    private final PaymentService service;
    private final UserService userService;

    public PaymentController(PaymentService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    private User getAuthenticatedUser(UserDetails details) {
        return userService.findByEmail(details.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/{paymentId}/process")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "Process payment of job or installment")
    public ResponseEntity<String> processPayment(
            @PathVariable("paymentId") UUID id,
            @RequestParam(name = "paidAmount") BigDecimal amount,
            @RequestParam(name = "method") PaymentMethod method,
            @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        service.processPayment(id, amount, method);
        return ResponseEntity.ok("Payment processed by " + logged.getEmail());
    }

    @GetMapping("/summary/client/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "Get client balance (debt)")
    public ResponseEntity<BigDecimal> getClientBalance(@PathVariable("id") UUID id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        return ResponseEntity.ok(service.getClientBalance(user));
    }

    @GetMapping("/summary/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get total billed and pending (admin)")
    public ResponseEntity<AdminFinancialSummaryDTO> getSummary() {
        return ResponseEntity.ok(service.getAdminSummary());
    }

    @GetMapping("/summary/me")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "Get my financial summary")
    public ResponseEntity<?> getMySummary(@AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        if (logged.getRole() == TypeRole.ADMIN)
            return ResponseEntity.ok(service.getAdminSummary());
        return ResponseEntity.ok(service.getClientBalance(logged));
    }

    @GetMapping("/summary/me/pending")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    @Operation(summary = "Get pending or partial payments and total pending balance for the logged user")
    public ResponseEntity<Map<String, Object>> getMyPendingPayments(@AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);

        List<PaymentSummaryDTO> pendingPayments = service.getClientPendingPayments(logged);

        BigDecimal pendingBalance = pendingPayments.stream()
                .map(PaymentSummaryDTO::outstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();
        response.put("user", logged.getEmail());
        response.put("pendingBalance", pendingBalance);
        response.put("pendingPayments", pendingPayments);

        return ResponseEntity.ok(response);
    }
}
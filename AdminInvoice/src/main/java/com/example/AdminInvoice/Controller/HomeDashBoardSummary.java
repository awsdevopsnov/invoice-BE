package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.DashBoardPageSummary;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Service.DashBoardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class HomeDashBoardSummary {
    @Autowired
    private DashBoardPaymentService dashBoardPaymentService;
    @Autowired private TokenValidator tokenValidator;

    @PostMapping
    public ResponseEntity<DashBoardPageSummary> getDashBoardSummary(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> requestBody) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                String dateFilter = requestBody.get("filter");
                DashBoardPageSummary summary = dashBoardPaymentService.dashBoardSummary(dateFilter);
                return ResponseEntity.ok(summary);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (IllegalArgumentException e) {
            // Handle bad request scenario (e.g., invalid date filter)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

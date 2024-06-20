package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.DashboardBarchart;
import com.example.AdminInvoice.Entity.DashboardReport;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Service.DashboardReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class HomeDashboardReport {
    @Autowired
    private DashboardReportService dashboardReportService;
    @Autowired
    private TokenValidator tokenValidator;


    @PostMapping("/dashboardReport")
    public ResponseEntity<DashboardReport> responseEntity(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                DashboardReport dashboardReport = dashboardReportService.DASHBOARD_REPORTS();
                return ResponseEntity.status(HttpStatus.OK).body(dashboardReport);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/dashboardBarchart")
    public ResponseEntity<DashboardBarchart> response(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                DashboardBarchart dashboardBarchart = dashboardReportService.DASHBOARD_BARCHART();
                return ResponseEntity.status(HttpStatus.OK).body(dashboardBarchart);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

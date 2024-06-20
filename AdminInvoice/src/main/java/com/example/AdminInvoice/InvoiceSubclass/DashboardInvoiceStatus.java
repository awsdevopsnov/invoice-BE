package com.example.AdminInvoice.InvoiceSubclass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardInvoiceStatus {
    private DashboardInvoiceStatusSubClass draft;
    private DashboardInvoiceStatusSubClass pending;
    private DashboardInvoiceStatusSubClass approved;
    private DashboardInvoiceStatusSubClass paid;
    private DashboardInvoiceStatusSubClass overdue;
    private DashboardInvoiceStatusSubClass delete;
    private DashboardInvoiceStatusSubClass returned;

}

package com.example.AdminInvoice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardPageSummary {
    private DashBoardPayments invoiceOverview;
    private DashBoardPaymentBarchart invoiceStatus;
}

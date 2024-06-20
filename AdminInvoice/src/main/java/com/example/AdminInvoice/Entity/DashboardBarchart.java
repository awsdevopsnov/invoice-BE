package com.example.AdminInvoice.Entity;

import com.example.AdminInvoice.InvoiceSubclass.DashboardInvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardBarchart {
    private DashboardInvoiceStatus invoiceStatus;
}

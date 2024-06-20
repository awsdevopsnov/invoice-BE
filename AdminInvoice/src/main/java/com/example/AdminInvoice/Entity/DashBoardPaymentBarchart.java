package com.example.AdminInvoice.Entity;

import com.example.AdminInvoice.InvoiceSubclass.DashBoardInvoiceStatusSub;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardPaymentBarchart {
    private DashBoardInvoiceStatusSub pending;
    private DashBoardInvoiceStatusSub approved;
    private DashBoardInvoiceStatusSub returned;
    private DashBoardInvoiceStatusSub deleted;
    private DashBoardInvoiceStatusSub draft;
    private DashBoardInvoiceStatusSub paid;
}


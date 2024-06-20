package com.example.AdminInvoice.Entity;

import com.example.AdminInvoice.InvoiceSubclass.DashBoardPaymentsSubClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardPayments {
    private DashBoardPaymentsSubClass total;
    private DashBoardPaymentsSubClass paid;
    private DashBoardPaymentsSubClass unPaid;



}

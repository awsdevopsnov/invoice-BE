package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.CustomizeReportARSummary;
import com.example.AdminInvoice.Entity.DashBoardPageSummary;
import com.example.AdminInvoice.Entity.DashBoardPayments;
import com.example.AdminInvoice.Entity.InvoiceDto;

import java.util.List;

public interface DashBoardPaymentService {
    public DashBoardPageSummary dashBoardSummary(String dateFilter) throws Exception;
}

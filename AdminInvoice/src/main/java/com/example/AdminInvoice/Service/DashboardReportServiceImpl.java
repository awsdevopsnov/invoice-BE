package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.DashboardBarchart;
import com.example.AdminInvoice.Entity.DashboardReport;
import com.example.AdminInvoice.Entity.InvoiceDto;
import com.example.AdminInvoice.InvoiceSubclass.*;
import com.example.AdminInvoice.ReportFilter.DateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DashboardReportServiceImpl implements DashboardReportService {
    @Autowired
    private DateFilter dateFilter;
    @Autowired
    private InvoiceService invoiceService;


    @Override
    public DashboardReport DASHBOARD_REPORTS() throws Exception {
        // Get all invoices
        List<InvoiceDto> invoices = invoiceService.getAllInvoices();

        // Initialize variables to hold totals and counts for each category
        double currentDaysTotalAmount = 0.0;
        long currentDaysNoOfCustomers = 0;
        long currentDaysNoOfInvoices = 0;

        double overdueDaysTotalAmount = 0.0;
        long overdueDaysNoOfCustomers = 0;
        long overdueDaysNoOfInvoices = 0;

        double above45DaysTotalAmount = 0.0;
        long above45DaysNoOfCustomers = 0;
        long above45DaysNoOfInvoices = 0;

        double overallTotal = 0.0;

        Set<String> currentDaysCustomers = new HashSet<>();
        Set<String> overdueDaysCustomers = new HashSet<>();
        Set<String> above45DaysCustomers = new HashSet<>();

        // Calculate total amounts for each aging category and overall total
        for (InvoiceDto invoice : invoices) {
            long daysPastDue = dateFilter.calculateDaysPastDue(invoice.getDueDate());

            if (daysPastDue <= 30) {
                currentDaysTotalAmount += invoice.getTotalAmount();
                currentDaysNoOfInvoices++;
                currentDaysCustomers.add(invoice.getCustomerName());
            } else if (daysPastDue <= 45) {
                overdueDaysTotalAmount += invoice.getTotalAmount();
                overdueDaysNoOfInvoices++;
                overdueDaysCustomers.add(invoice.getCustomerName());
            } else {
                above45DaysTotalAmount += invoice.getTotalAmount();
                above45DaysNoOfInvoices++;
                above45DaysCustomers.add(invoice.getCustomerName());
            }
            overallTotal += invoice.getTotalAmount();
        }

        currentDaysNoOfCustomers = currentDaysCustomers.size();
        overdueDaysNoOfCustomers = overdueDaysCustomers.size();
        above45DaysNoOfCustomers = above45DaysCustomers.size();

        // Create and set current day report
        DashboardReportCurrentDay currentDayReport = new DashboardReportCurrentDay();
        currentDayReport.setTotalAmount(currentDaysTotalAmount);
        currentDayReport.setNoOfCustomers(currentDaysNoOfCustomers);
        currentDayReport.setNoOfInvoices(currentDaysNoOfInvoices);

        // Create and set overdue day report
        DashboardReportOverdueDay overdueDayReport = new DashboardReportOverdueDay();
        overdueDayReport.setTotalAmount(overdueDaysTotalAmount);
        overdueDayReport.setNoOfCustomers(overdueDaysNoOfCustomers);
        overdueDayReport.setNoOfInvoices(overdueDaysNoOfInvoices);

        // Create and set above 45 day report
        DashboardReportAbove45Day above45DayReport = new DashboardReportAbove45Day();
        above45DayReport.setTotalAmount(above45DaysTotalAmount);
        above45DayReport.setNoOfCustomers(above45DaysNoOfCustomers);
        above45DayReport.setNoOfInvoices(above45DaysNoOfInvoices);

        // Create a DashboardReport instance and set calculated values
        DashboardReport dashboardReport = new DashboardReport();
        dashboardReport.setId(UUID.randomUUID().toString());
        dashboardReport.setCurrentDays(currentDayReport);
        dashboardReport.setOverdueDays(overdueDayReport);
        dashboardReport.setAbove45Days(above45DayReport);
        dashboardReport.setTotalReceivables(overallTotal);

        return dashboardReport;
    }

    @Override
    public DashboardBarchart DASHBOARD_BARCHART() throws Exception {
        List<InvoiceDto> list=invoiceService.getAllInvoices();

        DashboardInvoiceStatus invoiceStatus = new DashboardInvoiceStatus();
        invoiceStatus.setDraft(new DashboardInvoiceStatusSubClass(0, 0.0));
        invoiceStatus.setPending(new DashboardInvoiceStatusSubClass(0, 0.0));
        invoiceStatus.setApproved(new DashboardInvoiceStatusSubClass(0, 0.0));
        invoiceStatus.setPaid(new DashboardInvoiceStatusSubClass(0, 0.0));
        invoiceStatus.setOverdue(new DashboardInvoiceStatusSubClass(0, 0.0));
        invoiceStatus.setDelete(new DashboardInvoiceStatusSubClass(0, 0.0));
        invoiceStatus.setReturned(new DashboardInvoiceStatusSubClass(0, 0.0));

        for (InvoiceDto invoice : list) {
            InvoiceStatus status = invoice.getInvoiceStatus(); // Assuming InvoiceDto has a method getStatus() returning InvoiceStatus enum
            double amount = invoice.getTotalAmount();

            switch (status) {
                case DRAFT:
                    invoiceStatus.getDraft().setNoOfInvoices(invoiceStatus.getDraft().getNoOfInvoices() + 1);
                    invoiceStatus.getDraft().setTotalAmount(invoiceStatus.getDraft().getTotalAmount() + amount);
                    break;
                case PENDING:
                    invoiceStatus.getPending().setNoOfInvoices(invoiceStatus.getPending().getNoOfInvoices() + 1);
                    invoiceStatus.getPending().setTotalAmount(invoiceStatus.getPending().getTotalAmount() + amount);
                    break;
                case APPROVED:
                    invoiceStatus.getApproved().setNoOfInvoices(invoiceStatus.getApproved().getNoOfInvoices() + 1);
                    invoiceStatus.getApproved().setTotalAmount(invoiceStatus.getApproved().getTotalAmount() + amount);
                    break;
                case PAID:
                    invoiceStatus.getPaid().setNoOfInvoices(invoiceStatus.getPaid().getNoOfInvoices() + 1);
                    invoiceStatus.getPaid().setTotalAmount(invoiceStatus.getPaid().getTotalAmount() + amount);
                    break;
                case OVERDUE:
                    invoiceStatus.getOverdue().setNoOfInvoices(invoiceStatus.getOverdue().getNoOfInvoices() + 1);
                    invoiceStatus.getOverdue().setTotalAmount(invoiceStatus.getOverdue().getTotalAmount() + amount);
                    break;
                case DELETE:
                    invoiceStatus.getDelete().setNoOfInvoices(invoiceStatus.getDelete().getNoOfInvoices() + 1);
                    invoiceStatus.getDelete().setTotalAmount(invoiceStatus.getDelete().getTotalAmount() + amount);
                    break;
                case RETURNED:
                    invoiceStatus.getReturned().setNoOfInvoices(invoiceStatus.getReturned().getNoOfInvoices() + 1);
                    invoiceStatus.getReturned().setTotalAmount(invoiceStatus.getReturned().getTotalAmount() + amount);
                    break;
            }
        }

        DashboardBarchart dashboardBarchart = new DashboardBarchart();
        dashboardBarchart.setInvoiceStatus(invoiceStatus);
        return dashboardBarchart;
    }
}
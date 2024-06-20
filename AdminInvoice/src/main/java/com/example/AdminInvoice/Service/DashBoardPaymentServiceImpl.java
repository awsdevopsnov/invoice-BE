package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.DashBoardPageSummary;
import com.example.AdminInvoice.Entity.DashBoardPaymentBarchart;
import com.example.AdminInvoice.Entity.DashBoardPayments;
import com.example.AdminInvoice.Entity.InvoiceDto;
import com.example.AdminInvoice.InvoiceSubclass.DashBoardInvoiceStatusSub;
import com.example.AdminInvoice.InvoiceSubclass.DashBoardPaymentsSubClass;
import com.example.AdminInvoice.InvoiceSubclass.InvoiceStatus;
import com.example.AdminInvoice.ReportFilter.DateFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class DashBoardPaymentServiceImpl implements DashBoardPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(DashBoardPaymentServiceImpl.class);
    @Autowired private MongoTemplate mongoTemplate;
    @Autowired private DateFilter dateFilter;
    @Autowired private InvoiceService invoiceService;

    @Override
    public DashBoardPageSummary dashBoardSummary(String dateFilter) throws Exception {
        try {
            List<InvoiceDto> invoices = invoiceService.getInvoicesByDateFilter(dateFilter);

            DashBoardPayments totalPayments = new DashBoardPayments(
                    new DashBoardPaymentsSubClass(0.0, 0, 0),
                    new DashBoardPaymentsSubClass(0.0, 0, 0),
                    new DashBoardPaymentsSubClass(0.0, 0, 0)
            );

            DashBoardPaymentBarchart dashBoardPaymentBarchart = new DashBoardPaymentBarchart(
                    new DashBoardInvoiceStatusSub(0),
                    new DashBoardInvoiceStatusSub(0),
                    new DashBoardInvoiceStatusSub(0),
                    new DashBoardInvoiceStatusSub(0),
                    new DashBoardInvoiceStatusSub(0),
                    new DashBoardInvoiceStatusSub(0)
            );

            Map<String, Set<String>> customerTracker = new HashMap<>();
            customerTracker.put("total", new HashSet<>());
            customerTracker.put("paid", new HashSet<>());
            customerTracker.put("unpaid", new HashSet<>());

            for (InvoiceDto invoice : invoices) {
                double amount = invoice.getTotalAmount();
                String customerId = invoice.getCustomerName();
                customerTracker.get("total").add(customerId);

                totalPayments.getTotal().setTotalAmount(totalPayments.getTotal().getTotalAmount() + amount);
                totalPayments.getTotal().setNoOfInvoices(totalPayments.getTotal().getNoOfInvoices() + 1);

                if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
                    totalPayments.getPaid().setTotalAmount(totalPayments.getPaid().getTotalAmount() + amount);
                    totalPayments.getPaid().setNoOfInvoices(totalPayments.getPaid().getNoOfInvoices() + 1);
                    customerTracker.get("paid").add(customerId);
                } else {
                    totalPayments.getUnPaid().setTotalAmount(totalPayments.getUnPaid().getTotalAmount() + amount);
                    totalPayments.getUnPaid().setNoOfInvoices(totalPayments.getUnPaid().getNoOfInvoices() + 1);
                    customerTracker.get("unpaid").add(customerId);
                }

                DashBoardInvoiceStatusSub statusSubClass;
                switch (invoice.getInvoiceStatus()) {
                    case DRAFT:
                        statusSubClass = dashBoardPaymentBarchart.getDraft();
                        break;
                    case PENDING:
                        statusSubClass = dashBoardPaymentBarchart.getPending();
                        break;
                    case APPROVED:
                        statusSubClass = dashBoardPaymentBarchart.getApproved();
                        break;
                    case RETURNED:
                        statusSubClass = dashBoardPaymentBarchart.getReturned();
                        break;
                    case DELETE:
                        statusSubClass = dashBoardPaymentBarchart.getDeleted();
                        break;
                    case PAID:
                        statusSubClass = dashBoardPaymentBarchart.getPaid();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + invoice.getInvoiceStatus());
                }
                statusSubClass.setNoOfInvoices(statusSubClass.getNoOfInvoices() + 1);
            }

            totalPayments.getTotal().setNoOfCustomers(customerTracker.get("total").size());
            totalPayments.getPaid().setNoOfCustomers(customerTracker.get("paid").size());
            totalPayments.getUnPaid().setNoOfCustomers(customerTracker.get("unpaid").size());

            DashBoardPageSummary dashBoardPageSummary = new DashBoardPageSummary(totalPayments, dashBoardPaymentBarchart);

            return dashBoardPageSummary;
        } catch (Exception e) {
            logger.error("Error in dashBoardSummary method", e);
            throw e;
        }
    }
}

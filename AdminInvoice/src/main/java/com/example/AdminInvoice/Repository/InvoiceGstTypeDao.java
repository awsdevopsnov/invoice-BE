package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.InvoiceGstType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceGstTypeDao extends MongoRepository<InvoiceGstType,String> {
    Optional<InvoiceGstType> findByGstName(String gstName);
}

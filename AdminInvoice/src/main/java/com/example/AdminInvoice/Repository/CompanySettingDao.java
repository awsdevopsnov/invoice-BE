package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanySettingDao extends MongoRepository<CompanySettings,String> {
    Optional<CompanySettings> findByCompanyEmail(String companyEmail);
    Optional<CompanySettings> findById(String id);

    @Query("{}")
    Optional<CompanySettings> findFirstCompanySettings();

}

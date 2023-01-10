package com.example.auth.repository;

import com.example.auth.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice,String> {

//List<Invoice> findCustomerIdBySoftDeleteFalse(String customerId);
}

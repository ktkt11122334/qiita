package com.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.Customer;



public interface CustomerRepository extends JpaRepository<Customer, Long>  {

  @Query(" SELECT customer FROM Customer customer WHERE customer.tellNumber = ?1 AND customer.firstName = ?2 AND customer.lastName = ?3 ")
  public Customer findCustomerByTellNumberAndCustomer_name(String tell_number, String first_name, String last_name);

}

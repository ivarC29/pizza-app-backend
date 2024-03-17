package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends ListCrudRepository<CustomerEntity, String> {

    @Query(value = "SELECT c FROM CustomerEntity c WHERE c.phone = :phone")
    CustomerEntity findByPhone(@Param("phone") String phone);

}

 package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.CustomerEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


 @DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerEntity customer;
    private String phone;

    @BeforeEach
    public void setUp() throws Exception {
        phone = "123456789";
        customer = CustomerEntity.builder()
                .idCustomer("2346454337")
                .name("Jordan Salas")
                .address("Miramar L-19 Pt Alta")
                .email("jorgabwy@gmail.com")
                .phone(phone)
                .build();
    }

    @Test
    public void testFindByPhone() {
        // Given
        CustomerEntity savedCustomer = customerRepository.save(customer);
        // When
        CustomerEntity searchedCustomer = customerRepository.findByPhone(phone);
        // Then
        assertEquals(searchedCustomer.getIdCustomer(), savedCustomer.getIdCustomer());
        assertEquals(searchedCustomer.getPhone(), savedCustomer.getPhone());
    }
}

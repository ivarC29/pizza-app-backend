package com.ivarc29.pizza.web.controller;

import com.ivarc29.pizza.persistence.entity.CustomerEntity;
import com.ivarc29.pizza.persistence.entity.OrderEntity;
import com.ivarc29.pizza.service.CustomerService;
import com.ivarc29.pizza.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @Autowired
    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerEntity> getByPhone(@PathVariable("phone") String phone) {
        try {
            CustomerEntity customer = this.customerService.findByPhone(phone);

            if (customer != null)
                return ResponseEntity.ok(customer);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderEntity>> getCustomerOrder(@PathVariable("id") String idCustomer) {
        try {
            List<OrderEntity> orders = this.orderService.getCustomerOrders(idCustomer);

            if (orders.size() > 0)
                return ResponseEntity.ok(orders);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

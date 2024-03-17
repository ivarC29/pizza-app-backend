package com.ivarc29.pizza.web.controller;

import com.ivarc29.pizza.persistence.entity.OrderEntity;
import com.ivarc29.pizza.persistence.projection.OrderSummary;
import com.ivarc29.pizza.service.OrderService;
import com.ivarc29.pizza.service.dto.RandomOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAll() {
        try {
            List<OrderEntity> orders = this.orderService.getAll();

            if (orders.size() > 0)
                return ResponseEntity.ok(orders);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/today")
    public ResponseEntity<List<OrderEntity>> getTodayOrders() {
        try {
            List<OrderEntity> orders = this.orderService.getTodayOrders();

            if (orders.size() > 0)
                return ResponseEntity.ok(orders);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/outside")
    public ResponseEntity<List<OrderEntity>> getOutsideOrder() {
        try {
            List<OrderEntity> orders = this.orderService.getOutsideOrders();

            if (orders.size() > 0)
                return ResponseEntity.ok(orders);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderEntity>> getCustomerOrders(@PathVariable("id") String idCustomer) {
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

    @GetMapping("/summary/{id}")
    public ResponseEntity<OrderSummary> getSummary(@PathVariable("id") Integer orderId) {
        try {
            OrderSummary summary = this.orderService.getSummary(orderId);

            if (summary != null)
                return ResponseEntity.ok(summary);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/random")
    public ResponseEntity<Boolean> randomOrder(@RequestBody RandomOrderDto dto) {
        try {
            return ResponseEntity.ok(this.orderService.saveRandomOrder(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}

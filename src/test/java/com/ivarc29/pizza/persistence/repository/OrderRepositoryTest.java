package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.CustomerEntity;
import com.ivarc29.pizza.persistence.entity.OrderEntity;
import com.ivarc29.pizza.persistence.projection.OrderSummary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;


    private CustomerEntity customer;
    private LocalDateTime today;
    private OrderEntity order;
    private OrderEntity order2;

    @BeforeEach
    public void setUp() throws Exception {
        today = LocalDateTime.now();

        customer = CustomerEntity.builder()
                .idCustomer("2346454337")
                .name("Jordan Salas")
                .address("Miramar L-19 Pt Alta")
                .email("jorgabwy@gmail.com")
                .phone("987182934")
                .build();
        customer = customerRepository.save(customer);

        order = OrderEntity.builder()
                .idCustomer("2346454337")
                .date(today)
                .total(44.00)
                .method("D")
                .additionalNotes("Don't forget add extra sauce please ")
                .build();
        order2 = OrderEntity.builder()
                .idCustomer("2346454337")
                .date(today.minusDays(2))
                .total(44.00)
                .method("C")
                .additionalNotes("Hurry up please ")
                .build();
    }

    @Test
    @DisplayName("Test to get all orders after given date")
    public void TestFindAllByDateAfter() {
        // Given
        OrderEntity savedOrder = orderRepository.save(order);// Save order with today date
        OrderEntity savedOrder2 = orderRepository.save(order2);// Save order dated 2 days ago
        // When
        List<OrderEntity> orders = orderRepository.findAllByDateAfter(today.minusDays(1));
        // Then
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.stream().allMatch( order ->
                order.getDate().isAfter(today.minusDays(1))
                )).isTrue();
    }

    @Test
    public void TestFindAllByMethodIn() {
        // Given
        OrderEntity savedOrder = orderRepository.save(order);// Save order with method delivery "D"
        OrderEntity savedOrder2 = orderRepository.save(order2);// Save order with method carryout "C"
        // When
        System.out.println(savedOrder.getIdOrder());
        System.out.println(savedOrder2.getIdOrder());
        List<OrderEntity> orders = orderRepository.findAllByMethodIn(Arrays.asList("D", "C"));
        // Then
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders.stream().allMatch( order ->
                order.getMethod().equals("D") ||
                        order.getMethod().equals("C")
        )).isTrue();
    }

    @Test
    public void TestFindCustomerOrders() {
        // Given
        OrderEntity savedOrder = orderRepository.save(order);// Save order with method delivery "D"
        OrderEntity savedOrder2 = orderRepository.save(order2);// Save order with method carryout "C"
        // When
        List<OrderEntity> orders = orderRepository.findCustomerOrders(customer.getIdCustomer());
        // Then
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders.stream().allMatch( order ->
                order.getIdCustomer().equals(customer.getIdCustomer())
        )).isTrue();
    }

    // TODO: Necesita datos de Item y pizzs para el test.
    @Test
    public void TestFindSummary() {
        // Given
        OrderEntity savedOrder = orderRepository.save(order);
        System.out.println("Id Order Saved");
        System.out.println(savedOrder.getIdOrder());
        // When
        OrderSummary summary = orderRepository.findSummary(savedOrder.getIdOrder());
        System.out.println("Id Summary Recovered");
        System.out.println(summary);
        // Then
//        assertThat(summary).isNotNull();
//        assertThat(summary.getIdOrder()).isEqualTo(savedOrder.getIdOrder());
//        assertThat(summary.getCustomerName()).isEqualTo(customer.getName());

    }

    // TODO: Add procedure before execute this test
    @Test
    public void TestSaveRandomOrder() {
        // Given

        // When

        // Then
//        Assertions.assertEquals(true, orderRepository.saveRandomOrder(customer.getIdCustomer(), "M"));
    }
}
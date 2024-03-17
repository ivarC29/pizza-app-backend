package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.OrderEntity;
import com.ivarc29.pizza.persistence.projection.OrderSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByDateAfter(LocalDateTime date);
    List<OrderEntity> findAllByMethodIn(List<String> methods);

    @Query(value = "SELECT * FROM pizza_order WHERE id_customer= :id", nativeQuery = true)
    List<OrderEntity> findCustomerOrders(@Param("id") String idCustomer);

    @Query(value =
            """
            SELECT
                po.id_order "idOrder",
                cu.name "CustomerName",
                po.date "orderDate",
                po.total "orderTotal",
                STRING_AGG(pi.name, ', ') "pizzaNames"
            FROM pizza_order po
                JOIN customer cu
                    ON po.id_customer = cu.id_customer
                JOIN order_item oi
                    ON po.id_order = oi.id_order
                JOIN pizza pi
                    ON oi.id_pizza = pi.id_pizza
            WHERE po.id_order = :orderId
            GROUP BY po.id_order, cu.name, po.date, po.total;
    
            """, nativeQuery = true)
    OrderSummary findSummary(@Param("orderId") Integer orderId);

    @Procedure(value = "take_random_pizza_order", outputParameterName = "order_taken")
    boolean saveRandomOrder(@Param("id_customer") String idCustomer, @Param("method") String method);

}

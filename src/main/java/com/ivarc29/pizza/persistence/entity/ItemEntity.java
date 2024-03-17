package com.ivarc29.pizza.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item")
@IdClass(IdItem.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity {

    @Id
    @Column(name = "id_order", nullable = false)
    private Integer                 idOrder;

    @Id
    @Column(name = "id_item", nullable = false)
    private Integer                  idItem;

    @Column(name = "id_pizza", nullable = false)
    private Integer                 idPizza;

    @Column(nullable = false, columnDefinition = "NUMERIC(2,1)")
    private Double                  quantity;

    @Column(nullable = false, columnDefinition = "NUMERIC(5,2)")
    private Double                  price;

    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id_order", insertable = false, updatable = false)
    @JsonIgnore
    private OrderEntity order;

    @OneToOne
    @JoinColumn(name = "id_pizza", referencedColumnName = "id_pizza", insertable = false, updatable = false)
    private PizzaEntity             pizza;

}

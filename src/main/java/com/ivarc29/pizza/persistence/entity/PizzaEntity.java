package com.ivarc29.pizza.persistence.entity;

import com.ivarc29.pizza.persistence.audit.AuditPizzaListener;
import com.ivarc29.pizza.persistence.audit.AuditableEntity;
import com.ivarc29.pizza.util.BooleanToSmallintConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pizza")
@EntityListeners({AuditingEntityListener.class, AuditPizzaListener.class})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pizza", nullable = false)
    private Integer         idPizza;

    @Column(nullable = false, length = 30, unique = true)
    private String          name;

    @Column(nullable = false, length = 150)
    private String          description;

    @Column(nullable = false, columnDefinition = "NUMERIC(5,2)")
    private Double          price;

    @Column(columnDefinition = "SMALLINT")
    @Convert(converter = BooleanToSmallintConverter.class)
    private Boolean         vegetarian;

    @Column(columnDefinition = "SMALLINT")
    @Convert(converter = BooleanToSmallintConverter.class)
    private Boolean         vegan;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    @Convert(converter = BooleanToSmallintConverter.class)
    private Boolean         available;

    @Override
    public String toString() {
        return "PizzaEntity{" +
                "idPizza=" + idPizza +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", vegetarian=" + vegetarian +
                ", vegan=" + vegan +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaEntity that = (PizzaEntity) o;
        return Objects.equals(idPizza, that.idPizza) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(vegetarian, that.vegetarian) && Objects.equals(vegan, that.vegan) && Objects.equals(available, that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPizza, name, description, price, vegetarian, vegan, available);
    }
}

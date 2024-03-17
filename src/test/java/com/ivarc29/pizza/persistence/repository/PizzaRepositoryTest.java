package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.PizzaEntity;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class PizzaRepositoryTest {

    @Autowired
    private PizzaRepository pizzaRepository;

    private PizzaEntity pizza;

    @BeforeEach
    void setup() {
        pizza = PizzaEntity.builder()
                .name("Americana")
                .description("Carne, tocino y cebolla")
                .price(20.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
    }

    @DisplayName("Test for save pizza entity in database.")
    @Test
    public void testSave() {
        // Given
        // - Pizza object created in setup() method;
        // When
        PizzaEntity savedPizza = pizzaRepository.save(pizza);
        // Then
        assertThat(savedPizza).isNotNull();
        assertThat(savedPizza.getIdPizza()).isGreaterThan(0);
    }

    @DisplayName("Test for get all pizzas in db.")
    @Test
    public void testGetAll() {
        // Given
        PizzaEntity pizza1 = PizzaEntity.builder()
                .name("Rumani")
                .description("chesse and vegetables")
                .price(21.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        pizzaRepository.save(pizza);
        pizzaRepository.save(pizza1);

        // When
        List<PizzaEntity> pizzas = pizzaRepository.findAll();

        // Then
        assertThat(pizzas).isNotNull();
        assertThat(pizzas.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Test to update pizza entity in database")
    public void testUpdate() {
        // Given
        pizzaRepository.save(pizza);
        // When
        PizzaEntity pizzaToUpdate = pizzaRepository.findById(pizza.getIdPizza()).get();
        pizzaToUpdate.setName("VeganFull");
        pizzaToUpdate.setDescription("Alternative cheesse with onion and vegetable");
        pizzaToUpdate.setVegan(true);
        PizzaEntity updatedPizza = pizzaRepository.save(pizzaToUpdate);
        // Then
        assertThat(updatedPizza.getName()).isEqualTo("VeganFull");
        assertThat(updatedPizza.getDescription()).isEqualTo("Alternative cheesse with onion and vegetable");
        assertThat(updatedPizza.getVegan()).isTrue();
    }

    @DisplayName("Test to delete pizza from database")
    @Test
    public void testDelete() {
        // Given
        pizzaRepository.save(pizza);
        // When
        pizzaRepository.deleteById(pizza.getIdPizza());
        Optional<PizzaEntity> pizzaOptional = pizzaRepository.findById(pizza.getIdPizza());
        // Then
        assertThat(pizzaOptional).isEmpty();
    }
}
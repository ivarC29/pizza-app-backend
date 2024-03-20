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
                .name("American")
                .description("Meat, Bacon y Onion")
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
                .name("Rumanian")
                .description("cheese and vegetables")
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
    @DisplayName("Test method query for search pizza by name and available value(Ignore Case).")
    public void testFindFirstByAvailableTrueAndNameIgnoreCase() {
        // Given
        String name = "american";
        pizzaRepository.save(pizza);
        // When
        PizzaEntity savedPizza = pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name);
        //Then
        assertThat(savedPizza).isNotNull();
        assertThat(savedPizza.getName()).isEqualTo("American");
        assertThat(savedPizza.getAvailable()).isTrue();
    }

    @Test
    @DisplayName("Test method query get pizza available with description containing(Ignore Case).")
    public void TestFindAllByAvailableTrueAndDescriptionContainingIgnoreCase() {
        // Given
        String ingredient = "bacon";
        PizzaEntity pizza1 = PizzaEntity.builder()
                .name("PizzaPork")
                .description("cheese Bacon and vegetables")
                .price(21.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
        pizzaRepository.save(pizza);
        pizzaRepository.save(pizza1);
        // When
        List<PizzaEntity> savedPizzas = pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
        // Then
        assertThat(savedPizzas.size()).isNotNull();
        assertThat(savedPizzas.size()).isEqualTo(2);
        assertThat(savedPizzas.stream().allMatch(pizzaEntity ->
                pizzaEntity.getDescription().contains("Bacon")
                        && pizzaEntity.getAvailable()
        )).isTrue();
    }

    @Test
    @DisplayName("Test method query get pizza available with description not containing(Ignore Case).")
    public void TestFindAllByAvailableTrueAndDescriptionNotContainingIgnoreCase() {
        // Given
        String ingredient = "bacon";
        PizzaEntity pizza1 = PizzaEntity.builder()
                .name("PizzaFruit")
                .description("cheese Fruit and vegetables")
                .price(21.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        PizzaEntity pizza2 = PizzaEntity.builder()
                .name("PizzaChicken")
                .description("cheese Chicken and vegetables")
                .price(21.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
        pizzaRepository.save(pizza1);
        pizzaRepository.save(pizza2);
        // When
        List<PizzaEntity> savedPizzas = pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
        // Then
        assertThat(savedPizzas.size()).isNotNull();
        assertThat(savedPizzas.size()).isEqualTo(2);
        assertThat(savedPizzas.stream().allMatch(pizzaEntity ->
                !pizzaEntity.getDescription().contains("Bacon")
                        && pizzaEntity.getAvailable()
        )).isTrue();
    }

    @Test
    @DisplayName("Test get top 3 pizzas available with price less than equal to.(Order Asc)")
    public void testFindTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc() {
        // Given
        PizzaEntity pizza1 = PizzaEntity.builder()
                .name("Margarita")
                .description("Olive oil, Fresh tomato, and basil")
                .price(21.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        PizzaEntity pizza2 = PizzaEntity.builder()
                .name("PizzaFruit")
                .description("cheese Fruit and vegetables")
                .price(22.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        PizzaEntity pizza3 = PizzaEntity.builder()
                .name("PizzaChicken")
                .description("cheese Chicken and vegetables")
                .price(19.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
        pizzaRepository.save(pizza);
        pizzaRepository.save(pizza1);
        pizzaRepository.save(pizza2);
        pizzaRepository.save(pizza3);
        Double price = 22.0;
        // When
        List<PizzaEntity> savedPizzas = pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
        // Then
        assertThat(savedPizzas.size()).isNotNull();
        assertThat(savedPizzas.size()).isEqualTo(3);
        assertThat(savedPizzas.stream().allMatch( pizza ->
                pizza.getPrice() < 22.0
        )).isTrue();
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
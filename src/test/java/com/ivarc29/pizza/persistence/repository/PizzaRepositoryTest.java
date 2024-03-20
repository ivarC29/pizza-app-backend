package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.PizzaEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.ivarc29.pizza.service.dto.UpdatePizzaPriceDto;
import jakarta.transaction.Transactional;
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
    private PizzaEntity pizza1;
    private PizzaEntity pizza2;
    private PizzaEntity pizza3;
    private PizzaEntity pizza4;
    private PizzaEntity pizza5;
    private PizzaEntity pizza6;
    private PizzaEntity pizza7;
    private PizzaEntity pizza8;
    private PizzaEntity pizza9;


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
        pizza1 = PizzaEntity.builder()
                .name("Rumanian")
                .description("cheese and vegetables")
                .price(21.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        pizza2 = PizzaEntity.builder()
                .name("PizzaPork")
                .description("cheese Bacon and vegetables")
                .price(21.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
        pizza3 = PizzaEntity.builder()
                .name("PizzaFruit")
                .description("cheese Fruit and vegetables")
                .price(21.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        pizza4 = PizzaEntity.builder()
                .name("PizzaChicken")
                .description("cheese Chicken and vegetables")
                .price(21.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
        pizza5 = PizzaEntity.builder()
                .name("Margarita")
                .description("Olive oil, Fresh tomato, and basil")
                .price(21.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        pizza6 = PizzaEntity.builder()
                .name("PizzaFruit")
                .description("cheese Fruit and vegetables")
                .price(22.0)
                .vegetarian(true)
                .vegan(false)
                .available(true)
                .build();
        pizza7 = PizzaEntity.builder()
                .name("PizzaChicken")
                .description("cheese Chicken and vegetables")
                .price(19.0)
                .vegetarian(false)
                .vegan(false)
                .available(true)
                .build();
        pizza8 = PizzaEntity.builder()
                .name("Vegan Margherita")
                .description("Tomato sauce, Vegan mozzarella cheese and Fresh basil leaves")
                .price(26.0)
                .vegetarian(false)
                .vegan(true)
                .available(true)
                .build();
        pizza9 = PizzaEntity.builder()
                .name("Vegan BBQ Chickpea")
                .description("BBQ sauce, Red onion, thinly sliced and Fresh cilantro")
                .price(25.0)
                .vegetarian(false)
                .vegan(true)
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
        pizzaRepository.save(pizza);
        pizzaRepository.save(pizza2);
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
        pizzaRepository.save(pizza3);
        pizzaRepository.save(pizza4);
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
        pizzaRepository.save(pizza);
        pizzaRepository.save(pizza5);
        pizzaRepository.save(pizza6);
        pizzaRepository.save(pizza7);
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
    @DisplayName("Test count vegan pizzas.")
    public void TestCountByVeganTrue() {
        // Given
        pizzaRepository.save(pizza8);
        pizzaRepository.save(pizza9);
        // When
        int veganPizzasNumber = pizzaRepository.countByVeganTrue();
        // Then
        assertThat(veganPizzasNumber).isNotNull();
        assertThat(veganPizzasNumber).isEqualTo(2);
    }

    // TODO: Revisar este test.
//    @Test
//    @DisplayName("Test update price of pizza.")
//    @Transactional
//    public void TestUpdatePrice() {
//        // Given
//        PizzaEntity toUpdatedPizza = pizzaRepository.save(pizza);
//        UpdatePizzaPriceDto newPrice = new UpdatePizzaPriceDto();
//        newPrice.setPizzaId(toUpdatedPizza.getIdPizza());
//        newPrice.setNewPrice(27.0);
//        // When
//        pizzaRepository.updatePrice(newPrice);
//        Optional<PizzaEntity> updatedPizza = pizzaRepository.findById(toUpdatedPizza.getIdPizza());
//        // Then
//        assertThat(updatedPizza.get().getPrice()).isEqualTo(newPrice.getNewPrice());
//
//    }

    @Test
    @DisplayName("Test to update pizza entity in database")
    public void testUpdate() {
        // Given
        PizzaEntity savedPizza = pizzaRepository.save(pizza);
        // When
        PizzaEntity pizzaToUpdate = pizzaRepository.findById(savedPizza.getIdPizza()).get();
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
        PizzaEntity savedPizza = pizzaRepository.save(pizza);
        // When
        pizzaRepository.deleteById(savedPizza.getIdPizza());
        Optional<PizzaEntity> pizzaOptional = pizzaRepository.findById(savedPizza.getIdPizza());
        // Then
        assertThat(pizzaOptional).isEmpty();
    }
}
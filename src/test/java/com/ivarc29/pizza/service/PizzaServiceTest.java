package com.ivarc29.pizza.service;

import com.ivarc29.pizza.persistence.entity.PizzaEntity;
import com.ivarc29.pizza.persistence.repository.PizzaPagSortRepository;
import com.ivarc29.pizza.persistence.repository.PizzaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;
    @Mock
    private PizzaPagSortRepository pizzaPagSortRepository;
    
    @InjectMocks
    private PizzaService pizzaService;

    private Pageable pageable;
    private List<PizzaEntity> pizzas;
    private final int PAGE = 0;
    private final int ELEMENTS = 8;

    @BeforeEach
    void setUp() {
        // Config pageable
        pageable = PageRequest.of(PAGE, ELEMENTS);

        // Stack of pizzas
        pizzas = new ArrayList<>();
        pizzas.add(createPizza(1, "Margarita", "Classic margarita pizza", 10.0, true, false, true));
        pizzas.add(createPizza(2, "Pepperoni", "Pepperoni pizza", 12.0, false, false, true));
        pizzas.add(createPizza(3, "Hawaiian", "Hawaiian pizza", 13.0, false, false, true));
        pizzas.add(createPizza(4, "Vegan Holiday", "Vegan pizza", 8.5, true, true, false));
    }

    @DisplayName("Test to list all pizzas")
    @Test
    public void returnListOfPizzaEntityWhenUseGetAll() {

        // Given - precondition
        Page<PizzaEntity> pizzaPage = new PageImpl<>(pizzas, pageable, pizzas.size());

        // When - action or behavior on trial
        when(pizzaPagSortRepository.findAll(pageable)).thenReturn(pizzaPage);
        Page<PizzaEntity> result = pizzaService.getAll(PAGE, ELEMENTS);

        // Then - Check expected output
        Assertions.assertEquals(pizzas.size(), result.getTotalElements(), "Total elements should match");

        pizzas.forEach( pizza -> {
            Assertions.assertTrue(result.getContent().contains( pizza ), "Pizza entity should be present in the result");
        });

    }

    @DisplayName("Test to list all available pizzas")
    @Test
    void returnListOfPizzaEntityWhenUseGetAllAvailable() {
        // Given
        Sort sort = Sort.by(Sort.Direction.fromString("ASC"), "idPizza");
        Pageable sortPageable = PageRequest.of(PAGE, ELEMENTS, sort);
        List<PizzaEntity> availablePizzas = pizzas.stream()
                .filter( PizzaEntity::getAvailable)
                .collect(Collectors.toList());
        Page<PizzaEntity> pizzaPage = new PageImpl<>(availablePizzas, sortPageable, availablePizzas.size());

        // When
        when(pizzaPagSortRepository.findByAvailableTrue(sortPageable)).thenReturn(pizzaPage);
        Page<PizzaEntity> result = pizzaService.getAvailable(PAGE, ELEMENTS, "idPizza", "ASC");
        // Then
        Assertions.assertEquals(availablePizzas.size(), result.getTotalElements(), "Total elements should match");
        availablePizzas.forEach(pizza -> {
            Assertions.assertTrue(result.getContent().contains(pizza), "Pizza entity should be present in the result");
        });

        // Verify order
        List<PizzaEntity> resultContent = result.getContent();
        availablePizzas.sort( (a, b) -> a.getIdPizza().compareTo(b.getIdPizza()) );


        Assertions.assertEquals(availablePizzas, resultContent, "Pizzas should be ordered by name ascending");
    }

    @DisplayName("Test to get a pizza by id")
    @Test
    public void testGetById() {
        // Given
        PizzaEntity pizza = createPizza(1, "Margarita", "Classic margarita pizza", 10.0, true, false, true);
        int id = pizza.getIdPizza();

        // When
        when(pizzaRepository.findById(id)).thenReturn(Optional.of(pizza));

        // call service method
        PizzaEntity result = pizzaService.get(id);

        // Then
        Assertions.assertEquals(pizza, result);
    }

    @DisplayName("Test to get available pizza by name")
    @Test
    public void testGetAvailablePizzaByName() {
        // Given

        // When
        when( pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase("pepperoni") )
                .thenReturn( pizzas.get(1) );
        PizzaEntity result = pizzaService.getAvailableByName("pepperoni");
        // Then
        Assertions.assertEquals(pizzas.get(1), result);
        Assertions.assertEquals(pizzas.get(1).getName().toLowerCase(), result.getName().toLowerCase());

    }

    private static PizzaEntity createPizza(int id, String name, String description, double price, boolean vegetarian, boolean vegan, boolean available) {
        return PizzaEntity.builder()
                .idPizza(id)
                .name(name)
                .description(description)
                .price(price)
                .vegetarian(vegetarian)
                .vegan(vegan)
                .available(available)
                .build();
    }
}
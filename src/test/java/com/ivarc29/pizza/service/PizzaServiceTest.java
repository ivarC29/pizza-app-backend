package com.ivarc29.pizza.service;

import com.ivarc29.pizza.persistence.entity.PizzaEntity;
import com.ivarc29.pizza.persistence.repository.PizzaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;
    
    @InjectMocks
    private PizzaService pizzaService;


    @DisplayName("Test to list all pizzas")
    @Test
    public void returnListOfPizzaEntityWhenUseGetAll() {

        // Given - Condicion previa o configuracion
        List<PizzaEntity> pizzas = new ArrayList<>();
        pizzas.add(createPizza(1, "Margarita", "Classic margarita pizza", 10.0, true, false, true));
        pizzas.add(createPizza(2, "Pepperoni", "Pepperoni pizza", 12.0, false, false, true));
        pizzas.add(createPizza(3, "Hawaiana", "Hawaiian pizza", 13.0, false, true, true));

        // When - Accion o comportamiento a probar
        when(pizzaRepository.findAll()).thenReturn(pizzas);

        List<PizzaEntity> result = pizzaService.getAll();

        // Then - Verificar la salida esperada
        Assertions.assertEquals(pizzas.size(), result.size());

        pizzas.forEach( pizza -> {
            Assertions.assertEquals(pizza, result.get(pizza.getIdPizza() - 1), "Test de findAll completo");
        });

    }

    @DisplayName("Test to get a pizza by id")
    @Test
    public void testGetById() {
        // Mock de datos de prueba
        PizzaEntity pizza = createPizza(1, "Margarita", "Classic margarita pizza", 10.0, true, false, true);
        int id = pizza.getIdPizza();

        // Configurar comportamiento del repositorio mock
        when(pizzaRepository.findById(id)).thenReturn(Optional.of(pizza));

        // Llamar al método del servicio
        PizzaEntity result = pizzaService.get(id);

        // Verificar que el resultado sea el esperado
        Assertions.assertEquals(pizza, result);
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
package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.PizzaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PizzaPagSortRepositoryTest {

    @Mock
    private PizzaPagSortRepository pizzaPagSortRepository;

    @Test
    @DisplayName("Find available pizzas with pagination and sorting")
    public void testFindByAvailableTrue() {
        // Given
        Pageable pageable = Pageable.unpaged(); // Mock pageable object
        Page<PizzaEntity> pizzaEntities = mock(Page.class);
        when(pizzaPagSortRepository.findByAvailableTrue(pageable)).thenReturn(pizzaEntities);

        // When
        Page<PizzaEntity> result = pizzaPagSortRepository.findByAvailableTrue(pageable);

        // Then
        verify(pizzaPagSortRepository, times(1)).findByAvailableTrue(pageable);
        assertNotNull(result, "The result should not be null");
        assertSame(pizzaEntities, result, "The result should be the same as the mocked page");
    }
}
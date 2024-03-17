package com.ivarc29.pizza.web.controller;

import com.ivarc29.pizza.persistence.entity.PizzaEntity;
import com.ivarc29.pizza.service.PizzaService;
import com.ivarc29.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int elements) {
        try {
            return ResponseEntity.ok(this.pizzaService.getAll(page, elements));
        } catch ( Exception e ) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> getAvailable(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int elements,
                                                          @RequestParam(defaultValue = "idPizza") String sortedBy,
                                                          @RequestParam(defaultValue = "ASC") String sortDirection) {
        try {
            return ResponseEntity.ok(this.pizzaService.getAvailable(page,elements, sortedBy, sortDirection));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaEntity> getAvailableByName(@PathVariable("name") String name) {
        try {
            PizzaEntity pizza = this.pizzaService.getAvailableByName(name);
            if (pizza != null)
                return ResponseEntity.ok(pizza);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/with/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getAllThatDescriptionContains(@PathVariable("ingredient") String ingredient) {
        try {
            List<PizzaEntity> pizzas = this.pizzaService.getAllThatDescriptionContains(ingredient);
            if (pizzas.size() > 0)
                return ResponseEntity.ok(pizzas);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/without/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getAllThatDescriptionNotContains(@PathVariable("ingredient") String ingredient) {
        try {
            List<PizzaEntity> pizzas = this.pizzaService.getAllThatDescriptionNotContains(ingredient);
            if (pizzas.size() > 0)
                return ResponseEntity.ok(pizzas);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getTop3ByPrice(@PathVariable("price") Double price) {
        try {
            List<PizzaEntity> pizzas = this.pizzaService.getTop3ByPrice(price);
            if (pizzas.size() > 0)
                return ResponseEntity.ok(pizzas);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> get(@PathVariable("idPizza") int idPizza) {
        try {
            return ResponseEntity.ok(this.pizzaService.get(idPizza));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<PizzaEntity> add(@RequestBody PizzaEntity pizza) {
        try {
            PizzaEntity savedPizza = new PizzaEntity();
            if ( pizza.getIdPizza() == null || !this.pizzaService.exists(pizza.getIdPizza()) ) {
                savedPizza = this.pizzaService.save(pizza);
            }

            if (savedPizza != null) {
                return new ResponseEntity<>(savedPizza, HttpStatus.CREATED);
            }

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza) {
        try {
            if ( pizza.getIdPizza() != null && this.pizzaService.exists(pizza.getIdPizza()) )
                return ResponseEntity.ok(this.pizzaService.save(pizza));

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> updatePartially(@PathVariable("idPizza") int idPizza, @RequestBody PizzaEntity pizza) {
        try {
            PizzaEntity existingPizza = this.pizzaService.get(idPizza);
            if ( existingPizza == null )
                return ResponseEntity.notFound().build();

            BeanUtils.copyProperties(pizza, existingPizza, "idPizza");
            return ResponseEntity.ok(this.pizzaService.save(existingPizza));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> delete(@PathVariable int idPizza) {
        try {
            if ( this.pizzaService.exists(idPizza) ) {
                this.pizzaService.delete(idPizza);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch ( Exception e ) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/price")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto) {
        try {
            if ( this.pizzaService.exists(dto.getPizzaId()) ) {
                this.pizzaService.updatePrice(dto);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}

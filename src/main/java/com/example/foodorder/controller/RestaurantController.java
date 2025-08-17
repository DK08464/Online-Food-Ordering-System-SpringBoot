package com.example.foodorder.controller;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Restaurant;
import com.example.foodorder.repository.MenuItemRepo;
import com.example.foodorder.repository.RestaurantRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepo repo;
    private final MenuItemRepo menuRepo;

    public RestaurantController(RestaurantRepo repo, MenuItemRepo menuRepo){ 
        this.repo=repo; 
        this.menuRepo=menuRepo; 
    }

    // ----------------- PUBLIC ENDPOINTS -----------------

    // Get all restaurants (paginated)
    @GetMapping
    public Page<Restaurant> all(@RequestParam(defaultValue="0") int page, 
                                @RequestParam(defaultValue="20") int size){
        return repo.findAll(PageRequest.of(page,size));
    }

    // Get menu for a specific restaurant
    @GetMapping("/{id}/menu")
    public Page<MenuItem> menu(@PathVariable Long id, 
                               @RequestParam(defaultValue="0") int page, 
                               @RequestParam(defaultValue="50") int size){
        return menuRepo.findByRestaurantIdAndAvailableTrue(id, PageRequest.of(page,size));
    }

    // ----------------- ADMIN ENDPOINTS -----------------

    // Create new restaurant
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Restaurant create(@RequestBody Restaurant r){
        return repo.save(r);
    }

    // Update existing restaurant
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Restaurant> update(@PathVariable Long id, @RequestBody Restaurant r){
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(r.getName());
                    existing.setAddress(r.getAddress());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete restaurant
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return repo.findById(id)
                .map(r -> {
                    repo.delete(r);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Add menu item to a restaurant
    @PostMapping("/admin/{id}/menu")
    @PreAuthorize("hasRole('ADMIN')")
    public MenuItem add(@PathVariable Long id, @RequestBody MenuItem mi){
        Restaurant r = repo.findById(id).orElseThrow();
        mi.setRestaurant(r);
        return menuRepo.save(mi);
    }
}

package org.wheels.controller;

import org.springframework.web.bind.annotation.*;
import org.wheels.model.Bike;
import org.wheels.model.BikeType;
import org.wheels.service.BikeService;

import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    private final BikeService bikeService;
    public BikeController(BikeService bikeService) { this.bikeService = bikeService; }

    @GetMapping
    public List<Bike> getAll() { return bikeService.getAllBikes(); }

    @GetMapping("/available")
    public List<Bike> getAvailable(@RequestParam BikeType type) {
        return bikeService.getAvailableBikesByType(type);
    }

    @GetMapping("/{id}")
    public Bike getById(@PathVariable int id) { return bikeService.getBikeById(id); }

    @PutMapping("/{id}/rent")
    public void rentBike(@PathVariable int id, @RequestParam String userEmail) {
        bikeService.rentBike(id, userEmail);
    }

    @PutMapping("/{id}/return")
    public void returnBike(@PathVariable int id, @RequestParam String userEmail) {
        bikeService.returnBike(id, userEmail);
    }
}
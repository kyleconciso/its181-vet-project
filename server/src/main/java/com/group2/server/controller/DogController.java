package com.group2.server.controller;

import com.group2.server.model.Dog;
import com.group2.server.service.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dogs")
@CrossOrigin(origins = "*")
public class DogController {

    @Autowired
    private DogService dogService;

    @GetMapping
    public ResponseEntity<List<Dog>> getAllDogs() {
        List<Dog> dogs = dogService.getAllDogs();
        return new ResponseEntity<>(dogs, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Dog>> getAvailableDogs() {
        List<Dog> dogs = dogService.getAvailableDogs();
        return new ResponseEntity<>(dogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDogById(@PathVariable Long id) {
        Dog dog = dogService.getDogById(id);
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        Dog createdDog = dogService.createDog(dog);
        return new ResponseEntity<>(createdDog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody Dog dog) {
        Dog updatedDog = dogService.updateDog(id, dog);
        return new ResponseEntity<>(updatedDog, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
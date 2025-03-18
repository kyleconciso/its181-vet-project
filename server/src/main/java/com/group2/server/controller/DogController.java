package com.group2.server.controller;

import com.group2.server.model.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Dog>>> getAllDogs() {
        List<Dog> dogs = dogService.getAllDogs();
        return ResponseEntity.ok(new ApiResponse<>(true, "All dogs retrieved", dogs));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Dog>>> getAvailableDogs() {
        List<Dog> dogs = dogService.getAvailableDogs();
        return ResponseEntity.ok(new ApiResponse<>(true, "Available dogs retrieved", dogs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Dog>> getDogById(@PathVariable Long id) {
        try {
            Dog dog = dogService.getDogById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dog retrieved successfully", dog));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Dog>> createDog(@RequestBody Dog dog) {
        try {
            // Validation
            if (dog.getName() == null || dog.getBreed() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Dog name and breed are required", null));
            }

            Dog createdDog = dogService.createDog(dog);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Dog created successfully", createdDog));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Dog>> updateDog(@PathVariable Long id, @RequestBody Dog dog) {
        try {
            Dog updatedDog = dogService.updateDog(id, dog);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dog updated successfully", updatedDog));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDog(@PathVariable Long id) {
        try {
            dogService.deleteDog(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dog deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
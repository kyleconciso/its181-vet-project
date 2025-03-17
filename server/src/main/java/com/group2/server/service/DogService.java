package com.group2.server.service;

import com.group2.server.model.Dog;
import com.group2.server.repository.DogRepository;
import com.group2.server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DogService {

    @Autowired
    private DogRepository dogRepository;

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    public List<Dog> getAvailableDogs() {
        return dogRepository.findByIsAvailable(true);
    }

    public Dog getDogById(Long id) {
        return dogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + id));
    }

    public Dog createDog(Dog dog) {
        return dogRepository.save(dog);
    }

    public Dog updateDog(Long id, Dog dogDetails) {
        Dog dog = getDogById(id);
        dog.setName(dogDetails.getName());
        dog.setBreed(dogDetails.getBreed());
        dog.setAge(dogDetails.getAge());
        dog.setGender(dogDetails.getGender());
        dog.setDescription(dogDetails.getDescription());
        dog.setImageUrl(dogDetails.getImageUrl());
        dog.setAvailable(dogDetails.isAvailable());
        return dogRepository.save(dog);
    }

    public void deleteDog(Long id) {
        Dog dog = getDogById(id);
        dogRepository.delete(dog);
    }
}
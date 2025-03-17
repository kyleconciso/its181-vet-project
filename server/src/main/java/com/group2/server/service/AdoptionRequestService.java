package com.group2.server.service;

import com.group2.server.model.AdoptionRequest;
import com.group2.server.model.Dog;
import com.group2.server.model.User;
import com.group2.server.repository.AdoptionRequestRepository;
import com.group2.server.repository.DogRepository;
import com.group2.server.repository.UserRepository;

import com.group2.server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdoptionRequestService {

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private UserRepository userRepository;

    public AdoptionRequest createAdoptionRequest(AdoptionRequest request) {
        //validate dogid
        Dog dog = dogRepository.findById(request.getDogId())
                .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + request.getDogId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        request.setRequestDate(LocalDateTime.now());
        request.setStatus("PENDING");
        return adoptionRequestRepository.save(request);
    }

    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptionRequestRepository.findAll();
    }
    public List<AdoptionRequest> getAdoptionRequestsByUserId(Long userId) {
        return adoptionRequestRepository.findByUserId(userId);
    }

    public AdoptionRequest getAdoptionRequestById(Long id) {
        return adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption request not found with id: " + id));
    }

    public AdoptionRequest updateAdoptionRequestStatus(Long id, String status) {
        AdoptionRequest request = getAdoptionRequestById(id);
        request.setStatus(status);
        return adoptionRequestRepository.save(request);
    }
}
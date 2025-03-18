// server/src/main/java/com/group2/server/service/AdoptionRequestService.java
package com.group2.server.service;

import com.group2.server.exception.ResourceNotFoundException;
import com.group2.server.model.AdoptionRequest;
import com.group2.server.model.Dog;
import com.group2.server.model.User;
import com.group2.server.repository.AdoptionRequestRepository;
import com.group2.server.repository.DogRepository;
import com.group2.server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public AdoptionRequest createAdoptionRequest(AdoptionRequest request, Authentication authentication) {
        // Validate dog exists and is available
        Dog dog = dogRepository.findById(request.getDogId())
                .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + request.getDogId()));

        if (!dog.isAvailable()) {
            throw new IllegalStateException("Dog is not available for adoption");
        }

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        // Security check - ensure user is creating their own request or is admin
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        if (!currentUser.getId().equals(user.getId()) && !isAdmin) {
            throw new AccessDeniedException("You can only create adoption requests for yourself");
        }

        // Set default values
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("PENDING");
        request.setUserEmail(user.getEmail());

        return adoptionRequestRepository.save(request);
    }

    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptionRequestRepository.findAll();
    }

    public List<AdoptionRequest> getAdoptionRequestsByUserId(Long userId, Authentication authentication) {
        // Security check - ensure user is accessing their own requests or is admin
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        if (!currentUser.getId().equals(userId) && !isAdmin) {
            throw new AccessDeniedException("You can only access your own adoption requests");
        }

        return adoptionRequestRepository.findByUserId(userId);
    }

    public AdoptionRequest getAdoptionRequestById(Long id, Authentication authentication) {
        AdoptionRequest request = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption request not found with id: " + id));

        // Security check - ensure user is accessing their own request or is admin
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        if (!currentUser.getId().equals(request.getUserId()) && !isAdmin) {
            throw new AccessDeniedException("You can only access your own adoption requests");
        }

        return request;
    }

    public AdoptionRequest updateAdoptionRequestStatus(Long id, String status) {
        AdoptionRequest request = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption request not found with id: " + id));

        // Validate status
        status = status.trim().toUpperCase();
        if (!status.equals("PENDING") && !status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new IllegalArgumentException(String.format("Invalid status. Must be PENDING, APPROVED, or REJECTED, got: %s", status ));
        }

        // If status is changing to APPROVED, mark the dog as unavailable
        if (status.equals("APPROVED") && !request.getStatus().equals("APPROVED")) {
            Dog dog = dogRepository.findById(request.getDogId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + request.getDogId()));

            dog.setAvailable(false);
            dogRepository.save(dog);
        }

        // If status is changing from APPROVED to something else, mark the dog as available again
        if (!status.equals("APPROVED") && request.getStatus().equals("APPROVED")) {
            Dog dog = dogRepository.findById(request.getDogId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + request.getDogId()));

            dog.setAvailable(true);
            dogRepository.save(dog);
        }

        request.setStatus(status);
        return adoptionRequestRepository.save(request);
    }
}
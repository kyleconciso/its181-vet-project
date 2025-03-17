package com.group2.server.controller;

import com.group2.server.model.AdoptionRequest;
import com.group2.server.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class AdoptionRequestController {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @PostMapping
    public ResponseEntity<AdoptionRequest> createAdoptionRequest(@RequestBody AdoptionRequest request) {
        AdoptionRequest createdRequest = adoptionRequestService.createAdoptionRequest(request);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdoptionRequest>> getAllAdoptionRequests() {
        List<AdoptionRequest> requests = adoptionRequestService.getAllAdoptionRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AdoptionRequest>> getAdoptionRequestsByUserId(@PathVariable Long userId) {
        List<AdoptionRequest> requests = adoptionRequestService.getAdoptionRequestsByUserId(userId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionRequest> getAdoptionRequestById(@PathVariable Long id) {
        AdoptionRequest request = adoptionRequestService.getAdoptionRequestById(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<AdoptionRequest> updateAdoptionRequestStatus(@PathVariable Long id, @RequestBody String status) {
        AdoptionRequest updatedRequest = adoptionRequestService.updateAdoptionRequestStatus(id, status);
        return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
    }
}
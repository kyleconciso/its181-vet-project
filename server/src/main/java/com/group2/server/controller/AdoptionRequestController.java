// server/src/main/java/com/group2/server/controller/AdoptionRequestController.java
package com.group2.server.controller;

import com.group2.server.model.AdoptionRequest;
import com.group2.server.model.ApiResponse;
import com.group2.server.model.StatusUpdateRequest; // Import
import com.group2.server.service.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class AdoptionRequestController {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdoptionRequest>> createAdoptionRequest(
            @RequestBody AdoptionRequest request,
            Authentication authentication) {
        try {
            AdoptionRequest createdRequest = adoptionRequestService.createAdoptionRequest(request, authentication);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Adoption request created successfully", createdRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AdoptionRequest>>> getAllAdoptionRequests() {
        List<AdoptionRequest> requests = adoptionRequestService.getAllAdoptionRequests();
        return ResponseEntity.ok(new ApiResponse<>(true, "All adoption requests retrieved", requests));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AdoptionRequest>>> getAdoptionRequestsByUserId(
            @PathVariable Long userId,
            Authentication authentication) {
        try {
            List<AdoptionRequest> requests = adoptionRequestService.getAdoptionRequestsByUserId(userId, authentication);
            return ResponseEntity.ok(new ApiResponse<>(true, "User adoption requests retrieved", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdoptionRequest>> getAdoptionRequestById(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            AdoptionRequest request = adoptionRequestService.getAdoptionRequestById(id, authentication);
            return ResponseEntity.ok(new ApiResponse<>(true, "Adoption request retrieved", request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AdoptionRequest>> updateAdoptionRequestStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest statusUpdate) { // Use StatusUpdateRequest
        try {
            AdoptionRequest updatedRequest = adoptionRequestService.updateAdoptionRequestStatus(id, statusUpdate.getStatus());
            return ResponseEntity.ok(new ApiResponse<>(true, "Adoption request status updated", updatedRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
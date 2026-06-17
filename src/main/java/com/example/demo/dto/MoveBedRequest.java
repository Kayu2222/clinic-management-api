package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for POST /api/beds/{id}/move.
 * Example: { "toBedId": "W1-R2-B3" }
 */
public class MoveBedRequest {

    @NotBlank(message = "toBedId is required")
    private String toBedId;

    public String getToBedId() { return toBedId; }
    public void setToBedId(String toBedId) { this.toBedId = toBedId; }
}

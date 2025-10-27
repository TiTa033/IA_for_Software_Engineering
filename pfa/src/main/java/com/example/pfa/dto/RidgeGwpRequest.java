package com.example.pfa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RidgeGwpRequest {
    private double weight;
    private double length;
    private double width;
    private double height;
    
    @JsonProperty("distance_km")
    private double distanceKm;
    
    private String material;
}

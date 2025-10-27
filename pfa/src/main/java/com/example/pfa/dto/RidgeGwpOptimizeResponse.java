package com.example.pfa.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RidgeGwpOptimizeResponse {
    @JsonAlias({"opti_weight", "optiWeight"})
    private double optiWeight;
    
    @JsonAlias({"opti_GWP", "optiGwp"})
    private double optiGwp;
}

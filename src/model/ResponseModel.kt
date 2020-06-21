package com.example.ktor.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class ResponseModel(val flights: MutableList<FlightModel>, val numberOfGates: Int) {
    var averageUseOfGateInSeconds: Long = 0
    var gateAllocation = mutableMapOf<String , String>()
}

/*
@JsonProperty("gate_allocation")
    private HashMap<String, String> gateAllocation;
 */
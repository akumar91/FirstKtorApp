package com.example.ktor.model

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/** data class will generate equals()/hashCode(), toString() and copy() automatically
 * https://kotlinlang.org/docs/reference/data-classes.html
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class FlightsModel(val id: Int,
                        val numberOfGates: Int,
                        val flights: MutableList<FlightModel>)
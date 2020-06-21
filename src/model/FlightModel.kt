package com.example.ktor.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class FlightModel(val flightNumber: String,
                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                       @JsonSerialize(using = ToStringSerializer::class)
                       @JsonDeserialize(using = LocalDateTimeDeserializer::class)
                       val arrival: LocalDateTime,
                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                       @JsonSerialize(using = ToStringSerializer::class)
                       @JsonDeserialize(using = LocalDateTimeDeserializer::class)
                       val departure: LocalDateTime)
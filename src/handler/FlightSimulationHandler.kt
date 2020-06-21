package com.example.ktor.handler

import com.example.ktor.model.FlightsModel
import com.example.ktor.service.gateAssignmentService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Routing.flightGateAllocationHandler() {
    route("/flights") {
        get("/health") {
            call.healthCheck()
        }
        get("/gateAssignment"){
            call.gateAssignment()
        }
        post("/gateAssignment") {
            call.saveFlightSchedule()
        }
    }
}

private suspend fun ApplicationCall.healthCheck() {
    respond(mapOf("Status" to "UP"))
}

private suspend fun ApplicationCall.gateAssignment() {
    val flightSchedule = request.queryParameters["flightSchedule"]

    // flightSchedule?. => check if the flightSchedule is not null then use
    // jackson to convert to FlightsModel
    val flightsModel = flightSchedule?.let { jacksonObjectMapper().readValue<FlightsModel>(it) }
        // ?: Elvis operator =? In case flightSchedule?. returns null return 404 error
        ?: respond(HttpStatusCode.BadRequest)

    gateAssignmentService.test()
    val response = gateAssignmentService.allocateGateForFlights(flightsModel as FlightsModel)

    respond(HttpStatusCode.OK, response)
}

private suspend fun ApplicationCall.saveFlightSchedule() {
    val requestBody = receive<FlightsModel>()
    respond(HttpStatusCode.NoContent)
}
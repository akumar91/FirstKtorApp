package com.example.ktor.service

import com.example.ktor.model.FlightsModel
import com.example.ktor.model.ResponseModel
import java.text.SimpleDateFormat
import java.util.LinkedList
import java.util.Queue
import java.util.TimeZone
import kotlin.collections.HashMap

val gateAssignmentService by lazy {
    GateAssignmentService()
}

class GateAssignmentService {

    suspend fun test(){
        println("Hello")
    }

    suspend fun allocateGateForFlights(flightsModel: FlightsModel): ResponseModel {
        val flightModels = flightsModel.flights
        val numberOfGates: Int = flightsModel.numberOfGates
        val responseModel = ResponseModel(flightModels, numberOfGates)

        val arrivalWithFlight = HashMap<String, Long>()
        val departureWithFlight = HashMap<String, Long>()
        if (flightModels != null && flightModels.size > 0) {
            val gateAllocation = mutableMapOf<String , String>()
            val queueofFlightsWithEarlyArrival: Queue<String> = LinkedList()
            val queueofFlightsWithEarlyDeparture: Queue<String> = LinkedList()
            for (flightModel in flightModels) {
                var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                sdf.timeZone = TimeZone.getTimeZone("IST")
                var dt = sdf.parse(flightModel.arrival.toString())
                val arrivalforFlight = dt.time
                arrivalWithFlight[flightModel.flightNumber] = arrivalforFlight
                sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                sdf.timeZone = TimeZone.getTimeZone("IST")
                dt = sdf.parse(flightModel.departure.toString())
                val departureforFlight = dt.time
                departureWithFlight[flightModel.flightNumber] = departureforFlight
            }

            // sort arrival and departure flights by their arrival and departure time in ascending order
            val sortedArrivalWithFlight = arrivalWithFlight.toList().sortedBy { (_, value) -> value}.toMap()
            val sortedDepartureWithFlight = departureWithFlight.toList().sortedBy { (_, value) -> value}.toMap()

            // add coroutine here to add flight with arrival time in ascending order to a queue
            for ((key, value) in sortedArrivalWithFlight) {
                println("Arrival fligt Number is: $key")
                println("Time of arrival is: $value")
                queueofFlightsWithEarlyArrival.add(key)
            }
            // add coroutine here to add flight with departure time in ascending order to a queue
            for ((key, value) in sortedDepartureWithFlight) {
                println("Departure fligt Number is: $key")
                println("Time of departure is: $value")
                queueofFlightsWithEarlyDeparture.add(key)
            }
            for (i in 0 until numberOfGates) {
                if (queueofFlightsWithEarlyArrival.size > 0) {
                    //allocate gates to the flights for the available free gates
                    gateAllocation[queueofFlightsWithEarlyArrival.peek()] = i.toString()
                    queueofFlightsWithEarlyArrival.poll()
                }
            }
            if (queueofFlightsWithEarlyArrival.size > 0) {
                while (queueofFlightsWithEarlyArrival.size > 0) {
                    var flightWithEarlyDeparture: String = queueofFlightsWithEarlyDeparture.peek()
                    var flightGateNumber = gateAllocation[flightWithEarlyDeparture]
                    if (flightGateNumber != null) {
                        gateAllocation[queueofFlightsWithEarlyArrival.peek()] = flightGateNumber
                    }
                    val flightSavetoTail = queueofFlightsWithEarlyDeparture.peek()
                    queueofFlightsWithEarlyDeparture.add(flightSavetoTail)
                    queueofFlightsWithEarlyDeparture.poll()
                    queueofFlightsWithEarlyArrival.poll()
                }
            }
            responseModel.gateAllocation = gateAllocation
        }

        //add coroutine here to get average time
        var averageWaitTime: Long = 0
        var numberOfFlights = 0
        for ((key, value) in arrivalWithFlight) {
            val buffer = departureWithFlight[key]!! - value
            averageWaitTime += buffer
            numberOfFlights++
        }
        averageWaitTime /= numberOfFlights
        averageWaitTime /= 1000

        responseModel.averageUseOfGateInSeconds = averageWaitTime

        return responseModel
    }
}
package com.example.ktor

import com.example.ktor.handler.flightGateAllocationHandler
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.routing.Routing
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait=true)
}

/** A Ktor module is just a user-defined function receiving the Application class that is in charge of configuring
 * the server pipeline, install features, registering routes, handling requests, etc.
 * A Ktor application typically consists of a series of features. You can think of features as functionality
 * that is injected into the request and response pipeline.
 */
fun Application.module() {

    // A feature is installed into the Application by calling the install function:

    // All the API request will be routed to the below mentioned handler classes which will then based on the matching
    // HTTP verb and URI will respond back
    install(Routing) {
        flightGateAllocationHandler()
    }

    // All the request will be converted to json using jackson converter
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}

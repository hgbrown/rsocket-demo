package dev.hbrown.rsocketserver

import dev.hbrown.rsocketserver.data.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant


@Controller
class RSocketController {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * This @MessageMapping is intended to be used "request --> response" style.
     * For each Message received, a new Message is returned with ORIGIN=Server and INTERACTION=Request-Response.
     * @param request
     * @return Message
     */
    @MessageMapping("request-response")
    fun requestResponse(request: Message): Message {
        log.info("Received request-response request: {}", request)
        // create a single Message and return it
        return Message(SERVER, RESPONSE)
    }

    companion object {
        private val SERVER = "Server"
        private val RESPONSE = "Response"
        private val STREAM = "Stream"
        private val CHANNEL = "Channel"
    }

    /**
     * This @MessageMapping is intended to be used "fire --> forget" style.
     * When a new CommandRequest is received, nothing is returned (void)
     * @param request
     * @return
     */
    @MessageMapping("fire-and-forget")
    fun fireAndForget(request: Message) {
        log.info("Received fire-and-forget request: {}", request)
    }

    /**
     * This @MessageMapping is intended to be used "subscribe --> stream" style.
     * When a new request command is received, a new stream of events is started and returned to the client.
     * @param request
     * @return
     */
    @MessageMapping("stream")
    fun stream(request: Message): Flux<Message> {
        log.info("Received stream request: {}", request)
        return Flux
            // create a new Flux emitting an element every 1 second
            .interval(Duration.ofSeconds(1))
            // index the Flux
            .index()
            // create a Flux of new Messages using the indexed Flux
            .map { objects -> Message(SERVER, STREAM, objects.t1) }
            // use the Flux logger to output each flux event
            .log()
    }

    fun channel(requests: Flux<Message>): Flux<Message> {
        log.info("Received channel request (stream) at {}", Instant.now())

        return requests
            // Create an indexed flux which gives each element a number
            .index()
            // log what has been received
            .log()
            // then every 1 second per element received
            .delayElements(Duration.ofSeconds(1))
            // create a new Flux with one Message for each element (numbered)
            .map { objects -> Message(SERVER, CHANNEL, objects.t1) }
            // log what is being sent
            .log()
    }

}
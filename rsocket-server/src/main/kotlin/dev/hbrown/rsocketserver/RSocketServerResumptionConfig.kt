package dev.hbrown.rsocketserver

import io.rsocket.RSocketFactory.ServerRSocketFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.rsocket.server.ServerRSocketFactoryProcessor
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component


@Profile("resumption")
@Component
class RSocketServerResumptionConfig : ServerRSocketFactoryProcessor {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * In this method we can configure the ServerRSocketFactory. In this case, we are
     * switching on the 'resumption' feature with 'resume()'. By default, the Resume Session will have a
     * duration of 120s, a timeout of 10s, and use the In Memory (volatile, non-persistent) session store.
     * @param factory
     * @return
     */
    override fun process(factory: ServerRSocketFactory): ServerRSocketFactory {
        log.info("Adding RSocket Server 'Resumption' Feature.")
        return factory.resume() // By default duration=120s and store=InMemory and timeout=10s
    }
}

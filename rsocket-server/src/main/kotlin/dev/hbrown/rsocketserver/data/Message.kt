package dev.hbrown.rsocketserver.data

import java.time.Instant

data class Message(
    var origin: String,
    var interaction: String,
    var index: Long = 0,
    var created: Long = Instant.now().epochSecond
)

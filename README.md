# Channels

## Channels
```kt
    runBlocking {
        val channel = Channel<Int>()
        val channelClose = Channel<Int>()
        launch {
            for (x in 1..5) {
//                channel.send(x * x)
                channelClose.send(x * x)
            }
            channelClose.close()
        }

//        for (i in 1..5)
//            println(channel.receive())
        /*
        1
        4
        9
        16
        25
         */
        for (i in channelClose)
            println(i)
        /*
        1
        4
        9
        16
        25
         */
    }
 ```
 
 ## ChannelProducer
 ```kt
 fun main() {
    runBlocking {
//        val channel = produce {
//            for(x in 1..5)
//                send(x * x)
//        }
//        for (y in channel)
//            println(y)
        produceSquares().consumeEach { println(it) }
        /*
        1
        4
        9
        16
        25
         */
    }
}

fun CoroutineScope.produceSquares() = produce {
    for (x in 1..5)
        send(x * x)
}
```

## Pipelines
```kt
fun main() {
    runBlocking {
//        val channel = produce {
//            for(x in 1..5)
//                send(x * x)
//        }
//        for (y in channel)
//            println(y)
        produceSquares().consumeEach { println(it) }
        /*
        1
        4
        9
        16
        25
         */
    }
}

fun CoroutineScope.produceSquares() = produce {
    for (x in 1..5)
        send(x * x)
}
```

## FanOut

```kt
fun main() {
    runBlocking {
        val producer = produceNewNumbers()
        repeat(5) { launchProcessor(it, producer)}
        delay(1500L)
        producer.cancel()
        /*
        Processor 0 received 1
        Processor 0 received 2
        Processor 1 received 3
        Processor 2 received 4
        Processor 3 received 5
        Processor 4 received 6
        Processor 0 received 7
        Processor 1 received 8
        Processor 2 received 9
        Processor 3 received 10
        Processor 4 received 11
        Processor 0 received 12
        Processor 1 received 13
        Processor 2 received 14
        Processor 3 received 15
         */
    }
}

fun CoroutineScope.produceNewNumbers() = produce {
    var x = 1
    while (true) {
        send(x++)
        delay(100L)
    }
}

fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) =
    launch {
        for (message in channel)
            println("Processor $id received $message")
    }
```

## FanIn
```kt
fun main() {
    runBlocking {
        val channel = Channel<String>()
        launch { sendString(channel, 200L, "message1") }
        launch { sendString(channel, 500L, "message2") }
        repeat(6) {
            println(channel.receive())
        }
        coroutineContext.cancelChildren()
        /*
        message1
        message1
        message2
        message1
        message1
        message2
         */
    }
}

suspend fun sendString(channel: SendChannel<String>, time: Long, message: String) {
    while (true) {
        delay(time)
        channel.send(message)
    }
}
```

## BufferedChannels

```kt
fun main() {
    runBlocking {
        val channel = Channel<Int>(4)
        val sender = launch {
            repeat(10) {
                channel.send(it)
                println("Sent $it")
            }
        }

        repeat(3) {
            delay(1000)
            println("Received ${channel.receive()}")
            println("Received ${channel.receive()}")
        }
        sender.cancel()
        /*
        Sent 0
        Sent 1
        Sent 2
        Sent 3
        Received 0
        Received 1
        Sent 4
        Sent 5
        Received 2
        Received 3
        Sent 6
        Sent 7
        Received 4
        Received 5
         */
    }
}
```

## TickerChannels.kt
```kt
fun main() {
    runBlocking {
        val tickerChannel = ticker(100)
        launch {
            val startTime = System.currentTimeMillis()
            tickerChannel.consumeEach {
                val delta = System.currentTimeMillis() - startTime
                println("received tick after $delta")
            }
        }

        delay(1000)
        println("Done!")
        tickerChannel.cancel()
        /*
        received tick after 90
        received tick after 177
        received tick after 276
        received tick after 377
        received tick after 477
        received tick after 578
        received tick after 676
        received tick after 776
        received tick after 877
        received tick after 977
        Done!
         */
    }
}
```

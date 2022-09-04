import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
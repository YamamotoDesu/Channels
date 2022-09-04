import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
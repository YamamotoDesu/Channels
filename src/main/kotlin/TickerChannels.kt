import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
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
}
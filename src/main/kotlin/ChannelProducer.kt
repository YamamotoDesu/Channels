import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking

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
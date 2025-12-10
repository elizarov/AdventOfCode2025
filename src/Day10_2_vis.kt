import kotlin.time.DurationUnit
import kotlin.time.TimeSource

private const val INF = Int.MAX_VALUE / 2

fun main() {
    val input = readInput("Day10")
    for ((lineIndex, s) in input.withIndex()) {
        val goal = s.substringAfter('{').dropLast(1).split(",").map { it.toInt() }.toIntArray()
        val wiring = s.substringAfter(']').substringBefore('{').trim().split(" ").map { w ->
            w.substring(1, w.length - 1).split(",").sumOf { 1 shl it.toInt() }
        }.toIntArray()
        // reindex from highest bit to lowest
        var done = 0
        for (j in goal.size - 1 downTo 0) {
            val wcnt = IntArray(goal.size)
            for (i in done..<wiring.size) {
                val w = wiring[i]
                for (k in 0..j) if (w shr k and 1 != 0) wcnt[k]++
            }
            val j1 = (0..j)
                .sortedBy { goal[it] }
                .minBy { wcnt[it] }
            goal[j] = goal[j1].also { goal[j1] = goal[j] }
            for (i in wiring.indices) {
                val w = wiring[i]
                val bj = w shr j and 1
                val bj1 = w shr j1 and 1
                wiring[i] = w and (1 shl j).inv() and (1 shl j1).inv() or (bj shl j1) or (bj1 shl j)
            }
            wiring.sortDescending(done, wiring.size)
            while (done < wiring.size && wiring[done] shr j and 1 != 0) done++
        }
        // display
        println("=== ${lineIndex + 1}:")
        for (i in wiring.indices) {
            val str = goal.indices.joinToString("") { k ->
                when (wiring[i] shr k and 1) {
                    0 -> ".   "
                    1 -> "#   "
                    else -> error("!")
                }
            }
            println(str)
        }
        for (i in goal.indices) {
            print(goal[i].toString().padEnd(4, ' '))
        }
        println()
        println()
    }
}
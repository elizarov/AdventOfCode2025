import kotlin.time.DurationUnit
import kotlin.time.TimeSource

private const val INF = Int.MAX_VALUE / 2

fun main() {
    val input = readInput("Day10")
    var ans = 0
    for ((lineIndex, s) in input.withIndex()) {
        val start = TimeSource.Monotonic.markNow()
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
        // solve
        val n = goal.size
        val bc = IntArray(wiring.size + 1)
        for (i in wiring.indices) bc[i] = 32 - wiring[i].countLeadingZeroBits()
        val ff = IntArray(wiring.size + 1)
        for (i in wiring.size - 1 downTo 0) ff[i] = wiring[i] or ff[i + 1]
        val a = IntArray(n)
        fun find(i: Int, j0: Int): Int {
            val bci = bc[i]
            for (k in bci..<j0) if (a[k] < goal[k]) return INF
            val f = ff[i]
            for (k in 0..<bci) if ((f shr k) and 1 == 0 && a[k] < goal[k]) return INF
            if (i >= wiring.size) return 0
            var best = INF
            var cnt = 0
            if (bc[i + 1] < bci) {
                // optimize for the last one
                cnt = goal[bci - 1] - a[bci - 1]
                var ok = true
                for (k in 0..<bci) if ((wiring[i] shr k) and 1 != 0) {
                    a[k] += cnt
                    if (a[k] > goal[k]) ok = false
                }
                if (ok) {
                    best = minOf(best, find(i + 1, bci) + cnt)
                }
            } else {
                // enumerate all the choices
                do {
                    best = minOf(best, find(i + 1, bci) + cnt)
                    cnt++
                    var ok = true
                    for (k in 0..<bci) if ((wiring[i] shr k) and 1 != 0) {
                        a[k]++
                        if (a[k] > goal[k]) ok = false
                    }
                } while (ok)
            }
            for (k in 0..<bc[i]) if ((wiring[i] shr k) and 1 != 0) {
                a[k] -= cnt
            }
            return best
        }
        val res = find(0, n)
        println("${lineIndex + 1}: $res in ${start.elapsedNow().toString(DurationUnit.SECONDS, 3)}")
        check(res < INF)
        ans += res
    }
    println(ans)
}
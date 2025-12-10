private const val INF = Int.MAX_VALUE / 2

fun main() {
    val input = readInput("Day10")
    var ans = 0
    for (s in input) {
        val goal = s.substringBefore(']').drop(1).mapIndexed { i, c ->
            when (c) {
                '.' -> 0
                '#' -> 1
                else -> error("$c")
            } shl i
        }.sum()
        val wiring = s.substringAfter(']').substringBefore('{').trim().split(" ").map { w ->
            w.substring(1, w.length - 1).split(",").sumOf { 1 shl it.toInt() }
        }.toIntArray()
        fun find(i: Int, mask: Int): Int {
            if (i >= wiring.size) {
                return if (mask == goal) 0 else INF
            }
            val best = minOf(
                find(i + 1, mask),
                find(i + 1, mask xor wiring[i]) + 1
            )
            return best
        }
        val res = find(0, 0)
        check(res < INF)
        ans += res
    }
    println(ans)
}
fun main() {
    val input = readInput("Day11")
    val g = HashMap<String, HashSet<String>>()
    for (s in input) {
        val (u, vs) = s.split(": ")
        for (v in vs.split(" "))
            g.getOrPut(u) { HashSet() }.add(v)
    }
    val queue = ArrayDeque<String>()
    val cnt = HashMap<String, LongArray>()
    queue.add("svr")
    cnt["svr"] = LongArray(4) { if (it == 0) 1 else 0 }
    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        if (u == "out") continue
        val d = cnt.remove(u) ?: continue
        for (v in (g[u] ?: continue)) {
            if (v !in cnt) queue.add(v)
            val vd = cnt.getOrPut(v) { LongArray(4) }
            val bit = when (v) {
                "fft" -> 1
                "dac" -> 2
                else -> 0
            }
            for (k in 0..3) vd[k or bit] += d[k]
        }
    }
    println(cnt["out"]!![3])
}
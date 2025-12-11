fun main() {
    val input = readInput("Day11")
    val g = HashMap<String, HashSet<String>>()
    for (s in input) {
        val (u, vs) = s.split(": ")
        for (v in vs.split(" "))
            g.getOrPut(u) { HashSet() }.add(v)
    }
    val queue = ArrayDeque<String>()
    val cnt = HashMap<String, Long>()
    queue.add("you")
    cnt["you"] = 1
    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        if (u == "out") continue
        val d = cnt.remove(u) ?: continue
        for (v in (g[u] ?: continue)) {
            val prev = cnt.getOrDefault(v, 0)
            cnt[v] = prev + d
            if (prev == 0L) queue.add(v)
        }
    }
    println(cnt["out"])
}
import java.util.TreeMap

fun main() {
    val (rs, a) = readInput("Day05").parts { it }
    data class Evt(val x: Long, val d: Int) : Comparable<Evt> {
        override fun compareTo(other: Evt): Int {
            val dx = x.compareTo(other.x)
            if (dx != 0) return dx
            return -d.compareTo(other.d)
        }
    }
    val es = ArrayList<Evt>()
    for (r in rs) {
        val (a, b) = r.split("-").map { it.toLong() }
        es += Evt(a, 1)
        es += Evt(b, -1)
    }
    es.sort()
    var dd = 0
    val rr = TreeMap<Long, Evt>()
    for (e in es) {
        when (e.d) {
            1 -> if (dd == 0) rr[e.x] = e
            -1 -> if (dd == 1) rr[e.x + 1] = e
        }
        dd += e.d
    }
    var cnt = 0
    for (x in a.map { it.toLong() }) {
        val key = rr.floorKey(x)
        if (key != null && rr[key]!!.d == 1) {
            cnt++
        }
    }
    println(cnt)
}
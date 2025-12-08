fun main() {
    val input = readInput("Day08")
    val a = input.map { it.split(",").map { it.toInt() }.toIntArray() }
    val n = a.size
    val g = List(n) { ArrayList<Int>() }

    data class Dist(val i: Int, val j: Int, val d: Long)

    val d = ArrayList<Dist>()
    for (i in a.indices) for (j in i + 1..<n) {
        var sum = 0L
        for (k in 0..2) {
            sum += (a[i][k] - a[j][k]).toLong() * (a[i][k] - a[j][k])
        }
        d += Dist(i, j, sum)
    }
    d.sortBy { it.d }
    // DSU
    val dUp = IntArray(n) { it }
    val dSz = IntArray(n) { 1 }
    var num = n
    for ((i0, j0) in d) {
        var ci = i0
        while (dUp[ci] != ci) ci = dUp[ci]
        var cj = j0
        while (dUp[cj] != cj) cj = dUp[cj]
        if (ci == cj) continue
        if (dSz[ci] < dSz[cj]) {
            dUp[ci] = cj
            dSz[cj] += dSz[ci]
        } else {
            dUp[cj] = ci
            dSz[ci] += dSz[cj]
        }
        g[i0] += j0
        g[j0] += i0
        num--
        if (num == 1) {
            val ans = a[i0][0].toLong() * a[j0][0]
            println(ans)
            return
        }
    }
}
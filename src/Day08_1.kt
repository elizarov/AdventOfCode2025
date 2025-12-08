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
    for ((i0, j0) in d.take(1000)) {
        g[i0] += j0
        g[j0] += i0
    }
    // Connected components
    val cIdx = IntArray(n)
    val cSize = IntArray(n + 1)
    var cCnt = 0
    val q = IntArray(n)
    var qh: Int
    var qt: Int
    for (i0 in 0..<n) if (cIdx[i0] == 0) {
        cCnt++
        cIdx[i0] = cCnt
        cSize[cCnt] = 1
        q[0] = i0
        qh = 0
        qt = 1
        while (qh < qt) {
            val i = q[qh++]
            for (j in g[i]) if (cIdx[j] == 0) {
                cIdx[j] = cCnt
                cSize[cCnt]++
                q[qt++] = j
            }
        }
    }
    val ans = cSize.sortedDescending().take(3).fold(1L) { a, b -> a * b }
    println(ans)
}
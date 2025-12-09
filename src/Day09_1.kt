import kotlin.math.abs

fun main() {
    val p = readInput("Day09").map { s ->
        val (i, j) = s.split(",").map { it.toInt()}
        P2(i, j)
    }
    val n = p.size
    var ans = 0L
    for (i in 0..<n) for (j in i + 1..<n) {
        val a = (abs(p[i].i - p[j].i) + 1L) * (abs(p[i].j - p[j].j) + 1L)
        ans = maxOf(ans, a)
    }
    println(ans)
}
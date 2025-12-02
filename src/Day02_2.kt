fun main() {
    val input = readInput("Day02")
    var sum = 0L
    for (range in input[0].split(",")) {
        var (sa, sb) = range.split("-")
        while (sa.length < sb.length) {
           sum += day2BRangeEqLen(sa, "9".repeat(sa.length))
           sa = "1" + "0".repeat(sa.length)
        }
        sum += day2BRangeEqLen(sa, sb)
    }
    println(sum)
}

fun day2BRangeEqLen(sa: String, sb: String): Long {
    val a = sa.toLong()
    val b = sb.toLong()
    val len = sa.length
    val set = HashSet<Long>()
    for (k in 2..len) {
        if (len % k != 0) continue
        val n = len / k
        var np = 1L
        repeat(n) { np *= 10L }
        var cp = 1L
        var mulF = 0L
        repeat(k) {
            mulF += cp
            cp *= np
        }
        val ah = sa.take(n).toLong()
        val bh = sb.take(n).toLong()
        for (h in ah..bh) {
            val x = h * mulF
            if (x in a..b) set += x
        }
    }
    return set.sum()
}
fun main() {
    val input = readInput("Day02")
    var sum = 0L
    for (range in input[0].split(",")) {
        var (sa, sb) = range.split("-")
        while (sa.length < sb.length) {
           sum += day2ARangeEqLen(sa, "9".repeat(sa.length))
           sa = "1" + "0".repeat(sa.length)
        }
        sum += day2ARangeEqLen(sa, sb)
    }
    println(sum)
}

fun day2ARangeEqLen(sa: String, sb: String): Long {
    if (sa.length % 2 != 0) return 0
    val n = sa.length / 2
    var pow = 1L
    repeat(n) { pow *= 10L }
    val ah = sa.take(n).toLong()
    val al = sa.substring(n).toLong()
    val bh = sb.take(n).toLong()
    val bl = sb.substring(n).toLong()
    // ah al
    // bh bl
    val cnt = (bh - ah + 1)
    var sum = (ah + bh) * cnt / 2 * (pow + 1)
    if (al > ah) sum -= ah * (pow + 1)
    if (bl < bh) sum -= bh * (pow + 1)
    return sum
}
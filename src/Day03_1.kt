fun main() {
    val input = readInput("Day03")
    var sum = 0L
    for (s in input) {
        val (i, a) = s.dropLast(1).toCharArray().withIndex().maxBy { it.value }
        val b = s.substring(i + 1).max()
        sum += a.digitToInt() * 10 + b.digitToInt()
    }
    println(sum)
}
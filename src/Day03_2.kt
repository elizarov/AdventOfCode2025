fun main() {
    val input = readInput("Day03")
    var sum = 0L
    val k = 12
    for (s in input) {
        var prev = -1
        val str = buildString {
            for (j in 1..k) {
                val (i, a) = s.substring(prev + 1).dropLast(k - j).toCharArray().withIndex().maxBy { it.value }
                append(a)
                prev += i + 1
            }

        }
        sum += str.toLong()
    }
    println(sum)
}
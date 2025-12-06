fun main() {
    val input = readInput("Day06")
    val cs = ArrayList<Int>()
    cs += -1
    val maxLen = input.maxOf { it.length }
    for (j in 0..<maxLen) {
        if (input.indices.map { i -> input[i].getOrElse(j) { ' ' } }.all { it == ' ' }) {
            cs += j
        }
    }
    cs += maxLen
    val a = Array(input.size) { i ->
        Array(cs.size - 1) { j ->
            input[i].substring(cs[j] + 1, cs[j + 1].coerceAtMost(input[i].length)).padEnd(cs[j + 1] - cs[j] - 1, ' ')
        }
    }
    var sum = 0L
    for ((j, lastStr) in a.last().withIndex()) {
        val op = lastStr.trim()
        val nums = lastStr.indices.map { k ->
            (0..a.size - 2).joinToString("") { i -> a[i][j][k].toString() }.trim().toLong()
        }
        sum += when (op) {
            "*" -> nums.fold(1L) { a, b -> a * b }
            "+" -> nums.fold(0L) { a, b -> a + b }
            else -> error(op)
        }
    }
    println(sum)
}
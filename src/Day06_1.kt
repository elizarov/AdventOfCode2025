fun main() {
    val input = readInput("Day06")
    val a = Array(input.size) { input[it].trim().split("\\s+".toRegex()) }
    var sum = 0L
    for ((j, op) in a.last().withIndex()) {
        val nums = (0..input.size - 2).map { i -> a[i][j].toLong() }
        sum += when (op) {
            "*" -> nums.fold(1L) { a, b -> a * b }
            "+" -> nums.fold(0L) { a, b -> a + b }
            else -> error(op)
        }
    }
    println(sum)
}
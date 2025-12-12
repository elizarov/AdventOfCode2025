fun main() {
    val inputParts = readInput("Day12").parts { it }
    data class Shape(val c: Int)
    val sh = inputParts.dropLast(1).mapIndexed { index, lines ->
        check(lines[0] == "$index:")
        val a = lines.drop(1).toCharArray2()
        check(a.size2() == P2(3, 3))
        Shape(a.sumOf { it.count { c -> c == '#' } })
    }
    var ans = 0
    for (test in inputParts.last()) {
        val (m, n) = test.substringBefore(':').split('x').map { it.toInt() }
        val sc = test.substringAfter(": ").split(' ').map { it.toInt() }
        val total = sc.withIndex().sumOf { (i, c) -> sh[i].c * c }
        println("${m}x${n}: $total of ${m * n}, slack = ${m * n - total}")
        if (total <= m * n) ans++
    }
    println(ans)
}
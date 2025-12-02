fun main() {
    val input = readInput("Day01")
    var cnt = 0
    var pos = 50
    for (s in input) {
        val d = s.substring(1).toInt()
        when (s[0]) {
            'R' -> pos = (pos + d).mod(100)
            'L' -> pos = (pos - d).mod(100)
        }
        if (pos == 0) cnt++
    }
    println(cnt)
}
fun main() {
    val input = readInput("Day01")
    var cnt = 0
    var pos = 50
    for (s in input) {
        val d = s.substring(1).toInt()
        when (s[0]) {
            'R' -> {
                pos += d
                cnt += pos / 100
                pos = pos.mod(100)
            }
            'L' -> {
                if (pos == 0) cnt--
                pos -= d
                cnt += (-pos + 100) / 100
                pos = pos.mod(100)
            }
        }
    }
    println(cnt)
}
fun main() {
    val a = readInput("Day07").toCharArray2()
    var c0 = mapOf(a[0].indexOf('S') to 1L)
    for (i in 1..<a.size) {
        val c1 = HashMap<Int, Long>()
        for ((x, c) in c0) {
            if (a[i][x] == '^') {
                c1[x - 1] = c1.getOrDefault(x - 1, 0) + c
                c1[x + 1] = c1.getOrDefault(x + 1, 0) + c
            } else {
                c1[x] = c1.getOrDefault(x, 0) + c
            }
        }
        c0 = c1
    }
    println(c0.values.sum())
}
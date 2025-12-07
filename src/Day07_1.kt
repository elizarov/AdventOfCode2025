fun main() {
    val a = readInput("Day07").toCharArray2()
    var c0 = setOf(a[0].indexOf('S'))
    var cnt = 0
    for (i in 1..<a.size) {
        val c1 = HashSet<Int>()
        for (x in c0) {
            if (a[i][x] == '^') {
                cnt++
                c1 += (x - 1)
                c1 += (x + 1)
            } else {
                c1 += x
            }
        }
        c0 = c1
    }
    println(cnt)
}
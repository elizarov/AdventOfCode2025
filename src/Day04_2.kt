fun main() {
    val a = readInput("Day04").toCharArray2()
    val (m, n) = a.size2()
    var ans = 0
    do {
        var changes = false
        for (i in 0..<m) for (j in 0..<n) if (a[i][j] == '@'){
            var adj = 0
            for (di in -1..1) for (dj in -1..1) {
                val i2 = i + di
                val j2 = j + dj
                if (i2 in 0..<m && j2 in 0..<n && a[i2][j2] == '@') adj++
            }
            if (adj <= 4) {
                ans++
                changes = true
                a[i][j] = '.'
            }
        }
    } while (changes)
    println(ans)
}
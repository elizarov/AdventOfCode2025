import kotlin.math.abs
import kotlin.math.sign
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

private const val INF = Int.MAX_VALUE / 2

private tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

// Fast solution for Day 10 Part with Gaussian elimination + brute force of the remaining variables
fun main() {
    val start0 = TimeSource.Monotonic.markNow()
    val input = readInput("Day10")
    var ans = 0L
    for ((lineIndex, s) in input.withIndex()) {
        val start = TimeSource.Monotonic.markNow()
        val goal = s.substringAfter('{').dropLast(1).split(",").map { it.toLong() }.toLongArray()
        val n = goal.size // constraints
        val w = s.substringAfter(']').substringBefore('{').trim().split(" ").map { w ->
            val index = w.substring(1, w.length - 1).split(",").map { it.toInt() }
            val wi = LongArray(n)
            for (j in index) wi[j] = 1
            wi
        }.toTypedArray()
        val m = w.size // variables
        val gMax = goal.max()
        val d = Array(m) { LongArray(m) }
        for (k in 0..<m) d[k][k] = 1
        val bound = LongArray(m)
        val bDiv = LongArray(m) { 1L }
        val r = LongArray(m) { 1L }
        var resDiv = 1L
        var free = 0L
        //  ( sum_j r[j] * a[j] + free ) / resDiv --> min & integer (sum over j in 0..<m)
        // where:
        //    sum_j w[j][i] * a[j] == goal[i]   for all i in 0..<n (sum over j in 0..<m)
        //    sum_j d[j][k] * a[j] >= bound[k]  for all k in 0..<m (sum over j in 0..<m)
        //    sum_j d[j][k] * a[j]  - bound[k] -- divides by bDiv[k]
        var nr = n // n constraints remaining
        var mr = m // m variables remaining
        while (mr > 0 && nr > 0) {
            val ie = nr - 1 // eliminate constraint for goal
            val je = mr - 1 // eliminate variable
            var i1 = -1
            var j1 = -1
            find@ for (i in 0..<nr) for (j in 0..<mr) if (w[j][i] != 0L) {
                i1 = i
                j1 = j
                break@find
            }
            if (i1 < 0) {
                // no variables left to eliminate
                // check that all remaining constraints are trivial (0 = 0)
                for (i in 0..<nr) {
                    for (j in 0..<m) check(w[j][i] == 0L)
                    check(goal[i] == 0L)
                }
                nr = 0
                break
            }
            // swap conditions ie <> i1, variables je <> j1
            d[je] = d[j1].also { d[j1] = d[je] }
            r[je] = r[j1].also { r[j1] = r[je] }
            w[je] = w[j1].also { w[j1] = w[je] }
            for (j in 0..<mr) w[j][ie] = w[j][i1].also { w[j][i1] = w[j][ie] }
            check(w[je][ie] != 0L)
            goal[ie] = goal[i1].also { goal[i1] = goal[ie] }
            // eliminate variable je from d * a >= bound
            for (k in 0..<m) {
                if (d[je][k] == 0L) continue // is already zero
                val g = gcd(abs(d[je][k]), abs(w[je][ie])) * w[je][ie].sign
                val dMul = w[je][ie] / g
                val eMul = d[je][k] / g
                check(dMul > 0)
                for (j in 0..je) {
                    d[j][k] = dMul * d[j][k] - eMul * w[j][ie]
                }
                bound[k] = dMul * bound[k] - eMul * goal[ie]
                bDiv[k] *= dMul
                check(d[je][k] == 0L)
            }
            // eliminate variable je from r * a + free -> min
            if (r[je] != 0L) {
                val g = gcd(abs(r[je]), abs(w[je][ie])) * w[je][ie].sign
                val rMul = w[je][ie] / g
                val eMul = r[je] / g
                check(rMul > 0)
                for (j in 0..je) {
                    r[j] = rMul * r[j] - eMul * w[j][ie]
                }
                free = rMul * free + eMul * goal[ie]
                resDiv *= rMul
                check(r[je] == 0L)
            }
            // eliminate constraint ie and variable je from w * a == goal
            for (i in 0..ie) {
                if (w[je][i] == 0L) continue // is already zero
                val g = gcd(abs(w[je][i]), abs(w[je][ie]))
                val wMul = w[je][ie] / g
                val eMul = w[je][i] / g
                for (j in 0..je) {
                    w[j][i] = wMul * w[j][i] - eMul * w[j][ie]
                }
                goal[i] = wMul * goal[i] - eMul * goal[ie]
            }
            // consistency check: constraint eliminated to trivial 0 = 0
            for (j in 0..<m) check(w[j][ie] == 0L)
            check(goal[ie] == 0L)
            nr--
            mr--
        }
        check(nr == 0 || mr == 0) // all constraints or all variables should have been eliminated
        // bruteforce remaining mr variables
        // println("${lineIndex + 1}: !!! ${r.slice(0..<mr).joinToString(",")}")
        var res = Long.MAX_VALUE
        val psum = LongArray(m) // partial sum for bounds
        fun bf(j: Int, sum: Long) {
            if (j >= mr) {
                // check bounds
                for (k in 0..<m) if (psum[k] < bound[k] || (psum[k] - bound[k]) % bDiv[k] != 0L) return
                if (sum % resDiv != 0L) return
                res = minOf(res, sum / resDiv)
                return
            }
            for (aj in 0..gMax) {
                for (k in 0..<m) psum[k] += d[j][k] * aj
                bf(j + 1, sum + r[j] * aj)
                for (k in 0..<m) psum[k] -= d[j][k] * aj
            }
        }
        bf(0, free)
        println("${lineIndex + 1}: $res in ${start.elapsedNow().toString(DurationUnit.SECONDS, 3)}")
        check(res < INF)
        ans += res
    }
    println("Answer: $ans in ${start0.elapsedNow().toString(DurationUnit.SECONDS, 3)}")
}
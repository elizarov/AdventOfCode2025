import java.awt.image.*
import java.io.*
import javax.imageio.*
import kotlin.math.*

private const val U = 1
private const val D = 2
private const val L = 4
private const val R = 8

fun main() {
    data class P(val x: Int, val y: Int)
    val p = readInput("Day09").map { s ->
        val (x, y) = s.split(",").map { it.toInt()}
        P(x, y)
    }
    val n = p.size
    val xs = p.map { it.x }.distinct().sorted().toIntArray()
    val ys = p.map { it.y }.distinct().sorted().toIntArray()
    val xm = xs.withIndex().associate { (i, x) -> x to i }
    val ym = ys.withIndex().associate { (i, y) -> y to i }
    var ans = 0L
    val a = Array(xs.size) { CharArray(ys.size) { ' ' } }
    for (i in 0..<n) {
        val xi = xm[p[i].x]!!
        val yi = ym[p[i].y]!!
        check(a[xi][yi] == ' ')
        val j1 = (i + 1) % n
        val j2 = (i + n - 1) % n
        val j = if (p[j1].x == p[i].x) j1 else j2
        check(p[j].x == p[i].x)
        a[xi][yi] = if (p[j].y > p[i].y) 'v' else '^'
    }
    for (i in 0..<n) {
        val j = (i + 1) % n
        when  {
            p[i].x == p[j].x -> {
                val x = xm[p[i].x]!!
                val yi = ym[p[i].y]!!
                val yj = ym[p[j].y]!!
                val y1 = minOf(yi, yj)
                val y2 = maxOf(yi, yj)
                for (y in y1 + 1..<y2) {
                    check(a[x][y] == ' ')
                    a[x][y] = '|'
                }
            }
            p[i].y == p[j].y -> {
                val y = ym[p[i].y]!!
                val xi = xm[p[i].x]!!
                val xj = xm[p[j].x]!!
                val x1 = minOf(xi, xj)
                val x2 = maxOf(xi, xj)
                for (x in x1 + 1..<x2) {
                    check(a[x][y] == ' ')
                    a[x][y] = '-'
                }
            }
            else -> error("$i -> $j")
        }
    }
    fun dump() {
        println("=".repeat(xs.size))
        for (y in ys.indices) {
            for (x in xs.indices) {
                print(a[x][y])
            }
            println()
        }
    }
    dump()
    for (y in ys.indices) {
        var c = ' '
        var inside = false
        for (x in xs.indices) {
            val wasX = inside || c != ' '
            when (a[x][y]) {
                '|' -> inside = !inside
                'v' -> when(c) {
                    ' ' -> c = 'v'
                    'v' -> c = ' '
                    '^' -> { c = ' '; inside = !inside }
                }
                '^' -> when(c) {
                    ' ' -> c = '^'
                    '^' -> c = ' '
                    'v' -> { c = ' '; inside = !inside }
                }
            }
            if ((wasX || inside || c != ' ') && a[x][y] == ' ') a[x][y] = 'X'
        }
        check(!inside)
    }
    dump()
    dumpPng(a, "Day09.png")
    for (i in 0..<n) for (j in i + 1..<n) {
        val xi = xm[p[i].x]!!
        val xj = xm[p[j].x]!!
        val x1 = minOf(xi, xj)
        val x2 = maxOf(xi, xj)
        val yi = ym[p[i].y]!!
        val yj = ym[p[j].y]!!
        val y1 = minOf(yi, yj)
        val y2 = maxOf(yi, yj)
        var ok = true
        check@for (x in x1..x2) for (y in y1..y2) if (a[x][y] == ' ') {
            ok = false
            break@check
        }
        if (ok) {
            val a = (abs(p[i].x - p[j].x) + 1L) * (abs(p[i].y - p[j].y) + 1L)
            ans = maxOf(ans, a)
        }
    }
    println(ans)
}

fun dumpPng(a: Array<CharArray>, fileName: String) {
    val (m, n) = a.size2()
    val img = BufferedImage(m, n, BufferedImage.TYPE_INT_RGB)
    for (x in 0..<m) for(y in 0..<n) {
        val c = when (a[x][y]) {
            'v', '^' -> 0xff0000 // red
            'X' -> 0x00ff00 // green
            '-', '|' -> 0x007f00 // light green
            else -> 0xffffff // white
        }
        img.setRGB(x, y, c)
    }
    ImageIO.write(img, "png", File(fileName))
}

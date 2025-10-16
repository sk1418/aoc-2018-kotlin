import utils.*

// https://adventofcode.com/2018/day/6
fun main() {
    val today = "Day06"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay06() = Day06(map { line -> line.split(", ").let { it[0].toInt() to it[1].toInt() } })

    fun part1(input: List<String>): Int = input.toDay06().calc()

    fun part2(input: List<String>, limit: Int): Int = input.toDay06() calcWithLimit limit

    chkTestInput(Part1, testInput, 17) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 16) { part2(it, 32) }
    solve(Part2, input) { part2(it, 10000) }
}


private data class Day06(val points: List<Point>) {
    val minX = points.minOf { it.first }
    val maxX = points.maxOf { it.first }
    val minY = points.minOf { it.second }
    val maxY = points.maxOf { it.second }


    fun calc(): Int {
        val xBorders = listOf(minX, maxX)
        val yBorders = listOf(minY, maxY)

        val infinite = mutableSetOf<Point>()
        // the coordination -> markedBy point to distance
        val result: MutableMap<Point, Pair<Point, Int>> = mutableMapOf()

        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                val here = x to y
                val distances = points.map { it to (it manhattanDistanceTo here) } //point to distance
                val (closest, d) = distances.minBy { it.second }
                val isTie = distances.count { it.second == d } > 1
                if (!isTie) result[here] = closest to d

                // find infinite owners (those touching the border)
                if (x in xBorders || y in yBorders) {
                    result[here]?.first?.let { infinite += it }
                }
            }
        }

        return result.values.filter { it.first !in infinite }.groupingBy { it.first }.eachCount().maxOf { it.value }
    }

    infix fun calcWithLimit(limit: Int): Int {
        return (minX..maxX).sumOf { x ->
            (minY..maxY).count { y ->
                val here = x to y
                points.sumOf { it manhattanDistanceTo here } < limit
            }
        }
    }
}
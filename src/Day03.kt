import utils.*

// https://adventofcode.com/2018/day/3
fun main() {
    val today = "Day03"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val boxRegex = """#(?<id>\d+) @ (?<x>\d+),(?<y>\d+): (?<width>\d+)x(?<height>\d+)""".toRegex()
    fun parseInput(input: List<String>) = input.map { line ->
        boxRegex.matchEntire(line)!!.destructured.let { (id, x, y, width, height) ->
            Box(id.toInt(), (x.toInt() until x.toInt() + width.toInt()), (y.toInt() until y.toInt() + height.toInt()))
        }
    }

    fun part1(input: List<String>): Int = parseInput(input).overlappedPoints()
    fun part2(input: List<String>): Int = parseInput(input).findNonOverlappedBox().boxId

    chkTestInput(Part1, testInput, 4) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 3) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Box(val boxId: Int, val xRange: IntRange, val yRange: IntRange) {
    fun intersect(other: Box): Boolean {
        // IntRange's intersect generates new set, avoid using it here
        return xRange.first <= other.xRange.last &&
            xRange.last >= other.xRange.first &&
            yRange.first <= other.yRange.last &&
            yRange.last >= other.yRange.first
    }
}

private fun List<Box>.overlappedPoints(): Int {
    val pointsMap = mutableMapOf<Pair<Int, Int>, Int>()
    forEach { box ->
        for (x in box.xRange) {
            for (y in box.yRange) {
                val point = x to y
                pointsMap[point] = pointsMap.getOrDefault(point, 0) + 1
            }
        }
    }
    return pointsMap.filter { it.value > 1 }.size
}

private fun List<Box>.findNonOverlappedBox(): Box {
    forEach { box1 ->
        if (none { box2 -> box2 != box1 && box2.intersect(box1) }) return box1
    }
    error("no non-overlapping box found")
}
import utils.*

// https://adventofcode.com/2018/day/25
fun main() {
    val today = "Day25"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    chkTestInput(Part1, testInput, 0) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 0) { part2(it) }
    solve(Part2, input) { part2(it) }
}
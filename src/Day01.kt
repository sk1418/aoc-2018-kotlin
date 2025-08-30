import utils.*

// https://adventofcode.com/2018/day/1
fun main() {
    val today = "Day01"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toIntList() = map { Integer.parseInt(it) }
    fun part1(input: List<String>) = input.toIntList().sum()

    fun part2(input: List<String>): Int {
        val ints = input.toIntList()
        val occurred = mutableSetOf<Int>()
        val firstResult = input.toIntList().fold(0) { next, acc -> (next + acc).also { occurred.add(it) } }
        var step = firstResult
        while (true) {
            ints.forEach { s ->
                step += s
                if (step in occurred) return step
                occurred.add(step)
            }
        }
    }

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 2) { part2(it) }
    solve(Part2, input) { part2(it) }
}
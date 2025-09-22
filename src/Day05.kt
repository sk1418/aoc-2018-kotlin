import utils.*
import kotlin.math.abs

// https://adventofcode.com/2018/day/5
fun main() {
    val today = "Day05"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>)= part1(input.single())
    fun part2(input: List<String>)= part2(input.single())

    chkTestInput(Part1, testInput, 10) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 4) { part2(it) }
    solve(Part2, input) { part2(it) }
}


private val offset = abs('A' - 'a')
private fun part1(input: String): Int {
    val stack = ArrayDeque<Char>()
    input.forEach { c ->
        if (stack.isNotEmpty() && abs(stack.last() - c) == offset) {
            stack.removeLast()
        } else {
            stack.addLast(c)
        }
    }
    return stack.size
}

private fun part2(input: String): Int {
    return ('a'..'z').minOf { c -> part1(input.replace("$c", "", true)) }

}
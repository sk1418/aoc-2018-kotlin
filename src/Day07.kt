import utils.*
import java.util.*

// https://adventofcode.com/2018/day/7
fun main() {
    val today = "Day07"

    val input = readInput(today)
    val testInput = readTestInput(today)
    fun List<String>.toDay07(): Day07 {
        val chars = mutableSetOf<Char>()
        val pairs = this.map { line ->
            val charsInLine = line.drop(1).replace("[^A-Z]*".toRegex(), "").toList()
            chars.addAll(charsInLine)
            Pair(charsInLine[0], charsInLine[1])
        }
        return Day07(chars, pairs)
    }

    fun part1(input: List<String>): String {
        return input.toDay07().findRoute()
    }

    fun part2(input: List<String>, baseDuration: Int, workerCount: Int): Int {
        return input.toDay07().multiWorkers(baseDuration, workerCount)
    }

    chkTestInput(Part1, testInput, "CABDFE") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 15) { part2(it, 0, 2) }
    solve(Part2, input) { part2(it, 60, 5) }
}

private data class Day07(val allChars: Set<Char>, val pairs: List<Pair<Char, Char>>) {
    val deps = pairs.groupBy { it.second }.mapValues { (_, list) -> list.map { it.first }.sorted().toMutableList() }
    var initChars = allChars.filter { c -> c !in pairs.map { it.second } }

    val result1 = mutableListOf<Char>()
    val readyQ = PriorityQueue<Char>().apply { addAll(initChars) }

    // ready to go
    fun findRoute(): String {
        navigate()
        return result1.joinChars()
    }

    private fun navigate() {
        while (!readyQ.isEmpty()) {
            val c = readyQ.poll()
            result1.add(c)
            deps.filter { (key, depList) -> key !in result1 && key !in readyQ && result1.containsAll(depList) }.map { it.key }.let { readyQ.addAll(it) }
        }
    }

    data class Worker(var task: Char? = null, var remaining: Int = 0)


    val done = mutableSetOf<Char>()
    var time = 0

    fun multiWorkers(baseDuration: Int = 60, workerCount: Int = 5): Int {
        val workers = Array(workerCount) { Worker() }
        fun durationOf(c: Char) = baseDuration + (c - 'A' + 1)

        // Simulation tick
        while (done.size < allChars.size) {
            // Assign idle workers to available tasks
            workers.filter { it.task == null }.forEach { w ->
                val next = readyQ.poll()
                if (next != null) {
                    w.task = next
                    w.remaining = durationOf(next)
                }
            }

            time++
            workers.forEach { w ->
                if (w.task != null) {
                    w.remaining--
                    if (w.remaining == 0) {
                        val finished = w.task!!
                        done += finished
                        w.task = null

                        // Remove completed step from others' deps
                        deps.forEach { (k, v) ->
                            v.remove(finished)
                            if (v.isEmpty() && k !in done && k !in readyQ &&
                                workers.none { it.task == k }
                            ) {
                                readyQ.add(k)
                            }
                        }
                    }
                }
            }
        }

        return time
    }

}
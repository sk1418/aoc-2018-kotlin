import utils.*

// https://adventofcode.com/2018/day/4
fun main() {
    val today = "Day04"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val idRegex = """#(\d+)""".toRegex()
    val minRegex = """:(\d+)]""".toRegex()
    fun parseInput(input: List<String>): MutableList<Guard> {
        val guards = mutableListOf<Guard>()
        var currentGuard = Guard(-1)
        var sleepAt = -1
        input.sorted().forEach {
            when {
                "Guard" in it -> {
                    val id = idRegex.find(it)!!.groupValues[1].toInt()
                    currentGuard = guards.find { g -> g.id == id } ?: Guard(id).also { guards.add(it) }
                }

                "falls asleep" in it -> sleepAt = minRegex.find(it)!!.groupValues[1].toInt()

                "wakes up" in it -> {
                    val wakeAt = minRegex.find(it)!!.groupValues[1].toInt()
                    currentGuard.sleepRanges.add(sleepAt until wakeAt)
                }
            }

        }
        return guards
    }


    fun part1(input: List<String>) = Day04(parseInput(input)).strategy1()
    fun part2(input: List<String>) = Day04(parseInput(input)).strategy2()

    chkTestInput(Part1, testInput, 240) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 4455) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Guard(val id: Int, val sleepRanges: MutableList<IntRange> = mutableListOf())

private data class Day04(val guards: List<Guard>) {
    fun strategy1(): Int {
        val theGuard = guards.maxBy { guard -> guard.sleepRanges.sumOf { it.count() } }
        val theMinute = (0 until 60).maxBy { minute ->
            theGuard.sleepRanges.count { minute in it }
        }
        return theGuard.id * theMinute
    }

    fun strategy2(): Int {
        // guardId -> (minute, count)
        val guardMap = mutableMapOf<Int, Pair<Int, Int>>()
        guards.map { guard ->
            val minuteList = MutableList(60) { 0 }
            guard.sleepRanges.forEach { range -> range.forEach { minuteList[it] += 1 } }
            val thePair = minuteList.mapIndexed { idx, count -> idx to count }.maxBy { it.second }
            guardMap[guard.id] = thePair

        }
        val maxEntry = guardMap.entries.maxBy {(id, pair)-> pair.second  }

        return maxEntry.key * maxEntry.value.first
    }
}
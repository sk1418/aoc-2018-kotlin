import utils.*

// https://adventofcode.com/2018/day/2
fun main() {
    val today = "Day02"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        var (c2 , c3) = 0 to 0
        input.forEach { line ->
            val dict = line.groupingBy { it }.eachCount()
            c2+= if (dict.values.any { it == 2 }) 1 else 0
            c3+= if (dict.values.any { it == 3 }) 1 else 0
        }
        return c2 * c3
    }

    fun part2(input: List<String>): String {
        // O(n * m) solution
        //- Remove character i from all strings.
        //-	Put results in a Set.
        //-	If a duplicate occurs → that’s the answer.

        // all string.length are the same
        val length = input.first().length
        for(i in 0 until length) {
            val seen = mutableSetOf<String>()
            for (line in input) {
                val modified = line.removeRange(i, i + 1)
                if (modified in seen) return modified
                seen.add(modified)
            }
        }
        error("no solution found")
        //slower but functional solution O(n^2 * m):
//        return input.asSequence().flatMap { a ->
//            input.asSequence().mapNotNull { b ->
//                if (a == b) null
//                else {
//                    val common = a.zip(b).mapNotNull { (ca, cb) -> if (ca == cb) ca else null }
//                    if (common.size == a.length - 1) common.joinToString("") else null
//                }
//            }
//        }.first()
    }


    chkTestInput(Part1, testInput, 12) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}
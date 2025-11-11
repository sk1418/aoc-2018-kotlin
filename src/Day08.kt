import utils.*

// https://adventofcode.com/2018/day/8
fun main() {
    val today = "Day08"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return input.single().toInts().toIntArray().let { data -> parseNode(data, 0) }.second
    }

    fun part2(input: List<String>): Int {
        return input.single().toInts().toIntArray().let { data -> parseNode(data, 0) }.third
    }

    chkTestInput(Part1, testInput, 138) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 66) { part2(it) }
    solve(Part2, input) { part2(it) }
}

// returns Triple<nextIndex, metadataSumForSubtree, nodeValue>
private fun parseNode(data: IntArray, startIdx: Int): Triple<Int, Int, Int> {
    var idx = startIdx
    val childCount = data[idx++]
    val metaCount = data[idx++]

    val childValues = if (childCount > 0) IntArray(childCount) else IntArray(0)
    var totalMetaSum = 0

    // parse children recursively
    for (i in 0 until childCount) {
        val (nextIdx, childMetaSum, childValue) = parseNode(data, idx)
        idx = nextIdx
        totalMetaSum += childMetaSum
        childValues[i] = childValue
    }

    // read metadata entries
    val metadata = IntArray(metaCount)
    var metaSumHere = 0
    for (m in 0 until metaCount) {
        val v = data[idx++]
        metadata[m] = v
        metaSumHere += v
    }
    totalMetaSum += metaSumHere

    // compute node value (part 2 rule)
    val nodeValue = if (childCount == 0) {
        // no children -> value is sum of metadata
        metaSumHere
    } else {
        // children exist -> metadata entries are 1-based indices into children
        var sum = 0
        for (v in metadata) {
            if (v in 1..childCount) {
                sum += childValues[v - 1]
            }
        }
        sum
    }

    return Triple(idx, totalMetaSum, nodeValue)
}
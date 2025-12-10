private class Day10 {
	fun parse(input: List<String>): Triple<List<List<Boolean>>, List<List<Set<Int>>>, List<List<Int>>> {
		val diagrams = mutableListOf<List<Boolean>>()
		val buttonsList = mutableListOf<List<Set<Int>>>()
		val joltages = mutableListOf<List<Int>>()
		for (line in input) {
			val words = line.split(' ')
			val prat1 = words.first()
			val prat2 = words.subList(1, words.size - 1)
			val part3 = words.last()
			val diagram = prat1.substring(1, prat1.length - 1).map { it == '#' }
			diagrams.add(diagram)
			val buttons = prat2.map { it.substring(1, it.length - 1) }.map { b ->
				b.split(',').map { it.toInt() }.toSet()
			}
			buttonsList.add(buttons)
			val joltage = part3.substring(1, part3.length - 1).split(',').map { it.toInt() }
			joltages.add(joltage)
		}
		return Triple(diagrams, buttonsList, joltages)
	}

	fun pressButtonsRecurs1(diagram: List<Boolean>, buttons: List<Set<Int>>, level: Int, currMin: Int): Int {
		if (level >= currMin) return level
		if (diagram.all { !it }) return level
		var minRes = currMin
		for (i in buttons.indices) {
			val pressButton = buttons[i]
			val newDiagram = diagram.toMutableList()
			for (w in pressButton) newDiagram[w] = !newDiagram[w]
			val res = pressButtonsRecurs1(newDiagram, buttons.subList(i + 1, buttons.size), level + 1, minRes)
			if (res < minRes) minRes = res
		}
		return minRes
	}

	fun pressButtonsRecurs2(joltage: MutableList<Int>, buttons: List<Set<Int>>, level: Int, currMin: Int): Int {
		if (level >= currMin) return level
		if (joltage.all { it == 0 }) return level
		if (buttons.isEmpty()) return Int.MAX_VALUE
		val counters = IntArray(joltage.size)
		for (button in buttons) {
			for (w in button) counters[w]++
		}
		val minIndex = counters.withIndex().filter { it.value != 0 }.minBy { it.value }.index
		val minButtons = buttons.filter { minIndex in it }.sortedByDescending { it.size }
		if (minButtons.isEmpty()) return Int.MAX_VALUE
		return pressButtonsRecurs3(joltage, buttons - minButtons, level, currMin, minButtons)
	}

	fun pressButtonsRecurs3(joltage: MutableList<Int>, buttons: List<Set<Int>>, level: Int, currMin: Int, minButtons: List<Set<Int>>): Int {
		if (level + joltage.max() >= currMin) return currMin
		var minRes = currMin
		val pressButton = minButtons.first()
		val minJol = pressButton.minOf { joltage[it] }
		if (minButtons.size == 1) {
			for (w in pressButton) joltage[w] -= minJol
			val res = pressButtonsRecurs2(joltage, buttons, level + minJol, minRes)
			for (w in pressButton) joltage[w] += minJol
			if (res < minRes) minRes = res
		} else {
			for (j in minJol downTo 0) {
				for (w in pressButton) joltage[w] -= j
				val res = pressButtonsRecurs3(joltage, buttons, level + j, minRes, minButtons.subList(1, minButtons.size))
				for (w in pressButton) joltage[w] += j
				if (res < minRes) minRes = res
			}
		}
		return minRes
	}
}

fun main() {
	fun part1(input: List<String>): Int {
		val task = Day10()
		val (diagrams, buttonsList, _) = task.parse(input)
		var result = 0
		for ((i, diagram) in diagrams.withIndex()) {
			val buttons = buttonsList[i]
			result += task.pressButtonsRecurs1(diagram, buttons, 0, Int.MAX_VALUE)
		}
		return result
	}

	fun part2(input: List<String>): Int {
		val task = Day10()
		val (_, buttonsList, joltages) = task.parse(input)
		var result = 0
		for ((i, joltage) in joltages.withIndex()) {
			val buttons = buttonsList[i]
			result += task.pressButtonsRecurs2(joltage.toMutableList(), buttons, 0, Int.MAX_VALUE)
		}
		return result
	}

	val testInput = readInput("Day10_test")
	check(part1(testInput) == 7)
	check(part2(testInput) == 33)

	val input = readInput("Day10")
	part1(input).println() // 550
	part2(input).println() // 20042 (~13 minutes)
}

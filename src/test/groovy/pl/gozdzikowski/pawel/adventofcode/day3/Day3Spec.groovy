package pl.gozdzikowski.pawel.adventofcode.day3

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day3Spec extends Specification {
    GearRatios gearRatios = new GearRatios()

    def 'sample part1'() {
        given:
        def schema = """467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...\$.*....
.664.598..
"""
        when:
        Integer result = gearRatios.sumAdjacentToAnySymbol(new StringInput(schema))
        then:
        result == 4361
    }

    def 'solution part1'() {
        given:
        Input input = new FileInput("day3.txt")
        when:
        Integer result = gearRatios.sumAdjacentToAnySymbol(input)
        then:
        result == 527369
    }

    def 'sample part2'() {
        given:
        def schema = """467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...\$.*....
.664.598..
"""
        when:
        Integer result = gearRatios.multiplySumOfAdjacent(new StringInput(schema))
        then:
        result == 467835
    }

    def 'solution part2'() {
        given:
        Input input = new FileInput("day3.txt")
        when:
        Integer result = gearRatios.multiplySumOfAdjacent(input)
        then:
        result == 73074886
    }
}

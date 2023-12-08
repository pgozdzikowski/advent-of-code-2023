package pl.gozdzikowski.pawel.adventofcode.day8

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day8Spec extends Specification {
    HauntedWasteland hauntedWasteland = new HauntedWasteland()

    def 'should calculate for sample part1'() {
        given:
        def input = """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)"""
        when:
        Long result = hauntedWasteland.countWaysToExit(new StringInput(input))
        then:
        result == 6
    }

    def 'should calculate for input'() {
        given:
        def input = new FileInput("day8.txt")
        when:
        Long result = hauntedWasteland.countWaysToExit(input)
        then:
        result == 19631
    }

    def 'should calculate for sample part2'() {
        given:
        def input = """LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)"""
        when:
        Long result = hauntedWasteland.countWaysGhostToExit(new StringInput(input))
        then:
        result == 6
    }

    def 'should calculate for task input part 2'() {
        given:
        def input = new FileInput("day8.txt")
        when:
        Long result = hauntedWasteland.countWaysGhostToExit(input)
        then:
        result == 21003205388413
    }
}

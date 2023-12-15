package pl.gozdzikowski.pawel.adventofcode.day14

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day14Spec extends Specification {

    ParabolicReflectorDish parabolicReflectorDish = new ParabolicReflectorDish()

    def 'sample 1'() {
        given:
            String input = """O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#...."""
        when:
          Long result = parabolicReflectorDish.calculateTotalLoad(new StringInput(input))
        then:
            result == 136
    }

    def 'part1'() {
        given:
            Input input = new FileInput("day14.txt")
        when:
            def load = parabolicReflectorDish.calculateTotalLoad(input)
        then:
            load == 105461

    }
}

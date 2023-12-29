package pl.gozdzikowski.pawel.adventofcode.day21

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day21Spec extends Specification {

    StepCounter stepCounter = new StepCounter()

    def 'should calculate possible positions after 6 steps sample'() {
        given:
            String input = """...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
..........."""
        when:
            def plots = stepCounter.calculateNumOfPlots(new StringInput(input), 6)
        then:
            plots == 16
    }

    def 'part1'() {
        given:
            Input input = new FileInput('day21.txt')
        when:
            def plots = stepCounter.calculateNumOfPlots(input, 64)
        then:
            plots == 3639
    }
}

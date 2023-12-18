package pl.gozdzikowski.pawel.adventofcode.day18

import pl.gozdzikowski.pawel.adventofcode.day16.TheFloorWillBeLava
import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Ignore
import spock.lang.Specification

class Day18Spec extends Specification {

    LavaductLagoon lavaductLagoon = new LavaductLagoon()

    def 'sample part1'() {
        given:
        String input = """R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
"""
        when:
        def result = lavaductLagoon.calculateCubic(new StringInput(input))
        then:
        result == 62
    }

    @Ignore
    def 'part1'() {
        given:
            Input input = new FileInput('day18.txt')
        when:
            def result = lavaductLagoon.calculateCubic(input)
        then:
            result == 47675
    }

}

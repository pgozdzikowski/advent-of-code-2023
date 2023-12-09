package pl.gozdzikowski.pawel.adventofcode.day9

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import spock.lang.Specification

class Day9Spec extends Specification {
    MirageMaintenance mirageMaintenance = new MirageMaintenance()

    def 'should find for sample input'() {
        given:
        Input list = new ListInput([
                "0 3 6 9 12 15",
                "1 3 6 10 15 21",
                "10 13 16 21 30 45"
        ])
        when:
        Integer result = mirageMaintenance.findResultFromEnd(list)
        then:
        result == 114
    }

    def 'solution part1'() {
        given:
        Input list = new FileInput("day9.txt")
        when:
        Integer result = mirageMaintenance.findResultFromEnd(list)
        then:
        result == 1938800261
    }

    def 'should find for sample part2'() {
        given:
        Input list = new ListInput([
                "0 3 6 9 12 15",
                "1 3 6 10 15 21",
                "10 13 16 21 30 45"
        ])
        when:
        Integer result = mirageMaintenance.findResultFromBegin(list)
        then:
        result == 2
    }

    def 'solution part2'() {
        given:
        Input list = new FileInput("day9.txt")
        when:
        Integer result = mirageMaintenance.findResultFromBegin(list)
        then:
        result == 1112
    }
}

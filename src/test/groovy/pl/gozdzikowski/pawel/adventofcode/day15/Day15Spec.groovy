package pl.gozdzikowski.pawel.adventofcode.day15

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day15Spec extends Specification {
    LensLibrary lensLibrary = new LensLibrary()

    def 'should find for example string'() {
        given:
            String hash = "HASH"
        when:
            def result = lensLibrary.calculateSumOfHashAlgorithm(new StringInput(hash))
        then:
            result == 52
    }

    def 'for sample'() {
        given:
            String hash = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        when:
            def result = lensLibrary.calculateSumOfHashAlgorithm(new StringInput(hash))
        then:
            result == 1320
    }

    def 'part1'() {
        given:
            Input input = new FileInput('day15.txt')
        when:
            def res = lensLibrary.calculateSumOfHashAlgorithm(input)
        then:
            res == 510013
    }

    def 'sample focusing power'() {
        given:
            String hash = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        when:
            def result = lensLibrary.calculateFocusingPower(new StringInput(hash))
        then:
            result == 145
    }

    def 'part2'() {
        given:
            Input input = new FileInput('day15.txt')
        when:
            def res = lensLibrary.calculateFocusingPower(input)
        then:
            res == 268497
    }
}

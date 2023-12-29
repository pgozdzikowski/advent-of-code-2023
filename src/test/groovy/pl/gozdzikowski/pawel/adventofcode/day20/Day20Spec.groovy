package pl.gozdzikowski.pawel.adventofcode.day20

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day20Spec extends Specification{
    PulsePropagation pulsePropagation = new PulsePropagation()

    def 'should propagate pulse correctly'() {
        given:
            def input = """broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
"""
        when:
            def result = pulsePropagation.calculateMultiplyOfLowAndHighPulse(new StringInput(input))
        then:
            result == 32000000
    }

    def 'sample 2'() {
        given:
            def input = """broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
"""
        when:
            def result = pulsePropagation.calculateMultiplyOfLowAndHighPulse(new StringInput(input))
        then:
            result == 11687500
    }

    def 'part1'() {
        given:
            Input input = new FileInput('day20.txt')
        when:
            def pulse = pulsePropagation.calculateMultiplyOfLowAndHighPulse(input)
        then:
            pulse == 808146535
    }

}

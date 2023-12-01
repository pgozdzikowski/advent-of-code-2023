package pl.gozdzikowski.pawel.adventofcode

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import spock.lang.Specification

class InputSpec extends Specification{

    def 'should read single line of task'() {
        given:
        Input input = new FileInput("day0.txt")
        when:
        List<String> lines = input.get()
        then:
        lines.first() == '9686'
    }
}

package pl.gozdzikowski.pawel.adventofcode.day12

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import spock.lang.Ignore
import spock.lang.Specification

class Day12Spec extends Specification {

    HotSprings hotSprings = new HotSprings()

    def 't1'() {
        given:
            List<Integer> list = hotSprings.calculateNumsOfBroken('.#.###.#.######')
        expect:
            list == [1, 3, 1, 6]
    }

    def 't2'() {
        given:
            Long num = hotSprings.calculateForSingleLine('?###???????? 3,2,1')
        expect:
            num == 10
    }

    def 'should calculate sum for sample'() {
        given:
            List<String> list = [
                    '???.### 1,1,3',
                    '.??..??...?##. 1,1,3',
                    '?#?#?#?#?#?#?#? 1,3,1,6',
                    '????.#...#... 4,1,1',
                    '????.######..#####. 1,6,5',
                    '?###???????? 3,2,1'
            ]
        when:
            Long res = hotSprings.calculateSumOfArrangement(new ListInput(list))
        then:
            res == 21
    }

    @Ignore("Long test running")
    def 'part1'() {
        given:
            Input input = new FileInput('day12.txt')
        when:
            Long res = hotSprings.calculateSumOfArrangement(input)
        then:
            res == 7344
    }
}

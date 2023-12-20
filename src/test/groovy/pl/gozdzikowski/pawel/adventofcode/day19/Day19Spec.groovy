package pl.gozdzikowski.pawel.adventofcode.day19


import pl.gozdzikowski.pawel.adventofcode.day18.LavaductLagoon
import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Ignore
import spock.lang.Specification

class Day19Spec extends Specification {

    Aplenty aplenty = new Aplenty()

    def 'parse single workflow'() {
        given:
        String input = "pxh{s>526:A,A}"
        when:
        def result = aplenty.parseConditions(input)
        then:
        result == 62
    }

    def 'parse single parts'() {
        given:
            String input = "{x=787,m=2655,a=1222,s=2876}"
        when:
            def result = aplenty.compileParts(input)
        then:
            result == 62
    }

    def 'sample'() {
        given:
            String input = """px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
"""
        when:
            def result = aplenty.calculateSumOfPartsCategory(new StringInput(input))
        then:
            result == 19114
    }

    def 'solution part1'() {
        given:
            Input input = new FileInput('day19.txt')
        when:
            def result = aplenty.calculateSumOfPartsCategory(input)
        then:
            result == 47675
    }


}

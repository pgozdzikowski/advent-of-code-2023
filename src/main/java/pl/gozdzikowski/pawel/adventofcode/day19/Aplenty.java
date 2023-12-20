package pl.gozdzikowski.pawel.adventofcode.day19;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aplenty {
    Pattern pattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");

    public Long calculateSumOfPartsCategory(Input input) {
        String[] splitedInput = input.getContent().split("\\n\\n");
        List<Map<String, Long>> acceptedParts = new LinkedList<>();
        List<Map<String, Long>> parts = compileParts(splitedInput[1]);
        SequencedMap<String, List<Condition>> workflows = parseConditions(splitedInput[0]);

        for (Map<String, Long> part : parts) {
            String currentWorkflow = "in";
            while (!isAccepted(currentWorkflow) && !isRejected(currentWorkflow)) {
                List<Condition> conditions = workflows.get(currentWorkflow);
                for (Condition condition : conditions) {
                    if(condition.test(part)) {
                        currentWorkflow = condition.positive();
                        break;
                    }
                    else {
                        if(condition.negative() != null) {
                            currentWorkflow = condition.negative();
                            break;
                        }
                    }
                }
            }

            if(isAccepted(currentWorkflow))
                acceptedParts.add(part);
        }


        return acceptedParts.stream().map((el) -> el.values().stream().reduce(Long::sum).get())
                .reduce(Long::sum).get();
    }

    public List<Map<String, Long>> compileParts(String partsAsString) {
        List<Map<String, Long>> parts = new LinkedList<>();

        for (String partAsString : partsAsString.split("\\n")) {
            Map<String, Long> part = new HashMap<>();
            Matcher matcher = pattern.matcher(partAsString);
            matcher.find();
            part.put("x", Long.parseLong(matcher.group(1)));
            part.put("m", Long.parseLong(matcher.group(2)));
            part.put("a", Long.parseLong(matcher.group(3)));
            part.put("s", Long.parseLong(matcher.group(4)));
            parts.add(part);
        }
        return parts;
    }

    public SequencedMap<String, List<Condition>> parseConditions(String workflows) {
        String[] workflowsAsString = workflows.split("\\n");
        SequencedMap<String, List<Condition>> workflowsConditions = new LinkedHashMap<>();
        for (String workflowAsString : workflowsAsString) {
            String workflowName = workflowAsString.substring(0, workflowAsString.indexOf('{'));
            String conditionsAsString = workflowAsString.substring(workflowAsString.indexOf('{') + 1, workflowAsString.indexOf('}'));
            List<Condition> conditions = new LinkedList<>();
            String[] singleCondtions = conditionsAsString.split(",");
            for (int i = 0; i < singleCondtions.length - 1; ++i) {
                String negative = (i == singleCondtions.length - 2) ? singleCondtions[singleCondtions.length - 1] : null;
                String[] condToWhereToGo = singleCondtions[i].split(":");
                if (condToWhereToGo[0].indexOf('<') > 0) {
                    String[] labelToValue = condToWhereToGo[0].split("<");
                    conditions.add(new Condition(labelToValue[0], "<", Long.parseLong(labelToValue[1]), condToWhereToGo[1], negative));
                } else {
                    String[] labelToValue = condToWhereToGo[0].split(">");
                    conditions.add(new Condition(labelToValue[0], ">", Long.parseLong(labelToValue[1]), condToWhereToGo[1], negative));
                }

            }
            workflowsConditions.put(workflowName, conditions);
        }

        return workflowsConditions;
    }

    public boolean isAccepted(String workflowName) {
        return workflowName.equals("A");
    }

    public boolean isRejected(String workflowName) {
        return workflowName.equals("R");
    }


    public record Condition(
            String part,
            String condition,
            Long value,
            String positive,
            String negative
    ) {
        public boolean test(Map<String, Long> toCheck) {

            return switch(condition) {
                case "<" -> toCheck.get(part) < value;
                case ">" -> toCheck.get(part) > value;
                default -> throw new IllegalStateException();
            };
        }
    }
}

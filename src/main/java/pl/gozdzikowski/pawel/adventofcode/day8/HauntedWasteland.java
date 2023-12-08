package pl.gozdzikowski.pawel.adventofcode.day8;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;
import pl.gozdzikowski.pawel.adventofcode.shared.math.MathExt;

import java.util.*;

public class HauntedWasteland {
    public Long countWaysToExit(Input input) {
        String[] commandsToWay = input.getContent().split("\\n\\n");
        List<String> commands = Arrays.stream(commandsToWay[0].split("")).toList();
        Map<String, Pair<String, String>> desertMap = createDeasertMap(commandsToWay[1]);
        Long numberOfSteps = 0L;
        String currentPosition = "AAA";
        while(!currentPosition.equals("ZZZ")) {
            int index = (int)(numberOfSteps % commands.size());
            String nextMove = commands.get(index);
            switch(nextMove) {
                case "L" -> currentPosition = desertMap.get(currentPosition).left();
                case "R" -> currentPosition = desertMap.get(currentPosition).right();
            }
            numberOfSteps++;
        }
        return numberOfSteps;
    }

    public Long countWaysGhostToExit(Input input) {
        String[] commandsToWay = input.getContent().split("\\n\\n");
        List<String> commands = Arrays.stream(commandsToWay[0].split("")).toList();
        Map<String, Pair<String, String>> desertMap = createDeasertMap(commandsToWay[1]);
        Long numberOfSteps = 0L;
        List<String> currentPoints = new ArrayList<>(desertMap.keySet().stream().filter((el) -> el.endsWith("A")).toList());

        Map<String, List<String>> pointToPath = new HashMap<>();
        for (String startNode : currentPoints) {
            pointToPath.put(startNode, new ArrayList<>(List.of(startNode)));
        }
        while (!pointToPath.entrySet().stream().allMatch(((el) -> el.getValue().getLast().endsWith("Z")))) {
            int index = (int)((numberOfSteps % (long)commands.size()));
            String nextMove = commands.get(index);
            List<Map.Entry<String, List<String>>> toBeConsidered = pointToPath.entrySet().stream().filter(((el) -> !el.getValue().getLast().endsWith("Z"))).toList();

            for (Map.Entry<String, List<String>> entry: toBeConsidered) {
                String last = entry.getValue().getLast();
                String nextPosition = switch(nextMove) {
                    case "L" -> desertMap.get(last).left();
                    case "R" -> desertMap.get(last).right();
                    default -> throw new IllegalStateException("");
                };
                entry.getValue().add(nextPosition);
            }
            numberOfSteps++;
        }

        int[] array = pointToPath.values()
                .stream()
                .map((el) -> el.size() - 1)
                .mapToInt(Integer::intValue)
                .toArray();

        return MathExt.lcm(array);
    }

    private Map<String, Pair<String, String>> createDeasertMap(String s) {
        Map<String, Pair<String, String>> map = new HashMap<>();
        List<String> lines = Arrays.stream(s.split("\\n")).toList();
        for (String line : lines) {
            String[] splited = line.split("=");
            String key = splited[0].trim();
            String values = splited[1].trim().replace("(", "").replace(")", "");
            String[] splitedValues = values.split(",");
            map.put(key, Pair.of(splitedValues[0].trim(), splitedValues[1].trim()));
        }
        return map;
    }

}

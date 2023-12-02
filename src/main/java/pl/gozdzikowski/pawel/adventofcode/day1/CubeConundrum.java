package pl.gozdzikowski.pawel.adventofcode.day1;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CubeConundrum {

    Pattern gamePattern = Pattern.compile("Game (\\d+).*");

    public Integer sumOfGames(Input input, Limits limits) {
        return input.get().stream()
                .map((line) -> parseString(line))
                .filter((gameResults) -> gameResults.allMatchLimits(limits))
                .map(GameResults::gameNumber)
                .distinct()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer powerOfGame(Input input) {
        return input.get().stream()
                .map(this::parseString)
                .map(GameResults::findMaxOfSet)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public GameResults parseString(String line) {
        String restOfLine = line.split(":")[1];
        Matcher gameMatcher = gamePattern.matcher(line);
        gameMatcher.find();
        String gameNum = gameMatcher.group(1);
        String trimmedString = restOfLine.trim();
        String[] setOfGames = trimmedString.split(";");

        List<SingleShowResult> singleShowResultList = new LinkedList<>();
        for (int i = 0; i < setOfGames.length; ++i) {
            String[] gameResults = setOfGames[i].split(",");
            int redCount = 0;
            int blueCount = 0;
            int greenCount = 0;
            for (int j = 0; j < gameResults.length; ++j) {
                String[] singleColor = gameResults[j].trim().split("\\s");
                switch (singleColor[1]) {
                    case "red" -> redCount += Integer.parseInt(singleColor[0]);
                    case "blue" -> blueCount += Integer.parseInt(singleColor[0]);
                    case "green" -> greenCount += Integer.parseInt(singleColor[0]);
                    default -> throw new IllegalStateException("Unable to parse " + singleColor[1]);
                }
                singleShowResultList.add(new SingleShowResult(redCount, greenCount, blueCount));
            }

        }
        return new GameResults(Integer.parseInt(gameNum), singleShowResultList);
    }

    public record GameResults(
            Integer gameNumber,
            List<SingleShowResult> setsOfResults
    ) {
        boolean allMatchLimits(Limits limits) {
            return setsOfResults.stream().allMatch((res) -> res.notAboveLimit(limits));
        }

        Integer findMaxOfSet() {
            int maxBlue = setsOfResults.stream().map(SingleShowResult::blue).mapToInt(Integer::intValue).max().orElse(0);
            int maxRed = setsOfResults.stream().map(SingleShowResult::red).mapToInt(Integer::intValue).max().orElse(0);
            int maxGreen = setsOfResults.stream().map(SingleShowResult::green).mapToInt(Integer::intValue).max().orElse(0);
            return maxGreen * maxRed * maxBlue;
        }
    }

    public record SingleShowResult(Integer red,
                                   Integer green,
                                   Integer blue) {

        public boolean notAboveLimit(Limits limits) {
            return red <= limits.maxNumberOfRed && green <= limits.maxNumberOfGreen && blue <= limits.maxNumberOfBlue;
        }

    }

    record Limits(
            Integer maxNumberOfRed,
            Integer maxNumberOfBlue,
            Integer maxNumberOfGreen
    ) {
    }

}


package pl.gozdzikowski.pawel.adventofcode.day6;

import java.util.LinkedList;
import java.util.List;

public class WaitForIt {

    public Long findNumOfWays(String times, String recordDistance) {
        String[] timeArr = times.split("\\s");
        String[] recordDistanceArr = recordDistance.split("\\s");
        List<Long> numOfWaysPerRace = new LinkedList<>();
        for (int i = 0; i < timeArr.length; ++i) {
            numOfWaysPerRace.add(calculateNumOfWays(Long.valueOf(timeArr[i]), Long.valueOf(recordDistanceArr[i])));
        }
        return numOfWaysPerRace.stream()
                .reduce(1L, (acc, el) -> acc * el);
    }


    Long calculateNumOfWays(Long time, Long record) {
        List<TimeToDistance> results = new LinkedList<>();
        for (long i = 0; i <= time; ++i) {
            results.add(new TimeToDistance(i, i == 0L ? 0 : i * (time - i)));
        }
        return results.stream()
                .filter((el) -> el.traveledDistance > record)
                .count();
    }

    Long calculateForLongRace(Long time, Long record) {
        Long delta = time * time - 4 * record;
        double sqrtFromDelta =  Math.sqrt(delta);
        double x1 = (-time - sqrtFromDelta) / (2 * (-1));
        double x2 = (-time + sqrtFromDelta) / (2 * (-1));

        return (long)x1 - (long)x2;
    }

    record TimeToDistance(
            Long timeHoldingButton,
            Long traveledDistance
    ) {
    }
}

package pl.gozdzikowski.pawel.adventofcode.day20;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class PulsePropagation {

    public Long calculateMultiplyOfLowAndHighPulse(Input input) {
        String[] lines = input.getContent().split("\\n");
        Map<String, ElfModule> elfModules = convertToModuleMap(lines);
        Pair<Long, Long> result = LongStream.range(0, 1000)
                .mapToObj((ignore) -> propagateButtonForSinglePress(elfModules))
                .reduce(Pair.of(0L, 0L), (acc, el) -> Pair.of(acc.left() + el.left(), acc.right() + el.right()));

        return result.left() * result.right();
    }

    private Pair<Long, Long> propagateButtonForSinglePress(Map<String, ElfModule> elfModules) {
        Deque<PulsePropagationEntry> elfModulesToProcess = new LinkedList<>(List.of(new PulsePropagationEntry(null, elfModules.get("broadcaster"), PulseSate.LOW)));

        long lowSignals = 1;
        long highSignals = 0;
        while (!elfModulesToProcess.isEmpty()) {
            PulsePropagationEntry currentElfModule = elfModulesToProcess.poll();
            SequencedMap<ElfModule, PulseSate> processed = currentElfModule.dstModule.process(currentElfModule.srcModule, currentElfModule.pulseSate);
            processed.sequencedEntrySet().forEach((el) ->
                    elfModulesToProcess.add(new PulsePropagationEntry(currentElfModule.dstModule, el.getKey(), el.getValue()))
            );

            Map<PulseSate, List<Map.Entry<ElfModule, PulseSate>>> grouped = processed.entrySet()
                    .stream().collect(groupingBy(Map.Entry::getValue));
            lowSignals += grouped.getOrDefault(PulseSate.LOW, Collections.emptyList()).size();
            highSignals += grouped.getOrDefault(PulseSate.HIGH, Collections.emptyList()).size();

        }

        return Pair.of(lowSignals, highSignals);
    }

    record PulsePropagationEntry(
            ElfModule srcModule,
            ElfModule dstModule,
            PulseSate pulseSate
    ) {}

    public Map<String, ElfModule> convertToModuleMap(String[] lines) {
        Map<String, ElfModule> modules = new HashMap<>();
        for (String line : lines) {
            String[] splited = line.split("\\s->\\s");
            ElfModule elfModule = fromNameFactory(splited[0]);
            modules.put(elfModule.getName(), elfModule);
        }

        //make connections
        for (String line : lines) {
            String[] splited = line.split("\\s->\\s");
            String[] outgoingModules = splited[1].split(",");
            Stream.of(outgoingModules).filter((el) -> !modules.containsKey(el.trim())).forEach((el) -> modules.put(el.trim(), new ElfModule.NoOpModule(el.trim())));
            Stream.of(outgoingModules).forEach((el) -> modules.get(convertName(splited[0])).addOutput(modules.get(el.trim())));
            Stream.of(outgoingModules).forEach((el) -> modules.get(el.trim()).addInput(modules.get(convertName(splited[0]))));
        }

        return modules;
    }

    public String convertName(String name) {
        if (Stream.of("%", "&").noneMatch(name::contains))
            return name;
        else {
            return name.substring(1);
        }
    }

    public ElfModule fromNameFactory(String nameWithType) {
        if (Stream.of("%", "&").noneMatch(nameWithType::contains)) {
            return new ElfModule.NoOpModule(nameWithType);
        }

        return switch (nameWithType.charAt(0)) {
            case '%' -> new ElfModule.FlipFlowModule(nameWithType.substring(1));
            case '&' -> new ElfModule.ConjunctionModule(nameWithType.substring(1));
            default -> throw new IllegalStateException("Unable to find valid module");
        };
    }

    public static sealed abstract class ElfModule permits ElfModule.ConjunctionModule, ElfModule.FlipFlowModule, ElfModule.NoOpModule {

        protected List<ElfModule> outputs;
        protected List<ElfModule> inputs;
        private String name;

        public abstract SequencedMap<ElfModule, PulseSate> process(ElfModule sourceModule, PulseSate pulseSate);

        public ElfModule(String name) {
            this.outputs = new ArrayList<>();
            this.inputs = new ArrayList<>();
            this.name = name;
        }

        public void addInput(ElfModule elfModule) {
            this.inputs.add(elfModule);
        }

        public void addOutput(ElfModule elfModule) {
            this.outputs.add(elfModule);
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElfModule elfModule = (ElfModule) o;
            return Objects.equals(name, elfModule.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        protected SequencedMap<ElfModule, PulseSate> convertToOutputPulseMap(PulseSate pulseSate) {
            return outputs.stream().collect(Collectors.toMap(Function.identity(),
                    (el -> pulseSate),
                    (u, v) -> {
                        throw new IllegalStateException(String.format("Duplicate key %s", u));
                    },
                    LinkedHashMap::new)
            );
        }

        public static final class FlipFlowModule extends ElfModule {
            public boolean tunedOn = false;

            public FlipFlowModule(String name) {
                super(name);
            }

            @Override
            public SequencedMap<ElfModule, PulseSate> process(ElfModule sourceModule, PulseSate pulseSate) {
                if (pulseSate == PulseSate.HIGH) {
                    return new LinkedHashMap<>();
                }

                tunedOn = !tunedOn;
                PulseSate pulseSateAfterPass = !tunedOn ? PulseSate.LOW : PulseSate.HIGH;

                return convertToOutputPulseMap(pulseSateAfterPass);
            }
        }

        public static final class ConjunctionModule extends ElfModule {

            Map<ElfModule, PulseSate> memory = new HashMap<>();

            public ConjunctionModule(String name) {
                super(name);
            }

            @Override
            public SequencedMap<ElfModule, PulseSate> process(ElfModule sourceModule, PulseSate pulseSate) {
                if (sourceModule != null)
                    memory.put(sourceModule, pulseSate);
                PulseSate outputPulse = memory.values().stream().allMatch((el) -> el == PulseSate.HIGH) ? PulseSate.LOW : PulseSate.HIGH;
                return convertToOutputPulseMap(outputPulse);
            }

            @Override
            public void addInput(ElfModule elfModule) {
                super.addInput(elfModule);
                memory.put(elfModule, PulseSate.LOW);
            }

        }

        public static final class NoOpModule extends ElfModule {

            @Override
            public SequencedMap<ElfModule, PulseSate> process(ElfModule sourceModule, PulseSate pulseSate) {

                return convertToOutputPulseMap(pulseSate);
            }

            public NoOpModule(String name) {
                super(name);
            }
        }
    }


    public enum PulseSate {
        LOW, HIGH
    }

}

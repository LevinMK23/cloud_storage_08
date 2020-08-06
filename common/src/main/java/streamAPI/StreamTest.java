package streamAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("./common/src/main/java/streamAPI/file.txt");
        String result = Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(s -> s.length() > 0)
                .collect(Collectors.joining(", "));
        List<String> resultList = Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());
        HashMap<String, Integer> map = Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(s -> s.length() > 0)
                .collect(HashMap::new,
                        (m, s) -> m.put(s, m.getOrDefault(s, 0) + 1),
                        HashMap::putAll);
        System.out.println(map);
        String optional = Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(s -> s.length() > 10)
                .findFirst().orElse("default");
        System.out.println(optional);
        int factorial = IntStream.rangeClosed(1, 4)
                .reduce(1, (a, b) -> a * b);
        System.out.println(factorial);
        int sumLength = Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(s -> s.length() > 0)
                .reduce(0,
                        (acc, s) -> acc + s.length(),
                        Integer::sum);
        System.out.println(sumLength);
    }
}

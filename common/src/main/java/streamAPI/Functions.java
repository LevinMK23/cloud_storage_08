package streamAPI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Functions {
    public static void main(String[] args) {
        Consumer<String> consumer = System.out::println;
        consumer.accept("Hello");
        Predicate<String> predicate = ss -> ss.length() > 3;
        Supplier<HashMap<String, Integer>> supplier = HashMap::new;
        Function<String, String> function = s -> s;
//        IntStream.rangeClosed(1, 10)
//                .filter(value -> value % 2 == 0)
//                .forEach(value -> System.out.print(value + " "));
        Stream.of(1, 2, 3, 4)
                .map(x -> {
                    StringBuilder s = new StringBuilder();
                    for (int i = 0; i < x; i++) {
                        s.append("1");
                    }
                    return s.toString();
                })
                .forEach(x -> System.out.print(x + " "));
    }
}

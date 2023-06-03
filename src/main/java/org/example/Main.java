package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    final static String LETTERS = "RLRFR";
    final static int ROUTE_LENGTH = 100;
    final static int AMOUNT_OF_THREADS = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        generateThread();
        watchMap();

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void generateThread() {
        for (int i = 0; i < AMOUNT_OF_THREADS; i++) {
            new Thread(() -> {
                String rout = generateRoute(LETTERS, ROUTE_LENGTH);
                int amount = (int) rout.chars().filter(s -> s == 'R').count();
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(amount)) {
                        sizeToFreq.put(amount, sizeToFreq.get(amount) + 1);
                    } else {
                        sizeToFreq.put(amount, 1);
                    }
                }
            }).start();
        }
    }

    public static void watchMap() {
        Map.Entry<Integer, Integer> maxSize = sizeToFreq.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        System.out.format("Самое частое количество повторений %d (встретилось %d раз(а) )\n", maxSize.getKey(), maxSize.getValue());
        System.out.println("Другие размеры:");
        sizeToFreq.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(map -> System.out.format("- %d ( %d раз(а) )\n", map.getKey(), map.getValue()));
    }
}
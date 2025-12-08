import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SortComparison {
    private static final Map<Character, Integer> SuitRanks = Map.of(
            'H', 100,
            'C', 200,
            'D', 300,
            'S', 400
    );
    private static final Map<String, Integer> cardCache = new HashMap<>();


    static int getCardValue(String card) {
        char lastChar = card.charAt(card.length() - 1);
        int cardSuitValue = SuitRanks.get(lastChar);

        String cardValueString = card.substring(0, card.length() - 1);
        return cardSuitValue + Integer.parseInt(cardValueString);
    }
    static int getStoredCardValue(String card) {
        return cardCache.computeIfAbsent(card, SortComparison::getCardValue);
    }

    static int cardCompare(String card1, String card2) {
        return getStoredCardValue(card2) > getStoredCardValue(card1) ? -1 : 1;
    }

    static ArrayList<String> bubbleSort(ArrayList<String> array) {
        int n = array.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {  // Reduce comparisons each iteration
                String card1 = array.get(j);
                String card2 = array.get(j + 1);
                if (cardCompare(card1, card2) > 0) {
                    array.set(j + 1, card1);
                    array.set(j, card2);
                    swapped = true;
                }
            }
            if (!swapped) break;  // Early termination if no swaps occurred
        }
        return array;
    }

    static ArrayList<String> mergeSort(ArrayList<String> array) {
        // handle base case
        if (array.size() <= 1) {return array;}

        var midpoint = array.size()/2;
        var left = new ArrayList<String>(array.subList(0, midpoint));
        var right = new ArrayList<String>(array.subList(midpoint, array.size()));
        var sortedLeft = mergeSort(left);
        var sortedRight = mergeSort(right);

        return merge(sortedLeft, sortedRight);
    }

    static ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right) {
        var outputArray = new ArrayList<String>();
        var i = 0;
        var j = 0;
        while (i < left.size() && j < right.size()) {
            if (cardCompare(left.get(i), right.get(j)) > 0) {
                outputArray.add(left.get(i));
                i += 1;
            } else {
                outputArray.add(right.get(j));
                j += 1;

            }
        }
        outputArray.addAll(left.subList(i, left.size()));
        outputArray.addAll(right.subList(j, right.size()));

        return outputArray;
    }

    static void sortComparison(String... filePaths) throws IOException {
        var results = new HashMap<String, ArrayList<Double>>();
        results.put("bubbleSort", new ArrayList<Double>());
        results.put("mergeSort", new ArrayList<Double>());

        for (String filePath : filePaths) {
            List<String> ArrayFromFile;
            Path path = Paths.get(filePath);
            try (var reader = Files.newBufferedReader(path)) {
                ArrayFromFile = reader.readAllLines();
            }
            // Verify array
            if (ArrayFromFile.size() <= 1) {
                throw new RuntimeException("Test array retrieval failed");
            }

            recordBenchmark("bubbleSort",
                    results,
                    new Runnable() {
                        @Override
                        public void run() {
                            bubbleSort(new ArrayList<String>(ArrayFromFile));
                        }
                    }
                    );

            recordBenchmark("mergeSort",
                    results,
                    new Runnable() {
                        @Override
                        public void run() {
                            mergeSort(new ArrayList<String>(ArrayFromFile));
                        }
                    }
            );
        }
        IO.println(results);

        // Prepare formatting
        var longestAlgorithmTitle = 0;
        for (String k : results.keySet()) {
            if (k.length() > longestAlgorithmTitle) {
                longestAlgorithmTitle = k.length();
            }
        }

        var parsedTestDataTitles = Arrays.stream(filePaths).map(title -> title.split("\\.")[0].substring(4)).toList();
        var longestTestDataTitle = 0;
        for (String k : parsedTestDataTitles) {
            if (k.length() > longestTestDataTitle) {
                longestTestDataTitle = k.length();
            }
        }
        longestTestDataTitle += 3;

        try (var w = new BufferedWriter(new FileWriter("./sortComparison.csv"))) {
            w.write(" ".repeat(longestAlgorithmTitle) + ",");
            for (String parsedTestDataTitle : parsedTestDataTitles) {
                w.write(parsedTestDataTitle + " ".repeat(longestTestDataTitle - parsedTestDataTitle.length()) + ",");
            }
            w.newLine();

            for (String title : results.keySet()) {
                // Write the algorithm title
                w.write(title + " ".repeat(longestAlgorithmTitle - title.length()) + ",");

                // Write its results
                for (Double res : results.get(title)) {
                    String formattedResult;
                    if (res.toString().length() > longestTestDataTitle) {
                        formattedResult = res.toString().substring(0, longestTestDataTitle);
                    } else {
                        formattedResult = res.toString();
                    }

                    w.write(formattedResult + " ".repeat(longestTestDataTitle - formattedResult.length()) + ",");
                }
                w.newLine();
            }
        }
    }

    static void recordBenchmark(String title, HashMap<String, ArrayList<Double>> results, Runnable func) {
        var bench = new Benchmark(
                func,
               1
        );
        double result = (double)bench.Start() / 1000000;
        results.get(title).add(result);
    }

}
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class SortComparison {
    private static final Map<Character, Integer> SuitRanks = Map.of(
            'H', 100,
            'C', 200,
            'D', 300,
            'S', 400
    );

    static int getCardValue(String card) {
        char[] cardCharArray = card.toCharArray();
        var cardSuitValue = SuitRanks.get(cardCharArray[cardCharArray.length-1]);
        String cardValueString = "";
        cardValueString += cardCharArray[0];
        if (cardCharArray.length == 3) { cardValueString += cardCharArray[1]; }

        return cardSuitValue + Integer.parseInt(cardValueString);
    }

    static int cardCompare(String card1, String card2) {
        return getCardValue(card2) > getCardValue(card1) ? -1 : 1;
    }

    static ArrayList<String> bubbleSort(ArrayList<String> array) {
        for (int i = 0; i < array.size()-1; i++) {
            for (int j = 0; j < array.size()-1; j++) {
                String card1 = array.get(j);
                String card2 = array.get(j + 1);
                if (cardCompare(card1,card2)>0) {
                    array.set(j+1, card1);
                    array.set(j, card2);
                }
            }
        }
        return array;
    }
//
//    static ArrayList<String> mergeSort(ArrayList<String> array) {
//
//    }

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

        }
        IO.println(results);
    }

    static void recordBenchmark(String title, HashMap<String, ArrayList<Double>> results, Runnable func) {
        var bench = new Benchmark(
                func,
               2
        );
        double result = (double)bench.Start() / 1000000;
        results.get(title).add(result);
    }

}
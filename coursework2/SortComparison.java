import java.util.Map;

public class SortComparison {
    private final Map<String, Integer> SuitRanks = Map.of(
            "H", 1,
            "C", 2,
            "D", 3,
            "S", 4
    );

    static int cardCompare(String card1, String card2) {
        var card1Map = this.SuitRanks[card1[0]];
    }

//    static ArrayList<String> bubbleSort(ArrayList<String> array) {
//    }
//
//    static ArrayList<String> mergeSort(ArrayList<String> array) {
//
//    }
}
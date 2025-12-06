import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Testing {
    public static void main() throws IOException {
        IO.println(SortComparison.cardCompare("1S", "12H"));
        IO.println(SortComparison.bubbleSort(new ArrayList<String>(List.of("4H", "3S", "7S", "8C", "2D", "3H"))));

        SortComparison.sortComparison("./sort10.txt", "./sort100.txt", "./sort10000.txt");
    }
}

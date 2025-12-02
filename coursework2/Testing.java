import java.util.ArrayList;
import java.util.List;

public class Testing {
    public static void main() {
        IO.println(SortComparison.cardCompare("1S", "12H"));
        IO.println(SortComparison.bubbleSort(new ArrayList<String>(List.of("4H", "3S", "7S", "8C", "2D", "3H"))));
    }
}

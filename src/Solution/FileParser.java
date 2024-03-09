package Solution;

import Solution.records.ResponseDateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Stream;

/*  The main goal of this algorithm is fast navigation between different services and their questions.
    It's realized using special identifiers (which may be compared with unique hashcodes) for each service and question.

    For example, we have line C 1.1 8.15.1 P 15.10.2012 83
        Here, the identifier for service is 11 (by service and variation ids), which we add (if not exist) to the map.
        As a value in the map is one more map (key -> question identifier, value -> TreeSet with Nodes (response type, date, time of response).

    All features of this algorithm we can see in queries.
        For the query D 1.1 8.15.1 P 15.10.2012 83 we can in O(1) time-complexity find all Nodes related to this service and question.
        Because we only need to find all existing questions for service with identifier (key) 11, and then all Nodes for questionIdentifier (key) 8151. For HashMap it needs O(1) time.
        With the same time-complexity, we can find all Nodes for queries with * and only main categories.

        When all Nodes are found we filter them by date and response type. As dates are already sorted it's more quick to filter them.
        And for more fast filtering we use parallelStream also.

    In conclusion, time-complexity for each query is O(n) - it happens in the worst scenario when all lines have the same service and category or when we use * *  in the query

    Alternative way to implement is using arrays, eg [1][3] for service and [5][7][2] for questions.
 */
public class FileParser {

    private static final Map<Integer, Map<Integer, TreeSet<ResponseDateTime>>> parsingMap = new HashMap<>();
    private static final Parseable queryParser = new QueryParser();
    private static final Parseable waitingTimeLineParser = new WaitingTimeLineParser();

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            System.out.print("Please, enter filename you want to parse: ");
            String filename = reader.readLine();
            parseFile(filename);
        } catch (IOException e) {
            System.out.println("Shit happens, try different filename: " + e.getLocalizedMessage());
        }
    }

    public static void parseFile(String filename) {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            lines.forEach(FileParser::parseLine);
        } catch (IOException ex) {
            System.out.println("Shit happens during parsing: " + ex.getLocalizedMessage());
        }
    }

    private static void parseLine(String line) {
        String[] arr = line.split("\\s+");

        if (arr.length <= 1) return;

        if ("C".equals(arr[0])) {
            waitingTimeLineParser.parse(arr);
        } else if ("D".equals(arr[0])) {
            queryParser.parse(arr);
        }
    }

    public static Map<Integer, Map<Integer, TreeSet<ResponseDateTime>>> getParsingMap() {
        return parsingMap;
    }
}

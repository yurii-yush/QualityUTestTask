package Solution;

import Solution.records.Node;
import Solution.records.ResponseDateTime;

import java.time.LocalDate;
import java.util.*;

public class WaitingTimeLineParser implements Parseable {

    private final UniqueIdentifierGenerator uniqueIdentifierGenerator = new UniqueIdentifierGenerator();
    private static final Map<Integer, Map<Integer, TreeSet<ResponseDateTime>>> parsingMap = FileParser.getParsingMap();

    @Override
    public void parse(String[] arguments) {
        String[] service = arguments[1].split("\\.");
        String[] question = arguments[2].split("\\.");

        int serviceHash = uniqueIdentifierGenerator.getServiceIdentifier(service);
        int questionHash = uniqueIdentifierGenerator.getQuestionIdentifier(question);

        ResponseDateTime responseDateTime = mapArrayToNode(arguments);
        Node node = new Node(serviceHash, questionHash, responseDateTime);

        addNodeToMap(node);
    }

    private void addNodeToMap(Node node) {
        boolean isServiceHashInMap = parsingMap.containsKey(node.serviceIdentifier());

        if (isServiceHashInMap) {
            Map<Integer, TreeSet<ResponseDateTime>> questionsMap = parsingMap.get(node.serviceIdentifier());
            boolean isQuestionHashInMap = questionsMap.containsKey(node.questionIdentifier());
            if (isQuestionHashInMap) {
                questionsMap.get(node.questionIdentifier()).add(node.responseDateTime());
            } else {
                TreeSet<ResponseDateTime> responseDateTimeSet = new TreeSet<>();
                responseDateTimeSet.add(node.responseDateTime());
                questionsMap.put(node.questionIdentifier(), responseDateTimeSet);
            }
        } else {
            TreeSet<ResponseDateTime> responseDateTimeSet = new TreeSet<>();
            responseDateTimeSet.add(node.responseDateTime());

            Map<Integer, TreeSet<ResponseDateTime>> questionsMap = new HashMap<>();
            questionsMap.put(node.questionIdentifier(), responseDateTimeSet);
            parsingMap.put(node.serviceIdentifier(), questionsMap);
        }
    }

    private  ResponseDateTime mapArrayToNode(String[] arr) {
        LocalDate date = getLocalDateFromString(arr[4]);
        int time = Integer.parseInt(arr[5]);

        return new ResponseDateTime(isFirstResponse(arr[3]), date, time);
    }

}

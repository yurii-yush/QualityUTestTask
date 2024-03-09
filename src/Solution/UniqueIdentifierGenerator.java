package Solution;

import Solution.records.ResponseDateTime;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class UniqueIdentifierGenerator {
    private static final int serviceIdentifierRange = 10;
    private static final int questionTypeIdentifierRange = 1000;
    private static final int questionCategoryIdentifierRange = 10;

    private static final Map<Integer, Map<Integer, TreeSet<ResponseDateTime>>> parsingMap = FileParser.getParsingMap();

    public int getQuestionIdentifier(String[] question) {
        int identifier = Integer.parseInt(question[0]) * questionTypeIdentifierRange;
        if (question.length == 2 || question.length == 3) {
            identifier += Integer.parseInt(question[1]) * questionCategoryIdentifierRange;
        }
        if (question.length == 3) {
            identifier += Integer.parseInt(question[2]);
        }

        return identifier;
    }

    public int getServiceIdentifier(String[] service) {
        int identifier = Integer.parseInt(service[0]) * serviceIdentifierRange;
        if (service.length == 2) {
            identifier += Integer.parseInt(service[1]);
        }

        return identifier;
    }

    public Set<Integer> getQuestionsIdentifiersFromQuery(String query, int serviceHashCode) {
        if ("*".equals(query)) {
            return parsingMap.get(serviceHashCode).keySet();
        }

        String[] arr = query.split("\\.");
        int fromIdentifier = 0, toIdentifier = 0;
        int questionId = Integer.parseInt(arr[0]);
        if (arr.length == 1) {
            fromIdentifier = questionId * questionTypeIdentifierRange;
            toIdentifier = (questionId + 1) * questionTypeIdentifierRange;
        }
        if (arr.length == 2) {
            int categoryId = Integer.parseInt(arr[1]);
            fromIdentifier = (questionId * questionTypeIdentifierRange) + (categoryId * questionCategoryIdentifierRange);
            toIdentifier = fromIdentifier + 9;
        }
        if (arr.length == 3) {
            int categoryId = Integer.parseInt(arr[1]);
            int subcategoryId = Integer.parseInt(arr[2]);
            fromIdentifier = (questionId * questionTypeIdentifierRange) + (categoryId * questionCategoryIdentifierRange) + subcategoryId;
            toIdentifier = fromIdentifier;
        }

        return getQuestionsIdentifiersByRange(serviceHashCode, fromIdentifier, toIdentifier);

    }

    public Set<Integer> getServiceIdentifiersFromQuery(String query) {
        if ("*".equals(query)) {
            return parsingMap.keySet();
        } else {
            String[] ids = query.split("\\.");
            int serviceId = Integer.parseInt(ids[0]);
            int fromIdentifier = serviceId * serviceIdentifierRange;
            int toIdentifier = (serviceId + 1) * serviceIdentifierRange;

            if (ids.length == 2) {
                int variationId = Integer.parseInt(ids[1]);
                toIdentifier = fromIdentifier + variationId;
            }

            return getServiceIdentifiersByRange(fromIdentifier, toIdentifier);
        }
    }

    private Set<Integer> getQuestionsIdentifiersByRange(int serviceHashCode, int fromIdentifier, int toIdentifier) {
        return parsingMap.get(serviceHashCode)
                .keySet()
                .stream()
                .filter(key -> key >= fromIdentifier && key <= toIdentifier)
                .collect(Collectors.toSet());
    }

    private Set<Integer> getServiceIdentifiersByRange(int fromIdentifier, int toIdentifier) {
        return parsingMap.keySet()
                .stream().filter(key -> key >= fromIdentifier && key <= toIdentifier)
                .collect(Collectors.toSet());
    }

}

package Solution;

import Solution.records.ResponseDateTime;

import java.time.LocalDate;
import java.util.*;

public class QueryParser implements Parseable {
    private static final Map<Integer, Map<Integer, TreeSet<ResponseDateTime>>> parsingMap = FileParser.getParsingMap();
    private final UniqueIdentifierGenerator uniqueIdentifierGenerator = new UniqueIdentifierGenerator();

    @Override
    public void parse(String[] arguments) {
        Set<ResponseDateTime> resultSet = getResponseDateTimeSetForQuery(arguments);
        String[] dates = arguments[4].split("-");
        boolean isFirstResponse = isFirstResponse(arguments[3]);
        LocalDate fromDate = getLocalDateFromString(dates[0]);

        LocalDate toDate = dates.length == 1 ? fromDate : getLocalDateFromString(dates[1]);

        double result = resultSet.parallelStream()
                .filter(i -> i.date().isAfter(fromDate) && i.date().isBefore(toDate))
                .filter(i -> i.isFirstResponse() == isFirstResponse)
                .mapToLong(ResponseDateTime::time)
                .average()
                .orElse(0.0);

        System.out.println(convertDoubleToString(result));
    }

    private String convertDoubleToString(double result) {
        if (result==0) return "-";

        return String.valueOf(Math.round(result));
    }

    private Set<ResponseDateTime> getResponseDateTimeSetForQuery(String[] arr) {
        Set<Integer> serviceHashCodes = uniqueIdentifierGenerator.getServiceIdentifiersFromQuery(arr[1]);
        Set<ResponseDateTime> resultSet = new HashSet<>();

        serviceHashCodes
                .forEach(serviceCode ->
                        resultSet.addAll(getResponseDateTimeSetForServiceHashCodeAndQuestion(serviceCode, arr)));

        return resultSet;
    }

    private Set<ResponseDateTime> getResponseDateTimeSetForServiceHashCodeAndQuestion(int serviceHashCode, String[] arr) {
        if (!parsingMap.containsKey(serviceHashCode)) {
            return Collections.EMPTY_SET;
        }
        Set<Integer> questionHashCodeSet = uniqueIdentifierGenerator.getQuestionsIdentifiersFromQuery(arr[2], serviceHashCode);
        Set<ResponseDateTime> resultSet = new HashSet<>();
        Map<Integer, TreeSet<ResponseDateTime>> serviceMap = parsingMap.get(serviceHashCode);
        questionHashCodeSet.forEach(key -> resultSet.addAll(serviceMap.get(key)));

        return resultSet;
    }
}

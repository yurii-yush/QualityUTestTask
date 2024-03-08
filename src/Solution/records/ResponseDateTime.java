package Solution.records;

import java.time.LocalDate;

public record ResponseDateTime(boolean isFirstResponse, LocalDate date, long time) implements Comparable<ResponseDateTime>{
    @Override
    public int compareTo(ResponseDateTime o) {
        return date.compareTo(o.date);
    }
}

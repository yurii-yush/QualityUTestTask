package Solution;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface Parseable {

    void parse(String[] arguments);

    default LocalDate getLocalDateFromString(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("d.M.yyyy", Locale.GERMANY));

    }

    default boolean isFirstResponse(String s) {
        return "P".equals(s);
    }

}

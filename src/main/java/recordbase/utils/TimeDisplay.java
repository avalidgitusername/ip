package recordbase.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Formats dates and elapsed time for concise, human-readable task output. */
public final class TimeDisplay {
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("d MMM uuuu, h:mm a");

    private TimeDisplay() { }

    /** Formats a date and time without exposing the storage-oriented ISO representation. */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT);
    }

    /** Describes the calendar duration from {@code start} to {@code end}. */
    public static String formatDuration(LocalDateTime start, LocalDateTime end) {
        Period period = Period.between(start.toLocalDate(), end.toLocalDate());
        LocalDateTime anchor = start.plus(period);
        if (anchor.isAfter(end)) {
            period = period.minusDays(1);
            anchor = start.plus(period);
        }
        Duration remainder = Duration.between(anchor, end);
        return joinParts(period.getYears(), period.getMonths(), period.getDays(),
                remainder.toHours(), remainder.toMinutesPart());
    }

    /** Describes how long remains before a deadline, including overdue deadlines. */
    public static String formatDeadlineStatus(LocalDateTime deadline) {
        LocalDateTime now = LocalDateTime.now();
        boolean overdue = deadline.isBefore(now);
        String duration = overdue ? formatDuration(deadline, now) : formatDuration(now, deadline);
        return overdue ? "overdue by " + duration : "due in " + duration;
    }

    private static String joinParts(int years, int months, int days, long hours, int minutes) {
        List<String> parts = new ArrayList<>();
        addPart(parts, years, "year");
        addPart(parts, months, "month");
        addPart(parts, days, "day");
        addPart(parts, hours, "hour");
        addPart(parts, minutes, "minute");
        return parts.isEmpty() ? "less than a minute" : String.join(" ", parts);
    }

    private static void addPart(List<String> parts, long value, String unit) {
        if (value != 0) {
            parts.add(value + " " + unit + (value == 1 ? "" : "s"));
        }
    }
}

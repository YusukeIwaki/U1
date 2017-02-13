package io.github.yusukeiwaki.u1.calendar;

import com.annimon.stream.Stream;
import java.util.List;
import org.threeten.bp.LocalDate;

public class CalendarDayListHelper {

  private final LocalDate localDate;

  public CalendarDayListHelper(int year, int month) {
    this(LocalDate.of(year, month, 1));
  }

  private CalendarDayListHelper(LocalDate localDate) {
    this.localDate = localDate;
  }

  public List<CalendarDay> getDayList() {
    final int dayOfWeek1 = localDate.getDayOfWeek().getValue() % 7; //日曜日は0にしちゃう。

    final int numDaysOfLastMonth = localDate.minusMonths(1).lengthOfMonth();
    final int numDaysOfThisMonth = localDate.lengthOfMonth();

    final int dayOfWeekNextMonth1 = (dayOfWeek1 + numDaysOfThisMonth) % 7;

    Stream<CalendarDay> s = Stream.concat(
        Stream.range(0, dayOfWeek1)
            .map(dow -> numDaysOfLastMonth - dayOfWeek1 + 1 + dow)
            .map(day -> CalendarDay.create(CalendarDay.TYPE_OUT_OF_MONTH, day)),
        Stream.range(0, numDaysOfThisMonth)
            .map(dayIndex -> dayIndex + 1)
            .map(day -> CalendarDay.create(CalendarDay.TYPE_IN_MONTH, day)));

    if (dayOfWeekNextMonth1 == 0) {
      return s.toList();
    } else {
      return Stream.concat(s, Stream.range(dayOfWeekNextMonth1, 7)
          .indexed()
          .map(pair -> pair.getFirst() + 1)
          .map(day -> CalendarDay.create(CalendarDay.TYPE_OUT_OF_MONTH, day)))
          .toList();
    }

  }
}

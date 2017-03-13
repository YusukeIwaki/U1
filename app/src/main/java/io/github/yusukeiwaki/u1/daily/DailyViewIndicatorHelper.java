package io.github.yusukeiwaki.u1.daily;

import io.github.yusukeiwaki.u1.CalendarConfig;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

public class DailyViewIndicatorHelper {
  private final LocalDate base = CalendarConfig.START_DATE;

  public int getCount() {
    return (int) ChronoUnit.DAYS.between(base, base.plusMonths(CalendarConfig.DURATION_MONTHS));
  }

  public int getPositionForLocalDate(LocalDate localDate) {
    int index = (int) ChronoUnit.DAYS.between(base, localDate);
    return Math.max(0, index);
  }

  public LocalDate getLocalDateForPosition(int position) {
    return base.plusDays(position);
  }
}

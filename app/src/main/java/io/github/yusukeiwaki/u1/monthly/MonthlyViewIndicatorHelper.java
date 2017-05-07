package io.github.yusukeiwaki.u1.monthly;

import io.github.yusukeiwaki.u1.CalendarConfig;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

public class MonthlyViewIndicatorHelper {
  private final LocalDate base = CalendarConfig.START_DATE;

  public int getCount() {
    return CalendarConfig.DURATION_MONTHS;
  }

  public int getPositionForLocalDate(LocalDate localDate) {
    Period period = Period.between(base, localDate);
    int index = (int) period.toTotalMonths();
    return Math.max(0, index);
  }

  public LocalDate getLocalDateForPosition(int position) {
    return base.plusMonths(position);
  }
}

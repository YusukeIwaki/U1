package io.github.yusukeiwaki.u1.calendar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

public class CalendarIndicatorHelper {
  private final LocalDate base;

  public CalendarIndicatorHelper(LocalDate base) {
    this.base = base;
  }

  public int getPositionForLocalDate(LocalDate localDate) {
    Period period = Period.between(base, LocalDate.now());
    int index = period.getYears() * 12 + period.getMonths();
    return Math.max(0, index);
  }

  public LocalDate getLocalDateForPosition(int position) {
    return base.plusMonths(position);
  }
}

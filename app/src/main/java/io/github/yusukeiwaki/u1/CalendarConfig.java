package io.github.yusukeiwaki.u1;

import org.threeten.bp.LocalDate;

public interface CalendarConfig {
  LocalDate START_DATE = LocalDate.of(2016, 9, 1);
  int DURATION_MONTHS = 72; // 6 years.
}

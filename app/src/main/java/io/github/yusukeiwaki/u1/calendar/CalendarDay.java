package io.github.yusukeiwaki.u1.calendar;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class CalendarDay {
  public static final int TYPE_OUT_OF_MONTH = 0;
  public static final int TYPE_IN_MONTH = 1;

  abstract int type();

  abstract int day();

  static CalendarDay create(int type, int day) {
    return new AutoValue_CalendarDay(type, day);
  }
}

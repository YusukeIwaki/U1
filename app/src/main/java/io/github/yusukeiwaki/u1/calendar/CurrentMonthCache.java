package io.github.yusukeiwaki.u1.calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentMonthCache {
  public static final String KEY_YEAR = "year";
  public static final String KEY_MONTH = "month";

  public static SharedPreferences get(Context context) {
    return context.getSharedPreferences("current_month", Context.MODE_PRIVATE);
  }

  public static void updateYearAndMonth(Context context, int year, int month) {
    SharedPreferences currentMonthCache = get(context);
    if (currentMonthCache.getInt(CurrentMonthCache.KEY_YEAR, 0) != year ||
        currentMonthCache.getInt(CurrentMonthCache.KEY_MONTH, 0) != month) {
      currentMonthCache.edit()
          .putInt(CurrentMonthCache.KEY_YEAR, year)
          .putInt(CurrentMonthCache.KEY_MONTH, month)
          .apply();
    }
  }
}

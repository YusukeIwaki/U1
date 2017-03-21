package io.github.yusukeiwaki.u1.monthly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.daily.DailyViewActivity;
import java.util.HashMap;

import static io.github.yusukeiwaki.u1.monthly.CurrentMonthCache.updateYearAndMonth;

public class MonthlyViewFragment extends AbstractFragment {
  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";

  public static MonthlyViewFragment newInstance(int year, int month) {
    Bundle args = new Bundle();
    args.putInt(KEY_YEAR, year);
    args.putInt(KEY_MONTH, month);

    MonthlyViewFragment f = new MonthlyViewFragment();
    f.setArguments(args);
    return f;
  }

  private int year;
  private int month;
  private HashMap<Integer, CalendarDayView> calendarViewMap;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();
    year = args.getInt(KEY_YEAR);
    month = args.getInt(KEY_MONTH);
  }

  @Override protected int getLayout() {
    return R.layout.fragment_calendar;
  }

  @Override protected void onSetupView() {
    CalendarLayout layout = (CalendarLayout) rootView.findViewById(R.id.calendar);
    layout.setOnItemClickListener(day -> {
      if (day.type() == CalendarDay.TYPE_IN_MONTH) {
        showDayAt(year, month, day.day());
      } else {
        if (day.day() <= 7) {
          showNextMonth();
        } else {
          showPrevMonth();
        }
      }
    });

    calendarViewMap = new HashMap<>();
    CalendarDayListHelper helper = new CalendarDayListHelper(year, month);
    for (CalendarDay day : helper.getDayList()) {
      switch (day.type()) {
        case CalendarDay.TYPE_IN_MONTH:
          CalendarDayView calendarDayView = new CalendarDayView(getContext(), year, month, day.day());
          layout.addDayWithView(day, calendarDayView);
          calendarViewMap.put(day.day(), calendarDayView);
          break;
        case CalendarDay.TYPE_OUT_OF_MONTH:
          layout.addDay(day);
      }
    }
  }

  @Override public void onDestroyView() {
    for (CalendarDayView calendarDayView : calendarViewMap.values()) calendarDayView.cleanup();
    super.onDestroyView();
  }

  private void showDayAt(int year, int month, int day) {
    getContext().startActivity(DailyViewActivity.newIntent(getContext(), year, month, day));
  }

  private void showPrevMonth() {
    if (month == 1) {
      updateYearAndMonth(getContext(), year - 1, 12);
    } else {
      updateYearAndMonth(getContext(), year, month - 1);
    }
  }

  private void showNextMonth() {
    if (month == 12) {
      updateYearAndMonth(getContext(), year + 1, 1);
    } else {
      updateYearAndMonth(getContext(), year, month + 1);
    }
  }
}

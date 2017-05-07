package io.github.yusukeiwaki.u1.monthly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.daily.DailyViewActivity;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import java.util.HashMap;
import org.threeten.bp.LocalDate;

import static io.github.yusukeiwaki.u1.monthly.CurrentMonthCache.updateYearAndMonth;

public class MonthlyViewFragment extends AbstractFragment {
  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";
  private FirebaseMonthlyLifeEventManager firebaseArrayManager;

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
    firebaseArrayManager = new FirebaseMonthlyLifeEventManager(year, month);
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
          CalendarDayView calendarDayView = new CalendarDayView(getContext());
          layout.addDayWithView(day, calendarDayView);
          calendarViewMap.put(day.day(), calendarDayView);
          break;
        case CalendarDay.TYPE_OUT_OF_MONTH:
          layout.addDay(day);
      }
    }

    LocalDate now = LocalDate.now();
    if (year == now.getYear() && month == now.getMonthValue()) {
      View parent = (View) calendarViewMap.get(now.getDayOfMonth()).getParent();
      parent.setBackgroundResource(R.drawable.calendar_today_background);
    }

    firebaseArrayManager.setCallback(new FirebaseMonthlyLifeEventManager.Callback() {
      @Override public void onLifeEventAdded(int dayOfMonth, int index, LifeEvent lifeEvent) {
        calendarViewMap.get(dayOfMonth).onLifeEventAdded(index, lifeEvent);
      }

      @Override public void onLifeEventChanged(int dayOfMonth, int index, LifeEvent lifeEvent) {
        calendarViewMap.get(dayOfMonth).onLifeEventChanged(index, lifeEvent);
      }

      @Override public void onLifeEventRemoved(int dayOfMonth, int index) {
        calendarViewMap.get(dayOfMonth).onLifeEventRemoved(index);
      }
    });
  }

  @Override public void onDestroyView() {
    firebaseArrayManager.cleanup();
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

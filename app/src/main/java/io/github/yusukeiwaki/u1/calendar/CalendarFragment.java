package io.github.yusukeiwaki.u1.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.daily.DailyViewActivity;

import static io.github.yusukeiwaki.u1.calendar.CurrentMonthCache.updateYearAndMonth;

public class CalendarFragment extends AbstractFragment {
  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";

  public static CalendarFragment newInstance(int year, int month) {
    Bundle args = new Bundle();
    args.putInt(KEY_YEAR, year);
    args.putInt(KEY_MONTH, month);

    CalendarFragment f = new CalendarFragment();
    f.setArguments(args);
    return f;
  }

  private int year;
  private int month;

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
    RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 7);

    CalendarDayAdapter adapter = new CalendarDayAdapter(year, month);
    adapter.setOnItemClickListener(day -> {
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

    RecyclerView recyclerView = (RecyclerView) rootView;
    recyclerView.setLayoutManager(lm);
    recyclerView.setAdapter(adapter);
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

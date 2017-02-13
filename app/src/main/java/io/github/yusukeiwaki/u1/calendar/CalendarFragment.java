package io.github.yusukeiwaki.u1.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;

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
    Log.d("hoge", "year/month = "+ year+ "/" + month);
  }

  @Override public int getLayout() {
    return R.layout.fragment_calendar;
  }

  @Override public void onSetupView() {
    RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 7);

    RecyclerView recyclerView = (RecyclerView) rootView;
    recyclerView.setLayoutManager(lm);
    recyclerView.setAdapter(new CalendarDayAdapter(year, month));
  }
}

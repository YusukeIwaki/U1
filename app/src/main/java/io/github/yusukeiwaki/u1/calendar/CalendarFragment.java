package io.github.yusukeiwaki.u1.calendar;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;

public class CalendarFragment extends AbstractFragment {
  @Override public int getLayout() {
    return R.layout.fragment_calendar;
  }

  @Override public void onSetupView() {
    RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 7);

    RecyclerView recyclerView = (RecyclerView) rootView;
    recyclerView.setLayoutManager(lm);
    recyclerView.setAdapter(new CalendarDayAdapter(2016, 11));
  }
}

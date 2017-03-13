package io.github.yusukeiwaki.u1.daily;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDate;

public class DailyViewFragment extends AbstractFragment {
  private int year;
  private int month;
  private int day;

  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";
  private static final String KEY_DAY = "day";

  private DailyLifeEventRecyclerViewAdapter dailyLifeEventRecyclerViewAdapter;

  public static DailyViewFragment newInstance(int year, int month, int day) {
    Bundle args = new Bundle();
    args.putInt(KEY_YEAR, year);
    args.putInt(KEY_MONTH, month);
    args.putInt(KEY_DAY, day);

    DailyViewFragment fragment = new DailyViewFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int getLayout() {
    return R.layout.fragment_daily_view;
  }

  @Override protected void onSetupView() {
    Bundle args = getArguments();
    LocalDate dateNow = LocalDate.now();
    year = args.getInt(KEY_YEAR, dateNow.getYear());
    month = args.getInt(KEY_MONTH, dateNow.getMonthValue());
    day = args.getInt(KEY_DAY, dateNow.getDayOfMonth());

    setupLifeEventRecyclerView();

    rootView.findViewById(R.id.fab_add).setOnClickListener(view -> {
      FragmentManager fm = getFragmentManager();
      String tag = AddLifeEventDialogFragment.class.getSimpleName();

      if (fm.findFragmentByTag(tag) == null) {
        AddLifeEventDialogFragment.newInstance(year, month, day).show(fm, tag);
      }
    });
  }

  private void setupLifeEventRecyclerView() {
    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.lifeevent_recyclerview);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    dailyLifeEventRecyclerViewAdapter = new DailyLifeEventRecyclerViewAdapter(year, month, day);
    dailyLifeEventRecyclerViewAdapter.setOnClickListener((key, lifeEvent) -> {
      FragmentManager fm = getFragmentManager();
      String tag = EditLifeEventDialogFragment.class.getSimpleName();

      EditLifeEventDialogFragment.newInstance(key, lifeEvent).show(fm, tag);
    });
    dailyLifeEventRecyclerViewAdapter.setOnLongClickListener((key, lifeEvent) -> {
      FragmentManager fm = getFragmentManager();
      String tag = EditLifeEventDialogFragment.class.getSimpleName();

      DeleteLifeEventDialogFragment.newInstance(key).show(fm, tag);
      return true;
    });
    recyclerView.setAdapter(dailyLifeEventRecyclerViewAdapter);
  }

  @Override public void onDestroyView() {
    dailyLifeEventRecyclerViewAdapter.cleanup();
    super.onDestroyView();
  }
}

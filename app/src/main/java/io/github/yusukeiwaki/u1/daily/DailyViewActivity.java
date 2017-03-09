package io.github.yusukeiwaki.u1.daily;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDate;

public class DailyViewActivity extends AppCompatActivity {
  private int year;
  private int month;
  private int day;

  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";
  private static final String KEY_DAY = "day";

  private DailyLifeEventRecyclerViewAdapter dailyLifeEventRecyclerViewAdapter;

  public static Intent newIntent(Context context, int year, int month, int day) {
    Intent intent = new Intent(context, DailyViewActivity.class);
    intent.putExtra(KEY_YEAR, year);
    intent.putExtra(KEY_MONTH, month);
    intent.putExtra(KEY_DAY, day);
    return intent;
  }


  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_daily_view);

    Intent intent = getIntent();
    LocalDate dateNow = LocalDate.now();
    year = intent.getIntExtra(KEY_YEAR, dateNow.getYear());
    month = intent.getIntExtra(KEY_MONTH, dateNow.getMonthValue());
    day = intent.getIntExtra(KEY_DAY, dateNow.getDayOfMonth());
    setToolbarTitle(String.format("%d年 %d月%d日", year, month, day));

    setupLifeEventRecyclerView();

    findViewById(R.id.fab_add).setOnClickListener(view -> {
      FragmentManager fm = getSupportFragmentManager();
      String tag = AddLifeEventDialogFragment.class.getSimpleName();

      if (fm.findFragmentByTag(tag) == null) {
        AddLifeEventDialogFragment.newInstance(year, month, day).show(fm, tag);
      }
    });
  }

  private void setToolbarTitle(String title) {
    getSupportActionBar().setTitle(title);
  }

  private void setupLifeEventRecyclerView() {
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lifeevent_recyclerview);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    dailyLifeEventRecyclerViewAdapter = new DailyLifeEventRecyclerViewAdapter(year, month, day);
    dailyLifeEventRecyclerViewAdapter.setOnClickListener((key, lifeEvent) -> {
      FragmentManager fm = getSupportFragmentManager();
      String tag = EditLifeEventDialogFragment.class.getSimpleName();

      EditLifeEventDialogFragment.newInstance(key, lifeEvent).show(fm, tag);
    });
    dailyLifeEventRecyclerViewAdapter.setOnLongClickListener((key, lifeEvent) -> {
      FragmentManager fm = getSupportFragmentManager();
      String tag = EditLifeEventDialogFragment.class.getSimpleName();

      DeleteLifeEventDialogFragment.newInstance(key).show(fm, tag);
      return true;
    });
    recyclerView.setAdapter(dailyLifeEventRecyclerViewAdapter);
  }

  @Override protected void onDestroy() {
    dailyLifeEventRecyclerViewAdapter.cleanup();
    super.onDestroy();
  }
}

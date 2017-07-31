package io.github.yusukeiwaki.u1.daily;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDate;

public class DailyViewActivity extends AppCompatActivity {
  private LocalDate localDate;

  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";
  private static final String KEY_DAY = "day";

  public static Intent newIntent(Context context, int year, int month, int day) {
    Intent intent = new Intent(context, DailyViewActivity.class);
    intent.putExtra(KEY_YEAR, year);
    intent.putExtra(KEY_MONTH, month);
    intent.putExtra(KEY_DAY, day);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_daily_view);

    Intent intent = getIntent();
    LocalDate dateNow = LocalDate.now();
    int year = intent.getIntExtra(KEY_YEAR, dateNow.getYear());
    int month = intent.getIntExtra(KEY_MONTH, dateNow.getMonthValue());
    int day = intent.getIntExtra(KEY_DAY, dateNow.getDayOfMonth());
    localDate = LocalDate.of(year, month, day);

    setupViewPager();
  }

  private void setupViewPager() {
    final DailyViewIndicatorHelper indicatorHelper = new DailyViewIndicatorHelper();

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override public int getCount() {
        return indicatorHelper.getCount();
      }

      @Override public Fragment getItem(int position) {
        LocalDate date = indicatorHelper.getLocalDateForPosition(position);
        return DailyViewFragment.newInstance(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
      }
    });

    viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        LocalDate date = indicatorHelper.getLocalDateForPosition(position);
        final int year = date.getYear();
        final int month = date.getMonthValue();
        final int day = date.getDayOfMonth();

        getSupportActionBar().setTitle(String.format("%d年 %d月%d日", year, month, day));
      }
    });

    viewPager.setCurrentItem(indicatorHelper.getPositionForLocalDate(localDate));
  }

}

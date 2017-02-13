package io.github.yusukeiwaki.u1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import io.github.yusukeiwaki.u1.calendar.CalendarFragment;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupViewPager();
  }

  private static final LocalDate BASE = LocalDate.of(2016, 9, 20);

  private void setupViewPager() {
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override public int getCount() {
        return 72;
      }

      @Override public Fragment getItem(int position) {
        LocalDate date = BASE.plusMonths(position);
        return CalendarFragment.newInstance(date.getYear(), date.getMonthValue());
      }
    });

    Period period = Period.between(BASE, LocalDate.now());
    int index = period.getYears() * 12 + period.getMonths();
    viewPager.setCurrentItem(Math.max(0, index));
  }
}

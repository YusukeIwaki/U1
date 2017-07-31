package io.github.yusukeiwaki.u1.monthly;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.auth.FirebaseSignInDialogFragment;
import io.github.yusukeiwaki.u1.daily.AddLifeEventDialogFragment;
import org.threeten.bp.LocalDate;

public class MonthlyViewActivity extends AppCompatActivity {

  private FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    if (currentUser == null) {
      FragmentManager fm = getSupportFragmentManager();
      String tag = FirebaseSignInDialogFragment.class.getSimpleName();

      if (fm.findFragmentByTag(tag) == null) {
        FirebaseSignInDialogFragment.newInstance().show(fm, tag);
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar_view);

    setupViewPager();
    FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

    findViewById(R.id.fab_add).setOnClickListener(view -> {
      FragmentManager fm = getSupportFragmentManager();
      String tag = AddLifeEventDialogFragment.class.getSimpleName();

      if (fm.findFragmentByTag(tag) == null) {
        LocalDate now = LocalDate.now();
        AddLifeEventDialogFragment.newInstance(now.getYear(), now.getMonthValue(), now.getDayOfMonth()).show(fm, tag);
      }
    });
  }

  private final MonthlyViewIndicatorHelper
      indicatorHelper = new MonthlyViewIndicatorHelper();

  private void setupViewPager() {
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override public int getCount() {
        return indicatorHelper.getCount();
      }

      @Override public Fragment getItem(int position) {
        LocalDate date = indicatorHelper.getLocalDateForPosition(position);
        return MonthlyViewFragment.newInstance(date.getYear(), date.getMonthValue());
      }
    });

    viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        LocalDate date = indicatorHelper.getLocalDateForPosition(position);
        final int year = date.getYear();
        final int month = date.getMonthValue();

        CurrentMonthCache.updateYearAndMonth(MonthlyViewActivity.this, year, month);
        getSupportActionBar().setTitle(String.format("%d年 %d月", year, month));
      }
    });

    setCurrentMonthWithCache(CurrentMonthCache.get(this));
  }

  private void setCurrentMonthWithCache(SharedPreferences prefs) {
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    LocalDate current = LocalDate.now();
    if (prefs.contains(CurrentMonthCache.KEY_YEAR) && prefs.contains(CurrentMonthCache.KEY_MONTH)) {
      current = LocalDate.of(
          prefs.getInt(CurrentMonthCache.KEY_YEAR, current.getYear()),
          prefs.getInt(CurrentMonthCache.KEY_MONTH, current.getMonthValue()),
          1);
    }

    viewPager.setCurrentItem(indicatorHelper.getPositionForLocalDate(current));
  }

  @Override protected void onStart() {
    super.onStart();
    CurrentMonthCache.get(this).registerOnSharedPreferenceChangeListener(currentMonthCacheListener);
  }

  @Override protected void onStop() {
    CurrentMonthCache.get(this).unregisterOnSharedPreferenceChangeListener(currentMonthCacheListener);
    super.onStop();
  }

  SharedPreferences.OnSharedPreferenceChangeListener currentMonthCacheListener = (prefs, key) -> {
    if (CurrentMonthCache.KEY_YEAR.equals(key) || CurrentMonthCache.KEY_MONTH.equals(key)) {
      setCurrentMonthWithCache(prefs);
    }
  };

  @Override protected void onDestroy() {
    FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    super.onDestroy();
  }
}

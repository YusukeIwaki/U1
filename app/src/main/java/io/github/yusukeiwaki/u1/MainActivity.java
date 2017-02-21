package io.github.yusukeiwaki.u1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.github.yusukeiwaki.u1.auth.FirebaseSignInDialogFragment;
import io.github.yusukeiwaki.u1.calendar.CalendarFragment;
import io.github.yusukeiwaki.u1.calendar.CalendarIndicatorHelper;
import org.threeten.bp.LocalDate;

public class MainActivity extends AppCompatActivity {

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
    setContentView(R.layout.activity_main);

    setupViewPager();
    FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
  }

  private final CalendarIndicatorHelper
      indicatorHelper = new CalendarIndicatorHelper(LocalDate.of(2016, 9, 20));

  private void setupViewPager() {
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override public int getCount() {
        return 72;
      }

      @Override public Fragment getItem(int position) {
        LocalDate date = indicatorHelper.getLocalDateForPosition(position);
        return CalendarFragment.newInstance(date.getYear(), date.getMonthValue());
      }
    });

    viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        LocalDate date = indicatorHelper.getLocalDateForPosition(position);
        final int year = date.getYear();
        final int month = date.getMonthValue();

        getSupportActionBar().setTitle(String.format("%d年 %d月", year, month));
      }
    });

    viewPager.setCurrentItem(indicatorHelper.getPositionForLocalDate(LocalDate.now()));
  }

  @Override protected void onDestroy() {
    FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    super.onDestroy();
  }
}

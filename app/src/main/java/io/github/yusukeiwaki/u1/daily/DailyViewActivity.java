package io.github.yusukeiwaki.u1.daily;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDate;

public class DailyViewActivity extends AppCompatActivity {
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


  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_daily_view);

    Intent intent = getIntent();
    LocalDate dateNow = LocalDate.now();
    int year = intent.getIntExtra(KEY_YEAR, dateNow.getYear());
    int month = intent.getIntExtra(KEY_MONTH, dateNow.getMonthValue());
    int day = intent.getIntExtra(KEY_DAY, dateNow.getDayOfMonth());
    setToolbarTitle(String.format("%d年 %d月%d日", year, month, day));
  }

  private void setToolbarTitle(String title) {
    getSupportActionBar().setTitle(title);
  }
}
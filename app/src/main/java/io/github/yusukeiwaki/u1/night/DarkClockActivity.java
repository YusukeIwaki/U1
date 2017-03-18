package io.github.yusukeiwaki.u1.night;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class DarkClockActivity extends AppCompatActivity {

  private NightScreenBrightnessManager nightScreenBrightnessManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    findViewById(android.R.id.content).setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    );

    getWindow().addFlags(
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
    );

    setContentView(R.layout.activity_dark_clock);

    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_TIME_TICK);
    filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    registerReceiver(myReceiver, filter);

    updateCurrentTime();
    initializeScreenDarkness();

    setupBottomSheet();
  }

  @Override protected void onDestroy() {
    unregisterReceiver(myReceiver);
    super.onDestroy();
  }

  private BroadcastReceiver myReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();

      if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        updateBatterGauge(100 * level / scale);
      } else if (Intent.ACTION_TIME_TICK.equals(action)) {
        updateCurrentTime();
      }
    }
  };

  private void updateCurrentTime() {
    LocalDateTime now = LocalDateTime.now();

    ((TextView) findViewById(R.id.date))
        .setText(now.format(DateTimeFormatter.ofPattern("M月d日 (E)")));
    ((TextView) findViewById(R.id.time))
        .setText(now.format(DateTimeFormatter.ofPattern("H:mm")));
  }

  private void updateBatterGauge(int percent) {
    ((TextView) findViewById(R.id.battery_val))
        .setText(Integer.toString(percent) + "%");

    TextView icon = (TextView) findViewById(R.id.battery_icon);
    if (percent < 15) {
      icon.setText(R.string.fa_battery_empty);
    } else if (percent < 50) {
      icon.setText(R.string.fa_battery_quarter);
    } else if (percent < 70) {
      icon.setText(R.string.fa_battery_half);
    } else if (percent < 90) {
      icon.setText(R.string.fa_battery_three_quarters);
    } else {
      icon.setText(R.string.fa_battery_full);
    }
  }

  private void initializeScreenDarkness() {
    int hour = LocalDateTime.now().getHour();
    boolean isNight = hour <= 6 || hour >= 17;
    setActivityDark(isNight);

    nightScreenBrightnessManager = new NightScreenBrightnessManager(this, this::setActivityDark);
  }

  private void setActivityDark(boolean dark) {
    WindowManager.LayoutParams lp = getWindow().getAttributes();
    float brightness = dark ? 0.01f : WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
    lp.screenBrightness = brightness;
    lp.buttonBrightness = brightness;
    getWindow().setAttributes(lp);
  }

  private void setupBottomSheet() {
    BottomSheetBehavior bottomSheet = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
    View captionShowTimer = findViewById(R.id.caption_show_timer);
    View fragment = findViewById(R.id.fragment_junyu_timer);

    bottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {
        captionShowTimer.setVisibility(newState == BottomSheetBehavior.STATE_COLLAPSED ?
            View.VISIBLE : View.GONE);
      }

      @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        fragment.setAlpha(slideOffset);
      }
    });
    fragment.setAlpha(0);
  }

  @Override protected void onResume() {
    super.onResume();
    nightScreenBrightnessManager.enable();
  }

  @Override protected void onPause() {
    nightScreenBrightnessManager.disable();
    super.onPause();
  }
}

package io.github.yusukeiwaki.u1.night;

import android.widget.TextView;
import com.varunest.sparkbutton.SparkButton;
import io.github.yusukeiwaki.u1.widgets.CircularTimerRecorder;

public class TimeRecorderManager {
  private final CircularTimerRecorder timerRecorder;
  private final SparkButton toggleButton;
  private final TextView minuteText;

  public interface OnTimerStartCallback {
    void onTimerStart();
  }
  private OnTimerStartCallback onTimerStartCallback;

  public TimeRecorderManager(SparkButton toggleButton, CircularTimerRecorder timerRecorder, TextView minuteText) {
    this.toggleButton = toggleButton;
    this.timerRecorder = timerRecorder;
    this.minuteText = minuteText;
  }

  public void initialize() {
    toggleButton.setChecked(timerRecorder.isStarted());

    toggleButton.setEventListener((button, isChecked) -> {
      if (isChecked) {
        timerRecorder.startTimer();
        if (onTimerStartCallback != null) {
          onTimerStartCallback.onTimerStart();
        }
      } else {
        timerRecorder.stopTimer();
      }
    });

    timerRecorder.setTimeRecordCallback(minute ->
        minuteText.setText(Integer.toString(minute) + "分"));
  }

  public void setOnTimerStartCallback(OnTimerStartCallback onTimerStartCallback) {
    this.onTimerStartCallback = onTimerStartCallback;
  }

  public void forceStop() {
    toggleButton.setChecked(false);
    timerRecorder.stopTimer();
  }

  public long getElapsedTime() {
    return timerRecorder.getElapsedTime();
  }

  public void reset() {
    toggleButton.setChecked(false);
    timerRecorder.resetTimer();
  }
}

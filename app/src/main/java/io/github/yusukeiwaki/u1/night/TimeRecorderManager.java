package io.github.yusukeiwaki.u1.night;

import android.util.Pair;
import android.widget.TextView;
import com.varunest.sparkbutton.SparkButton;
import io.github.yusukeiwaki.u1.widgets.CircularTimerRecorder;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDateTime;

public class TimeRecorderManager {
  private final CircularTimerRecorder timerRecorder;
  private final SparkButton toggleButton;
  private final TextView minuteText;
  private final ArrayList<Pair<LocalDateTime, LocalDateTime>> timeRangeList = new ArrayList<>();
  private LocalDateTime timeStarted;

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
        timeStarted = LocalDateTime.now();
      } else {
        timerRecorder.stopTimer();
        if (timeStarted != null) {
          timeRangeList.add(new Pair<>(timeStarted, LocalDateTime.now()));
          timeStarted = null;
        }
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
    if (timeStarted != null) {
      timeRangeList.add(new Pair<>(timeStarted, LocalDateTime.now()));
      timeStarted = null;
    }
  }

  public long getElapsedTime() {
    return timerRecorder.getElapsedTime();
  }

  public List<Pair<LocalDateTime, LocalDateTime>> getTimeRangeList() {
    return timeRangeList;
  }

  public void reset() {
    toggleButton.setChecked(false);
    timerRecorder.resetTimer();
    timeRangeList.clear();
  }
}

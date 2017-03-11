package io.github.yusukeiwaki.u1.night;

import android.content.Context;
import android.widget.Toast;
import io.github.yusukeiwaki.u1.daily.FirebaseLifeEventManager;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import java.util.ArrayList;
import org.threeten.bp.LocalDateTime;

public class JunyuTimerManager {
  private final Context context;
  private final TimeRecorderManager recorderLeft;
  private final TimeRecorderManager recorderRight;
  private LocalDateTime timeStarted;

  public JunyuTimerManager(Context context, TimeRecorderManager recorderLeft, TimeRecorderManager recorderRight) {
    this.context = context;
    this.recorderLeft = recorderLeft;
    this.recorderRight = recorderRight;
  }

  public void initialize() {
    recorderLeft.initialize();
    recorderRight.initialize();

    recorderLeft.setOnTimerStartCallback(() -> {
      if (timeStarted == null) timeStarted = LocalDateTime.now();
      recorderRight.forceStop();
    });
    recorderRight.setOnTimerStartCallback(() -> {
      if (timeStarted == null) timeStarted = LocalDateTime.now();
      recorderLeft.forceStop();
    });
  }

  public void uploadLifeEvent() {
    long timeLeft = recorderLeft.getElapsedTime();
    long timeRight = recorderRight.getElapsedTime();

    if (timeStarted == null || timeLeft + timeRight == 0) return;
    upload(createJunyuLifeEvent(timeStarted, timeLeft, timeRight));

    recorderLeft.reset();
    recorderRight.reset();
    timeStarted = null;
  }

  private static LifeEvent createJunyuLifeEvent(LocalDateTime timeStarted, long left, long right) {
    return new LifeEvent(
        timeStarted.toEpochSecond(LifeEvent.getTimeZone()),
        LocalDateTime.now().toEpochSecond(LifeEvent.getTimeZone()),
        new ArrayList<String>() {
          {
            add("授乳");
          }
        },
        String.format("左: %d分%d秒 右: %d分%d秒",
            left / 60, left % 60,
            right / 60, right % 60));
  }

  private void upload(LifeEvent lifeEvent) {
    new FirebaseLifeEventManager().create(lifeEvent);
    Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
  }
}

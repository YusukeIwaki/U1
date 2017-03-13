package io.github.yusukeiwaki.u1.night;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;
import io.github.yusukeiwaki.u1.daily.FirebaseLifeEventManager;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

public class JunyuTimerManager {
  private final Context context;
  private final TimeRecorderManager recorderLeft;
  private final TimeRecorderManager recorderRight;

  public JunyuTimerManager(Context context, TimeRecorderManager recorderLeft, TimeRecorderManager recorderRight) {
    this.context = context;
    this.recorderLeft = recorderLeft;
    this.recorderRight = recorderRight;
  }

  public void initialize() {
    recorderLeft.initialize();
    recorderRight.initialize();

    recorderLeft.setOnTimerStartCallback(() -> {
      recorderRight.forceStop();
    });
    recorderRight.setOnTimerStartCallback(() -> {
      recorderLeft.forceStop();
    });
  }

  public void uploadLifeEvent() {
    recorderLeft.forceStop();
    recorderRight.forceStop();

    LifeEvent lifeEvent = new TimeRecorderAggregator(recorderLeft, recorderRight).createLifeEvent();

    if (lifeEvent != null) {
      upload(lifeEvent);

      recorderLeft.reset();
      recorderRight.reset();
      Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
    }
  }

  private static class TimeRecorderAggregator {
    private final TimeRecorderManager recorderLeft;
    private final TimeRecorderManager recorderRight;
    private List<Pair<Boolean, Pair<LocalDateTime, LocalDateTime>>> mergedTimeRangeList;
    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;

    public TimeRecorderAggregator(TimeRecorderManager recorderLeft, TimeRecorderManager recorderRight) {
      this.recorderLeft = recorderLeft;
      this.recorderRight = recorderRight;
    }

    public LifeEvent createLifeEvent() {
      if (recorderLeft.getElapsedTime() + recorderRight.getElapsedTime() <= 0) return null;

      createMergedTimeRangeList();

      return new LifeEvent(
          getTimeStarted().toEpochSecond(LifeEvent.getTimeZone()),
          getTimeEnded().toEpochSecond(LifeEvent.getTimeZone()),
          new ArrayList<String>() {
            {
              add("授乳");
            }
          },
          getDescription());
    }

    private void createMergedTimeRangeList() {
      mergedTimeRangeList = new ArrayList<>();
      for (Pair<LocalDateTime, LocalDateTime> range : recorderLeft.getTimeRangeList()) {
        mergedTimeRangeList.add(new Pair<>(LEFT, range));
      }
      for (Pair<LocalDateTime, LocalDateTime> range : recorderRight.getTimeRangeList()) {
        mergedTimeRangeList.add(new Pair<>(RIGHT, range));
      }
    }

    private LocalDateTime getTimeStarted() {
      Collections.sort(mergedTimeRangeList, (list1, list2) -> list1.second.first.compareTo(list2.second.first));
      return mergedTimeRangeList.get(0).second.first;
    }

    private LocalDateTime getTimeEnded() {
      Collections.sort(mergedTimeRangeList, (list1, list2) -> list2.second.second.compareTo(list1.second.first));
      return mergedTimeRangeList.get(0).second.second;
    }

    private String getDescription() {
      Collections.sort(mergedTimeRangeList, (list1, list2) -> list1.second.first.compareTo(list2.second.first));
      StringBuilder sb = new StringBuilder();
      for (Pair<Boolean, Pair<LocalDateTime, LocalDateTime>> rangePair : mergedTimeRangeList) {
        String timeRangeString = getTimeRangeString(rangePair.second);
        if (TextUtils.isEmpty(timeRangeString)) continue;

        if (rangePair.first == LEFT) {
          sb.append("左：");
        } else {
          sb.append("右：");
        }
        sb.append(timeRangeString).append("\n");
      }
      return sb.toString().trim();
    }

    private static String getTimeRangeString(Pair<LocalDateTime, LocalDateTime> timeRange) {
      Duration d = Duration.between(timeRange.first, timeRange.second);
      long seconds = d.getSeconds();

      if (seconds <= 0) return null;

      return String.format("%d分%d秒", seconds / 60, seconds % 60);
    }
  }

  private static void upload(LifeEvent lifeEvent) {
    new FirebaseLifeEventManager().create(lifeEvent);
  }
}

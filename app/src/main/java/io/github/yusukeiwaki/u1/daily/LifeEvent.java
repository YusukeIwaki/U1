package io.github.yusukeiwaki.u1.daily;

import java.io.Serializable;
import java.util.List;
import org.threeten.bp.ZoneOffset;

// DO NOT USE Auto.Value for using with Firebase DB.
public class LifeEvent implements Serializable {
  private long timeStart;
  private long timeEnd;
  private List<String> events;
  private String memo;

  public static ZoneOffset getTimeZone() {
    return ZoneOffset.ofHours(9);
  }

  private LifeEvent() {
  }

  public LifeEvent(long timeStart, long timeEnd, List<String> events, String memo) {
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.events = events;
    this.memo = memo;
  }

  public long getTimeStart() {
    return timeStart;
  }

  public long getTimeEnd() {
    return timeEnd;
  }

  public List<String> getEvents() {
    return events;
  }

  public String getMemo() {
    return memo;
  }
}

package io.github.yusukeiwaki.u1.daily;

import java.io.Serializable;
import java.util.List;
import org.threeten.bp.LocalDateTime;

// DO NOT USE Auto.Value for using with Firebase DB.
public class LifeEvent implements Serializable {
  private LocalDateTime timeStart;
  private LocalDateTime timeEnd;
  private List<String> events;
  private String memo;

  private LifeEvent() {
  }

  public LifeEvent(LocalDateTime timeStart, LocalDateTime timeEnd, List<String> events, String memo) {
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.events = events;
    this.memo = memo;
  }

  public LocalDateTime getTimeStart() {
    return timeStart;
  }

  public LocalDateTime getTimeEnd() {
    return timeEnd;
  }

  public List<String> getEvents() {
    return events;
  }

  public String getMemo() {
    return memo;
  }
}

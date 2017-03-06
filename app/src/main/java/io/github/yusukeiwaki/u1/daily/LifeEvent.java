package io.github.yusukeiwaki.u1.daily;

import java.io.Serializable;
import java.util.List;
import org.threeten.bp.LocalDateTime;

// DO NOT USE Auto.Value for using with Firebase DB.
public class LifeEvent implements Serializable {
  public final LocalDateTime timeStart;
  public final LocalDateTime timeEnd;
  public final List<String> events;
  public final String memo;

  public LifeEvent(LocalDateTime timeStart, LocalDateTime timeEnd, List<String> events, String memo) {
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.events = events;
    this.memo = memo;
  }
}

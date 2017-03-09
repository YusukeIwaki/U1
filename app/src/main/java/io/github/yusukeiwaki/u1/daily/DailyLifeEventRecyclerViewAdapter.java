package io.github.yusukeiwaki.u1.daily;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import org.threeten.bp.LocalDateTime;

/*package*/ class DailyLifeEventRecyclerViewAdapter extends FirebaseRecyclerAdapter<LifeEvent, DailyLifeEventViewHolder> {
  public interface OnClickListener {
    void onClick(String key, LifeEvent lifeEvent);
  }

  public interface OnLongClickListener {
    boolean onLongClick(String key, LifeEvent lifeEvent);
  }

  private static class ExtLifeEvent extends LifeEvent {
    public final String key;

    public ExtLifeEvent(String key, LifeEvent lifeEvent) {
      super(lifeEvent.getTimeStart(), lifeEvent.getTimeEnd(), lifeEvent.getEvents(), lifeEvent.getMemo());
      this.key = key;
    }
  }

  @Override protected LifeEvent parseSnapshot(DataSnapshot snapshot) {
    return new ExtLifeEvent(snapshot.getKey(), super.parseSnapshot(snapshot));
  }

  private OnClickListener onClickListener;
  private OnLongClickListener onLongClickListener;

  private static Query getDatabaseReferenceFor(int year, int month, int day) {
    long start = LocalDateTime.of(year, month, day, 0, 0).toEpochSecond(LifeEvent.getTimeZone());
    long end = start + 86399;

    return FirebaseLifeEventManager.getDatabaseReference()
        .orderByChild("timeStart")
        .startAt(start).endAt(end);
  }

  public DailyLifeEventRecyclerViewAdapter(int year, int month, int day) {
    super(LifeEvent.class,
        DailyLifeEventViewHolder.getLayout(),
        DailyLifeEventViewHolder.class,
        getDatabaseReferenceFor(year, month, day));
  }

  @Override protected void populateViewHolder(DailyLifeEventViewHolder lifeEventViewHolder, LifeEvent lifeEvent, int position) {
    lifeEventViewHolder.bind(lifeEvent);

    if (lifeEvent instanceof ExtLifeEvent) {
      String key = ((ExtLifeEvent) lifeEvent).key;
      if (onClickListener != null) {
        lifeEventViewHolder.itemView.setOnClickListener(v -> onClickListener.onClick(key, lifeEvent));
      }
      if (onLongClickListener != null) {
        lifeEventViewHolder.itemView.setOnLongClickListener(v -> onLongClickListener.onLongClick(key, lifeEvent));
      }
    }
  }

  public void setOnClickListener(OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
    this.onLongClickListener = onLongClickListener;
  }
}

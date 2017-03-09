package io.github.yusukeiwaki.u1.daily;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDateTime;

/*package*/ class DailyLifeEventRecyclerViewAdapter extends FirebaseRecyclerAdapter<LifeEvent, DailyLifeEventViewHolder> {

  private static Query getDatabaseReferenceFor(int year, int month, int day) {
    long start = LocalDateTime.of(year, month, day, 0, 0).toEpochSecond(LifeEvent.getTimeZone());
    long end = start + 86399;

    return FirebaseLifeEventManager.getDatabaseReference()
        .orderByChild("timeStart")
        .startAt(start).endAt(end);
  }

  public DailyLifeEventRecyclerViewAdapter(int year, int month, int day) {
    super(LifeEvent.class,
        R.layout.listitem_daily_lifeevent,
        DailyLifeEventViewHolder.class,
        getDatabaseReferenceFor(year, month, day));
  }

  @Override protected void populateViewHolder(DailyLifeEventViewHolder lifeEventViewHolder, LifeEvent lifeEvent, int position) {
    lifeEventViewHolder.bind(lifeEvent);
  }
}

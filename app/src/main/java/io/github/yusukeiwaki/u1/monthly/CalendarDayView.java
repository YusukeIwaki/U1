package io.github.yusukeiwaki.u1.monthly;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import io.github.yusukeiwaki.u1.daily.FirebaseLifeEventManager;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import org.threeten.bp.LocalDateTime;

public class CalendarDayView extends RecyclerView {

  private static class LifeEventsAdapter extends FirebaseRecyclerAdapter<LifeEvent, CalendarDayLifeEventViewHolder> {
    private OnItemClickListener onItemClickListener;

    public LifeEventsAdapter(int year, int month, int day) {
      super(LifeEvent.class,
          CalendarDayLifeEventViewHolder.getLayout(),
          CalendarDayLifeEventViewHolder.class,
          getDatabaseReferenceFor(year, month, day));
    }

    private static Query getDatabaseReferenceFor(int year, int month, int day) {
      long start = LocalDateTime.of(year, month, day, 0, 0).toEpochSecond(LifeEvent.getTimeZone());
      long end = start + 86399;

      return FirebaseLifeEventManager.getDatabaseReference()
          .orderByChild("timeStart")
          .startAt(start).endAt(end);
    }

    @Override
    protected void populateViewHolder(CalendarDayLifeEventViewHolder holder, LifeEvent lifeEvent, int position) {
      holder.bind(lifeEvent);
      holder.itemView.setOnClickListener(view -> {
        if (onItemClickListener != null) onItemClickListener.onItemClick(lifeEvent);
      });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
      this.onItemClickListener = onItemClickListener;
    }
  }

  private final LifeEventsAdapter lifeEventsAdapter;

  public CalendarDayView(Context context, int year, int month, int day) {
    super(context);
    lifeEventsAdapter = new LifeEventsAdapter(year, month, day);
    setAdapter(lifeEventsAdapter);
    setLayoutManager(new LinearLayoutManager(getContext()));
  }

  public interface OnItemClickListener {
    void onItemClick(LifeEvent lifeEvent);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    lifeEventsAdapter.setOnItemClickListener(onItemClickListener);
  }

  public void cleanup() {
    lifeEventsAdapter.cleanup();
  }
}

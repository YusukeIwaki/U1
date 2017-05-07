package io.github.yusukeiwaki.u1.monthly;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import java.util.ArrayList;

public class CalendarDayView extends RecyclerView {

  private static class LifeEventsAdapter extends RecyclerView.Adapter<CalendarDayLifeEventViewHolder> {
    private final ArrayList<LifeEvent> lifeEventList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Override
    public CalendarDayLifeEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(CalendarDayLifeEventViewHolder.getLayout(), parent, false);
      return new CalendarDayLifeEventViewHolder(itemView);
    }

    @Override public void onBindViewHolder(CalendarDayLifeEventViewHolder holder, int position) {
      LifeEvent lifeEvent = lifeEventList.get(position);
      holder.bind(lifeEvent);
      holder.itemView.setOnClickListener(view -> {
        if (onItemClickListener != null) onItemClickListener.onItemClick(lifeEvent);
      });
    }

    @Override public int getItemCount() {
      return lifeEventList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
      this.onItemClickListener = onItemClickListener;
    }

    public void onLifeEventAdded(int index, LifeEvent lifeEvent) {
      lifeEventList.add(index, lifeEvent);
      notifyItemInserted(index);
    }

    public void onLifeEventChanged(int index, LifeEvent lifeEvent) {
      lifeEventList.set(index, lifeEvent);
      notifyItemChanged(index);
    }

    public void onLifeEventRemoved(int index) {
      lifeEventList.remove(index);
      notifyItemRemoved(index);
    }
  }

  private final LifeEventsAdapter lifeEventsAdapter;

  public CalendarDayView(Context context) {
    super(context);
    lifeEventsAdapter = new LifeEventsAdapter();
    setAdapter(lifeEventsAdapter);
    setLayoutManager(new LinearLayoutManager(getContext()));
  }

  public void onLifeEventAdded(int index, LifeEvent lifeEvent) {
    lifeEventsAdapter.onLifeEventAdded(index, lifeEvent);
  }

  public void onLifeEventChanged(int index, LifeEvent lifeEvent) {
    lifeEventsAdapter.onLifeEventChanged(index, lifeEvent);
  }

  public void onLifeEventRemoved(int index) {
    lifeEventsAdapter.onLifeEventRemoved(index);
  }

  public interface OnItemClickListener {
    void onItemClick(LifeEvent lifeEvent);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    lifeEventsAdapter.setOnItemClickListener(onItemClickListener);
  }
}

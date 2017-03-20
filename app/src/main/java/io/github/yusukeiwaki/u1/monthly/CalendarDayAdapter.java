package io.github.yusukeiwaki.u1.monthly;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.github.yusukeiwaki.u1.R;
import java.util.List;

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayViewHolder> {

  private final List<CalendarDay> dayList;
  private OnItemClickListener itemClickListener;

  public CalendarDayAdapter(int year, int month) {
    CalendarDayListHelper helper = new CalendarDayListHelper(year, month);
    dayList = helper.getDayList();
  }

  @Override public CalendarDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View itemView = inflater.inflate(R.layout.calendar_day, parent, false);
    return new CalendarDayViewHolder(itemView);
  }

  @Override public void onBindViewHolder(CalendarDayViewHolder holder, int position) {
    CalendarDay day = dayList.get(position);
    holder.bind(day);
    holder.itemView.setTag(day);
    holder.itemView.setOnClickListener(onClickListener);
  }

  View.OnClickListener onClickListener = view -> {
    CalendarDay day = (CalendarDay) view.getTag();
    if (day != null && itemClickListener != null) {
      itemClickListener.onItemClick(day);
    }
  };

  @Override public int getItemCount() {
    return dayList.size();
  }

  public interface OnItemClickListener {
    void onItemClick(CalendarDay day);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    itemClickListener = listener;
  }
}

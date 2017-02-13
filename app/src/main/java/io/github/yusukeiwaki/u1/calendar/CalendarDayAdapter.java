package io.github.yusukeiwaki.u1.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.github.yusukeiwaki.u1.R;
import java.util.List;

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayViewHolder> {

  private final List<CalendarDay> dayList;

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
    holder.bind(dayList.get(position));
  }

  @Override public int getItemCount() {
    return dayList.size();
  }
}

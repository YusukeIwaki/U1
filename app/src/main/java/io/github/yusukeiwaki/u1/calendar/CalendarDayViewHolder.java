package io.github.yusukeiwaki.u1.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import io.github.yusukeiwaki.u1.R;

public class CalendarDayViewHolder extends RecyclerView.ViewHolder {
  private final TextView dayText;
  public CalendarDayViewHolder(View itemView) {
    super(itemView);
    dayText = (TextView) itemView.findViewById(R.id.day);
  }

  public void bind(CalendarDay calendarDay) {
    switch (calendarDay.type()) {
      case CalendarDay.TYPE_IN_MONTH:
        itemView.setAlpha(1.0f);
        break;
      case CalendarDay.TYPE_OUT_OF_MONTH:
        itemView.setAlpha(0.125f);
        break;
    }

    dayText.setText(Integer.toString(calendarDay.day()));
  }
}

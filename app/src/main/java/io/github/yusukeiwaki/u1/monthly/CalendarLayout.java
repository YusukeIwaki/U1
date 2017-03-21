package io.github.yusukeiwaki.u1.monthly;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.github.yusukeiwaki.u1.R;
import java.util.HashMap;

public class CalendarLayout extends GridLayout {
  public interface OnItemClickListener {
    void onItemClicked(CalendarDay day);
  }

  private OnItemClickListener itemClickListener;
  private final HashMap<Integer, CalendarDayView> calendarDayViews = new HashMap<>();

  public CalendarLayout(Context context) {
    super(context);
    initialize(context, null);
  }

  public CalendarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(context, attrs);
  }

  public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context, attrs);
  }

  private void initialize(Context context, AttributeSet attrs) {
    setColumnCount(7);
  }

  public void setOnItemClickListener(OnItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  private View createGridItem() {
    View item = LayoutInflater.from(getContext()).inflate(R.layout.calendar_day, this, false);

    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1);
    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1);
    item.setLayoutParams(params);

    return item;
  }

  public void addDay(CalendarDay calendarDay) {
    addDayWithView(calendarDay, null);
  }

  public void addDayWithView(CalendarDay calendarDay, CalendarDayView calendarDayView) {
    View item = createGridItem();
    item.setOnClickListener(v -> {
      if (itemClickListener != null) itemClickListener.onItemClicked(calendarDay);
    });

    switch (calendarDay.type()) {
      case CalendarDay.TYPE_IN_MONTH:
        item.setAlpha(1.0f);
        if (calendarDayView != null) {
          calendarDayViews.put(calendarDay.day(), calendarDayView);
        }
        break;
      case CalendarDay.TYPE_OUT_OF_MONTH:
        item.setAlpha(0.125f);
        break;
    }

    TextView dayText = (TextView) item.findViewById(R.id.day);
    dayText.setText(Integer.toString(calendarDay.day()));

    if (calendarDayView != null) {
      calendarDayView.setOnItemClickListener(lifeEvent -> {
        if (itemClickListener != null) itemClickListener.onItemClicked(calendarDay);
      });
      calendarDayView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      ((LinearLayout) item).addView(calendarDayView);
    }

    addView(item);
  }
}

package io.github.yusukeiwaki.u1.monthly;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import java.util.List;
import org.threeten.bp.LocalDateTime;

public class CalendarDayLifeEventViewHolder extends RecyclerView.ViewHolder {
  private TextView timeStartLabel;
  private TextView detailLabel;

  public CalendarDayLifeEventViewHolder(View itemView) {
    super(itemView);

    timeStartLabel = (TextView) itemView.findViewById(R.id.time_start_text);
    detailLabel = (TextView) itemView.findViewById(R.id.detail_text);
  }

  public static int getLayout() {
    return R.layout.listitem_calendar_day_lifeevent;
  }

  public void bind(LifeEvent lifeEvent) {
    timeStartLabel.setText(
        LocalDateTime.ofEpochSecond(lifeEvent.getTimeStart(), 0, LifeEvent.getTimeZone())
            .format(LifeEvent.getTimeFormatter()));

    List<String> events = lifeEvent.getEvents();
    detailLabel.setText(TextUtils.join("・", events));
  }
}

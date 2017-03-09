package io.github.yusukeiwaki.u1.daily;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import io.github.yusukeiwaki.u1.R;
import org.threeten.bp.LocalDateTime;

public class DailyLifeEventViewHolder extends RecyclerView.ViewHolder {
  private TextView timeStartLabel;
  private TextView timeKaraLabel;
  private TextView timeEndLabel;
  private TextView memoText;

  public DailyLifeEventViewHolder(View itemView) {
    super(itemView);

    timeStartLabel = (TextView) itemView.findViewById(R.id.time_start_text);
    timeKaraLabel = (TextView) itemView.findViewById(R.id.time_kara_text);
    timeEndLabel = (TextView) itemView.findViewById(R.id.time_end_text);
    memoText = (TextView) itemView.findViewById(R.id.memo);
  }

  public static int getLayout() {
    return R.layout.listitem_daily_lifeevent;
  }

  public void bind(LifeEvent lifeEvent) {
    timeStartLabel.setText(
        LocalDateTime.ofEpochSecond(lifeEvent.getTimeStart(), 0, LifeEvent.getTimeZone())
            .format(LifeEvent.getTimeFormatter()));

    if (lifeEvent.getTimeEnd() > 0) {
      timeEndLabel.setText(
          LocalDateTime.ofEpochSecond(lifeEvent.getTimeEnd(), 0, LifeEvent.getTimeZone())
              .format(LifeEvent.getTimeFormatter()));

      timeKaraLabel.setVisibility(View.VISIBLE);
      timeEndLabel.setVisibility(View.VISIBLE);
    } else {
      timeKaraLabel.setVisibility(View.GONE);
      timeEndLabel.setVisibility(View.GONE);
    }

    if (lifeEvent.getMemo() != null) {
      memoText.setText(lifeEvent.getMemo());
    }
  }
}

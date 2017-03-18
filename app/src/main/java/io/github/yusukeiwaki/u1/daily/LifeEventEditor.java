package io.github.yusukeiwaki.u1.daily;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.varunest.sparkbutton.SparkButton;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.widgets.TimeInputLayout;
import java.util.ArrayList;
import org.threeten.bp.LocalDateTime;

public class LifeEventEditor {
  private final int year;
  private final int month;
  private final int day;

  public LifeEventEditor(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public LifeEventEditor(long epoch) {
    LocalDateTime localDateTime = getLocalDateTimeOfDayFor(epoch);
    this.year = localDateTime.getYear();
    this.month = localDateTime.getMonthValue();
    this.day = localDateTime.getDayOfMonth();
  }

  public int getLayout() {
    return R.layout.dialog_edit_lifeevent;
  }

  public interface Callback {
    void onSubmit(LifeEvent lifeEvent);
  }

  private class SetupForDialog {
    private final Dialog dialog;
    private final Callback callback;

    public SetupForDialog(Dialog dialog, Callback callback) {
      this.dialog = dialog;
      this.callback = callback;
    }

    public void execute() {
      CompoundButton chkTimeEnd = (CompoundButton) dialog.findViewById(R.id.chk_time_end);
      onCheckTimeEndChanged(chkTimeEnd, chkTimeEnd.isChecked());
      chkTimeEnd.setOnCheckedChangeListener(this::onCheckTimeEndChanged);

      dialog.findViewById(R.id.btn_save).setOnClickListener(v -> callback.onSubmit(getInputValues()));
    }

    private void onCheckTimeEndChanged(CompoundButton chk, boolean isChecked) {
      ((TextView) dialog.findViewById(R.id.caption_time_start))
          .setText(isChecked ? "開始時刻" : "時刻");

      ((TextView) dialog.findViewById(R.id.caption_time_end)).setText("終了時刻");

      TimeInputLayout editorTimeStart = (TimeInputLayout) dialog.findViewById(R.id.editor_time_start);
      TimeInputLayout editorTimeEnd = (TimeInputLayout) dialog.findViewById(R.id.editor_time_end);

      if (isChecked) {
        editorTimeEnd.setTime(editorTimeStart.getTime());
        editorTimeEnd.setVisibility(View.VISIBLE);
        editorTimeEnd.requestFocus();
      } else {
        editorTimeEnd.setVisibility(View.GONE);
      }
    }

    private LifeEvent getInputValues() {
      TimeInputLayout editorTimeStart = (TimeInputLayout) dialog.findViewById(R.id.editor_time_start);
      TimeInputLayout editorTimeEnd = (TimeInputLayout) dialog.findViewById(R.id.editor_time_end);
      CompoundButton chkTimeEnd = (CompoundButton) dialog.findViewById(R.id.chk_time_end);

      ArrayList<String> listSelected = new ArrayList<>();
      ViewGroup container = (LinearLayout) dialog.findViewById(R.id.event_type_container);
      for (int i = 0; i < container.getChildCount(); i++) {
        View v = container.getChildAt(i);
        if (v instanceof SparkButton) {
          SparkButton btn = (SparkButton) v;
          if (btn.isChecked()) {
            listSelected.add((String) btn.getTag());
          }
        }
      }
      TextView editorMemo = (TextView) dialog.findViewById(R.id.editor_memo);



      return new LifeEvent(
          getEpochSecondsFor(editorTimeStart.getTime()),
          chkTimeEnd.isChecked() ? getEpochSecondsFor(editorTimeEnd.getTime()) : 0,
          listSelected,
          editorMemo.getText().toString());
    }
  }

  private long getEpochSecondsFor(TimeInputLayout.TimeOfDay timeOfDay) {
    return LocalDateTime.of(year, month, day, timeOfDay.hour, timeOfDay.minute)
        .toEpochSecond(LifeEvent.getTimeZone());
  }

  private LocalDateTime getLocalDateTimeOfDayFor(long epoch) {
    return LocalDateTime.ofEpochSecond(epoch, 0, LifeEvent.getTimeZone());
  }

  public void initializeDialogValuesWith(Dialog dialog, LifeEvent lifeEvent) {
    TimeInputLayout editorTimeStart = (TimeInputLayout) dialog.findViewById(R.id.editor_time_start);
    TimeInputLayout editorTimeEnd = (TimeInputLayout) dialog.findViewById(R.id.editor_time_end);
    CompoundButton chkTimeEnd = (CompoundButton) dialog.findViewById(R.id.chk_time_end);

    editorTimeStart.setTime(getLocalDateTimeOfDayFor(lifeEvent.getTimeStart()));
    long timeEnd = lifeEvent.getTimeEnd();
    if (timeEnd != 0) {
      chkTimeEnd.setChecked(true);
      editorTimeEnd.setTime(getLocalDateTimeOfDayFor(timeEnd));
    }

    TextView editorMemo = (TextView) dialog.findViewById(R.id.editor_memo);
    editorMemo.setText(lifeEvent.getMemo());

    for (String tag : lifeEvent.getEvents()) {

      ViewGroup container = (LinearLayout) dialog.findViewById(R.id.event_type_container);
      for (int i = 0; i < container.getChildCount(); i++) {
        View v = container.getChildAt(i);
        if (v instanceof SparkButton) {
          SparkButton btn = (SparkButton) v;
          String tag2 = (String) btn.getTag();
          if (tag.equals(tag2)) {
            btn.setChecked(true);
          }
        }
      }
    }
  }

  public void setupForDialog(Dialog dialog, Callback callback) {
    new SetupForDialog(dialog, callback).execute();
  }
}

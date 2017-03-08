package io.github.yusukeiwaki.u1.daily;

import android.app.Dialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.annimon.stream.Stream;
import com.varunest.sparkbutton.SparkButton;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.widgets.TimeInputLayout;
import java.util.List;
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

      SparkButton chkNyou = (SparkButton) dialog.findViewById(R.id.chk_nyou);
      chkNyou.setTag("尿");
      SparkButton chkBen = (SparkButton) dialog.findViewById(R.id.chk_ben);
      chkBen.setTag("便");
      SparkButton chkChusha = (SparkButton) dialog.findViewById(R.id.chk_chusha);
      chkChusha.setTag("注射");
      SparkButton chkJunyu = (SparkButton) dialog.findViewById(R.id.chk_junyu);
      chkJunyu.setTag("授乳");

      List<String> selectedChk = Stream.of(chkNyou, chkBen, chkChusha, chkJunyu)
          .filter(chk -> chk.isChecked())
          .map(chk -> (String) chk.getTag())
          .toList();

      TextView editorMemo = (TextView) dialog.findViewById(R.id.editor_memo);



      return new LifeEvent(
          editorTimeStart.getLocalDateTimeWithDate(year, month, day),
          chkTimeEnd.isChecked() ? editorTimeEnd.getLocalDateTimeWithDate(year, month, day) : null,
          selectedChk,
          editorMemo.getText().toString());
    }

  }

  public void initializeDialogValuesWith(Dialog dialog, LifeEvent lifeEvent) {
    TimeInputLayout editorTimeStart = (TimeInputLayout) dialog.findViewById(R.id.editor_time_start);
    TimeInputLayout editorTimeEnd = (TimeInputLayout) dialog.findViewById(R.id.editor_time_end);
    CompoundButton chkTimeEnd = (CompoundButton) dialog.findViewById(R.id.chk_time_end);

    editorTimeStart.setTime(lifeEvent.getTimeStart());
    LocalDateTime timeEnd = lifeEvent.getTimeEnd();
    if (timeEnd != null) {
      chkTimeEnd.setChecked(true);
      editorTimeEnd.setTime(timeEnd);
    }

    TextView editorMemo = (TextView) dialog.findViewById(R.id.editor_memo);
    editorMemo.setText(lifeEvent.getMemo());

    for (String tag : lifeEvent.getEvents()) {
      SparkButton chkNyou = (SparkButton) dialog.findViewById(R.id.chk_nyou);
      SparkButton chkBen = (SparkButton) dialog.findViewById(R.id.chk_ben);
      SparkButton chkChusha = (SparkButton) dialog.findViewById(R.id.chk_chusha);
      SparkButton chkJunyu = (SparkButton) dialog.findViewById(R.id.chk_junyu);

      Stream.of(chkNyou, chkBen, chkChusha, chkJunyu)
          .filter(sparkButton -> tag.equals(sparkButton.getTag()))
          .findFirst()
          .ifPresent(sparkButton -> {
            sparkButton.setChecked(true);
          });
    }
  }

  public void setupForDialog(Dialog dialog, Callback callback) {
    new SetupForDialog(dialog, callback).execute();
  }
}

package io.github.yusukeiwaki.u1.daily;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.annimon.stream.Stream;
import com.varunest.sparkbutton.SparkButton;
import io.github.yusukeiwaki.u1.AbstractDialogFragment;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.widgets.TimeInputLayout;
import java.util.List;

public class EditLifeEventDialogFragment extends AbstractDialogFragment {
  private int year;
  private int month;
  private int day;

  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";
  private static final String KEY_DAY = "day";

  public static EditLifeEventDialogFragment newInstance(int year, int month, int day) {
    Bundle args = new Bundle();
    args.putInt(KEY_YEAR, year);
    args.putInt(KEY_MONTH, month);
    args.putInt(KEY_DAY, day);

    EditLifeEventDialogFragment fragment = new EditLifeEventDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int getLayout() {
    return R.layout.dialog_edit_lifeevent;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handleArgs(getArguments());
  }

  private void handleArgs(Bundle args) {
    if (args == null) throw new IllegalArgumentException("year, month, day is required.");
    if (!args.containsKey(KEY_YEAR)) throw new IllegalArgumentException("year is required.");
    if (!args.containsKey(KEY_MONTH)) throw new IllegalArgumentException("month is required.");
    if (!args.containsKey(KEY_DAY)) throw new IllegalArgumentException("day is required.");

    year = args.getInt(KEY_YEAR);
    month = args.getInt(KEY_MONTH);
    day = args.getInt(KEY_DAY);
  }

  @Override protected void onSetupDialog() {
    Dialog dialog = getDialog();

    CompoundButton chkTimeEnd = (CompoundButton) dialog.findViewById(R.id.chk_time_end);
    onCheckTimeEndChanged(chkTimeEnd, chkTimeEnd.isChecked());
    chkTimeEnd.setOnCheckedChangeListener(this::onCheckTimeEndChanged);

    dialog.findViewById(R.id.btn_save).setOnClickListener(v -> onSubmit());
  }

  private void onCheckTimeEndChanged(CompoundButton chk, boolean isChecked) {
    Dialog dialog = getDialog();

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
    Dialog dialog = getDialog();

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

  private void onSubmit() {
    LifeEvent lifeEvent = getInputValues();
  }
}

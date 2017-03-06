package io.github.yusukeiwaki.u1.daily;

import android.app.Dialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import io.github.yusukeiwaki.u1.AbstractDialogFragment;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.widgets.TimeInputLayout;

public class EditLifeEventDialogFragment extends AbstractDialogFragment {

  public static EditLifeEventDialogFragment newInstance() {
    return new EditLifeEventDialogFragment();
  }

  @Override protected int getLayout() {
    return R.layout.dialog_edit_lifeevent;
  }

  @Override protected void onSetupDialog() {
    Dialog dialog = getDialog();

    CompoundButton chkTimeEnd = (CompoundButton) dialog.findViewById(R.id.chk_time_end);
    onCheckTimeEndChanged(chkTimeEnd, chkTimeEnd.isChecked());
    chkTimeEnd.setOnCheckedChangeListener(this::onCheckTimeEndChanged);
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
}

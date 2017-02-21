package io.github.yusukeiwaki.u1;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;

public abstract class AbstractDialogFragment extends DialogFragment {

  protected abstract int getLayout();

  protected abstract void onSetupDialog();

  @Override public final void setupDialog(Dialog dialog, int style) {
    super.setupDialog(dialog, style);
    dialog.setContentView(getLayout());
    onSetupDialog();
  }
}

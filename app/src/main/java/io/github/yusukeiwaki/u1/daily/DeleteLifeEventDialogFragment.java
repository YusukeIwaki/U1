package io.github.yusukeiwaki.u1.daily;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import io.github.yusukeiwaki.u1.AbstractDialogFragment;
import io.github.yusukeiwaki.u1.R;

public class DeleteLifeEventDialogFragment extends AbstractDialogFragment {
  private static final String KEY_ID = "id";
  private static final String KEY_LIFE_EVENT = "life_event";

  private String origId;
  private FirebaseLifeEventManager lifeEventManager;

  public static DeleteLifeEventDialogFragment newInstance(String id) {
    Bundle args = new Bundle();
    args.putString(KEY_ID, id);

    DeleteLifeEventDialogFragment fragment = new DeleteLifeEventDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int getLayout() {
    return R.layout.dialog_delete_lifeevent;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handleArgs(getArguments());
  }

  private void handleArgs(Bundle args) {
    if (args == null) throw new IllegalArgumentException("year, month, day is required.");
    if (!args.containsKey(KEY_ID)) throw new IllegalArgumentException("id is required.");
    origId = args.getString(KEY_ID);
    lifeEventManager = new FirebaseLifeEventManager();
  }

  @Override protected void onSetupDialog() {
    Dialog dialog = getDialog();
    dialog.findViewById(R.id.btn_delete).setOnClickListener(v -> {
      lifeEventManager.delete(origId);
      dismiss();
    });
  }
}

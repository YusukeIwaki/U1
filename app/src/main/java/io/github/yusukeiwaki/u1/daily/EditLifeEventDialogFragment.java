package io.github.yusukeiwaki.u1.daily;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import io.github.yusukeiwaki.u1.AbstractDialogFragment;

public class EditLifeEventDialogFragment extends AbstractDialogFragment {
  private static final String KEY_ID = "id";
  private static final String KEY_LIFE_EVENT = "life_event";

  private String origId;
  private LifeEvent origLifeEvent;

  private FirebaseLifeEventManager lifeEventManager;
  private LifeEventEditor lifeEventEditor;

  public static EditLifeEventDialogFragment newInstance(String id, LifeEvent lifeEvent) {
    Bundle args = new Bundle();
    args.putString(KEY_ID, id);
    args.putSerializable(KEY_LIFE_EVENT, lifeEvent);

    EditLifeEventDialogFragment fragment = new EditLifeEventDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int getLayout() {
    return lifeEventEditor.getLayout();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handleArgs(getArguments());
    lifeEventManager = new FirebaseLifeEventManager();
  }

  private void handleArgs(Bundle args) {
    if (args == null) throw new IllegalArgumentException("year, month, day is required.");
    if (!args.containsKey(KEY_ID)) throw new IllegalArgumentException("id is required.");
    if (!args.containsKey(KEY_LIFE_EVENT)) throw new IllegalArgumentException("life_event is required.");

    origId = args.getString(KEY_ID);
    origLifeEvent = (LifeEvent) args.getSerializable(KEY_LIFE_EVENT);

    lifeEventEditor = new LifeEventEditor(origLifeEvent.getTimeStart());
  }

  @Override protected void onSetupDialog() {
    Dialog dialog = getDialog();
    lifeEventEditor.setupForDialog(dialog, lifeEvent -> {
      lifeEventManager.update(origId, lifeEvent);
      dismiss();
    });
    lifeEventEditor.initializeDialogValuesWith(dialog, origLifeEvent);
  }
}

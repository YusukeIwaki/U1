package io.github.yusukeiwaki.u1.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import io.github.yusukeiwaki.u1.AbstractDialogFragment;

public class AddLifeEventDialogFragment extends AbstractDialogFragment {
  private static final String KEY_YEAR = "year";
  private static final String KEY_MONTH = "month";
  private static final String KEY_DAY = "day";

  private FirebaseLifeEventManager lifeEventManager;
  private LifeEventEditor lifeEventEditor;

  public static AddLifeEventDialogFragment newInstance(int year, int month, int day) {
    Bundle args = new Bundle();
    args.putInt(KEY_YEAR, year);
    args.putInt(KEY_MONTH, month);
    args.putInt(KEY_DAY, day);

    AddLifeEventDialogFragment fragment = new AddLifeEventDialogFragment();
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
    if (!args.containsKey(KEY_YEAR)) throw new IllegalArgumentException("year is required.");
    if (!args.containsKey(KEY_MONTH)) throw new IllegalArgumentException("month is required.");
    if (!args.containsKey(KEY_DAY)) throw new IllegalArgumentException("day is required.");

    int year = args.getInt(KEY_YEAR);
    int month = args.getInt(KEY_MONTH);
    int day = args.getInt(KEY_DAY);
    lifeEventEditor = new LifeEventEditor(year, month, day);
  }

  @Override protected void onSetupDialog() {
    lifeEventEditor.setupForDialog(getDialog(), lifeEvent -> {
      lifeEventManager.create(lifeEvent);
      dismiss();
    });
  }


}

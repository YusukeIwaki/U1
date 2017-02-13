package io.github.yusukeiwaki.u1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractFragment extends Fragment {

  public abstract int getLayout();

  public abstract void onSetupView();

  protected View rootView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(getLayout(), container, false);
    onSetupView();
    return rootView;
  }
}

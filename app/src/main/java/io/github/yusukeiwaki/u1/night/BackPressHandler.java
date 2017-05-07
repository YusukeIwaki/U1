package io.github.yusukeiwaki.u1.night;

import android.os.Handler;
import android.os.Message;

public class BackPressHandler extends Handler {

  private boolean enabled;

  public boolean handleBackPressed() {
    if (enabled) return false;

    enabled = true;
    sendEmptyMessageDelayed(1, 2500);
    return true;
  }

  @Override public void handleMessage(Message msg) {
    if (msg.what == 1) {
      enabled = false;
    }
  }
}

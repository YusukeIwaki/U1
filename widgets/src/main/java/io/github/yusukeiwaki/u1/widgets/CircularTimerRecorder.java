package io.github.yusukeiwaki.u1.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class CircularTimerRecorder extends RingProgressBar {

  private static final String TAG = CircularTimerRecorder.class.getSimpleName();

  private static class TickTack {
    public interface Callback {
      void onTick(long elapsedTime);
    }

    private final Callback callback;
    private Timer timer;
    private TimerTask timerTask;
    private long elapsedTime;

    public TickTack(Callback callback) {
      this.callback = callback;
      timer = null;
      elapsedTime = 0;
    }

    public void start() {
      if (timer == null) {
        timer = new Timer();
        timerTask = new TimerTask() {
          @Override public void run() {
            callback.onTick(++elapsedTime);
          }
        };
        timer.schedule(timerTask, 1000, 1000);
      }
    }

    public boolean isStarted() {
      return timer != null;
    }

    public void stop() {
      if (timer != null) {
        timer.cancel();
        timer = null;
      }
    }

    public long getElapsedTime() {
      return elapsedTime;
    }

    public void reset() {
      stop();
      elapsedTime = 0;
      callback.onTick(0);
    }
  }

  private TickTack tickTack;

  public interface TimeRecordCallback {
    void onMinuteChanged(int minute);
  }
  TimeRecordCallback timeRecordCallback;

  public CircularTimerRecorder(Context context) {
    super(context);
    initialize(context, null);
  }

  public CircularTimerRecorder(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(context, attrs);
  }

  public CircularTimerRecorder(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context, attrs);
  }

  private void initialize(Context context, AttributeSet attrs) {
    setMax(60);
    setStyle(1);

    tickTack = new TickTack(new TickTack.Callback() {
      @Override public void onTick(final long elapsedTime) {
        post(new Runnable() {
          @Override public void run() {
            updateProgress(elapsedTime);

            if (timeRecordCallback != null && elapsedTime % 60 == 0) {
              timeRecordCallback.onMinuteChanged((int) (elapsedTime / 60));
            }
          }
        });
      }
    });

    addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
      @Override public void onViewAttachedToWindow(View view) {
      }

      @Override public void onViewDetachedFromWindow(View view) {
        stopTimer();
        removeOnAttachStateChangeListener(this);
      }
    });
  }

  private void setStyle(int style) {
    try {
      Field fStyle = RingProgressBar.class.getDeclaredField("style");
      fStyle.setAccessible(true);
      fStyle.set(this, style);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  public void setTimeRecordCallback(TimeRecordCallback timeRecordCallback) {
    this.timeRecordCallback = timeRecordCallback;
  }

  private void updateProgress(long elapsedTime) {
    int unit = (int) (elapsedTime % 120);

    if (unit < 60) {
      setProgress(unit);
      setRotationY(0);
    } else {
      setProgress(120 - unit);
      setRotationY(180);
    }
  }

  public void startTimer() {
    if (tickTack != null && !tickTack.isStarted()) {
      tickTack.start();
    }
  }

  public void stopTimer() {
    if (tickTack != null && tickTack.isStarted()) {
      tickTack.stop();
    }
  }

  public boolean isStarted() {
    return tickTack != null && tickTack.isStarted();
  }

  public long getElapsedTime() {
    if (tickTack != null) {
      return tickTack.getElapsedTime();
    }
    return 0;
  }

  public void resetTimer() {
    if (tickTack != null) {
      tickTack.reset();
    }
  }
}

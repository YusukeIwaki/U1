package io.github.yusukeiwaki.u1.night;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

/*package*/ class NightScreenBrightnessManager {
  public interface Callback {
    void onNightStateDetected(boolean isNight);
  }

  private static class NightStateDetector {
    private static final int SIZE = 5;
    private final int[] states = new int[SIZE];
    private int index = 0;
    private boolean hasEnoughData = false;
    private final Callback callback;

    private static final int STATE_UNSTABLE = -1;
    private static final int STATE_NIGHT = 0;
    private static final int STATE_DAY = 1;
    private int state = STATE_UNSTABLE;

    private NightStateDetector(Callback callback) {
      this.callback = callback;
    }

    public void pushData(float value) {
      int i = index % SIZE;
      states[i] = getStateOf(value);

      index++;
      if (index >= SIZE) {
        if (!hasEnoughData) {
          hasEnoughData = true;
        }
        index %= SIZE;
      }

      if (hasEnoughData) handleValues();
    }

    private int getStateOf(float value) {
      //確実にnight
      if (value < 1.0f) return STATE_NIGHT;

      //確実にday
      if (value >= 2.0f) return STATE_DAY;

      return STATE_UNSTABLE;
    }

    private void handleValues() {
      int newState = detectStatePrecisely();
      if (newState != state) {
        state = newState;
        callback.onNightStateDetected(state == STATE_NIGHT);
      }
    }

    private int detectStatePrecisely() {
      int numNight = 0;
      int numDay = 0;
      int numOther = 0;
      for (int state: states) {
        if (state == STATE_NIGHT) numNight++;
        else if (state == STATE_DAY) numDay++;
        else numOther++;
      }

      if (numNight > numDay && numNight > numOther) {
        return STATE_NIGHT;
      }
      if (numDay > numNight && numDay > numOther) {
        return STATE_DAY;
      }
      return STATE_UNSTABLE;
    }
  }

  private NightStateDetector nightStateDetector;


  private boolean isEnabled;
  private final SensorManager sensorManager;
  private final SensorEventListener lightSensorListener = new SensorEventListener() {
    @Override public void onSensorChanged(SensorEvent sensorEvent) {
      if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
        float value = sensorEvent.values[0];
        handleSensorValue(value);
      }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void handleSensorValue(float value) {
      nightStateDetector.pushData(value);
    }
  };

  public NightScreenBrightnessManager(Context context, Callback callback) {
    nightStateDetector = new NightStateDetector(callback);
    isEnabled = false;

    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
  }

  public void enable() {
    if (isEnabled) return;
    isEnabled = true;

    List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_LIGHT);
    if (!sensors.isEmpty()) {
      sensorManager.registerListener(lightSensorListener, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void disable() {
    if (!isEnabled) return;
    isEnabled = false;
    sensorManager.unregisterListener(lightSensorListener);
  }
}

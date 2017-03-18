package io.github.yusukeiwaki.u1.night;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import java.util.List;

/*package*/ class NightScreenBrightnessManager {
  public interface Callback {
    void onNightStateDetected(boolean isNight);
  }

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
      boolean isNight = value <= 1.0f;
      Message m = handler.obtainMessage(0, isNight);
      handler.removeMessages(0);
      handler.sendMessageDelayed(m, 600);
    }
  };



  private final Callback callback;
  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      if (callback != null) callback.onNightStateDetected((boolean) msg.obj);
    }
  };

  public NightScreenBrightnessManager(Context context, Callback callback) {
    isEnabled = false;

    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    this.callback = callback;
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

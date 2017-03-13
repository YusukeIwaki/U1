package io.github.yusukeiwaki.u1.night;

import android.view.View;
import android.widget.TextView;
import com.varunest.sparkbutton.SparkButton;
import io.github.yusukeiwaki.u1.AbstractFragment;
import io.github.yusukeiwaki.u1.R;
import io.github.yusukeiwaki.u1.widgets.CircularTimerRecorder;

public class JunyuTimerFragment extends AbstractFragment {
  @Override protected int getLayout() {
    return R.layout.fragment_junyu_timer;
  }

  @Override protected void onSetupView() {
    SparkButton btnTimeRecorderLeft = (SparkButton) rootView.findViewById(R.id.btn_time_recorder_left);
    SparkButton btnTimeRecorderRight = (SparkButton) rootView.findViewById(R.id.btn_time_recorder_right);
    JunyuTimerManager junyuTimerManager = new JunyuTimerManager(getContext(),
        new TimeRecorderManager(
            btnTimeRecorderLeft,
            (CircularTimerRecorder) rootView.findViewById(R.id.time_recorder_left),
            (TextView) rootView.findViewById(R.id.text_time_recorder_left)),
        new TimeRecorderManager(
            btnTimeRecorderRight,
            (CircularTimerRecorder) rootView.findViewById(R.id.time_recorder_right),
            (TextView) rootView.findViewById(R.id.text_time_recorder_right)));

    extendTouchBoundary(btnTimeRecorderLeft);
    extendTouchBoundary(btnTimeRecorderRight);

    junyuTimerManager.initialize();


    rootView.findViewById(R.id.btn_upload_record).setOnClickListener(view -> {
      junyuTimerManager.uploadLifeEvent();
    });

  }

  private void extendTouchBoundary(SparkButton sparkButton) {
    View parent = (View) sparkButton.getParent().getParent();
    parent.setOnClickListener(view -> {
      sparkButton.callOnClick();
    });
  }
}

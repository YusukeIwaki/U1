package io.github.yusukeiwaki.u1.night;

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
    JunyuTimerManager junyuTimerManager = new JunyuTimerManager(getContext(),
        new TimeRecorderManager(
            (SparkButton) rootView.findViewById(R.id.btn_time_recorder_left),
            (CircularTimerRecorder) rootView.findViewById(R.id.time_recorder_left),
            (TextView) rootView.findViewById(R.id.text_time_recorder_left)),
        new TimeRecorderManager(
            (SparkButton) rootView.findViewById(R.id.btn_time_recorder_right),
            (CircularTimerRecorder) rootView.findViewById(R.id.time_recorder_right),
            (TextView) rootView.findViewById(R.id.text_time_recorder_right)));

    junyuTimerManager.initialize();


    rootView.findViewById(R.id.btn_upload_record).setOnClickListener(view -> {
      junyuTimerManager.uploadLifeEvent();
    });

  }
}

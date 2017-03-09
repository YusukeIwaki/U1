package io.github.yusukeiwaki.u1.widgets;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import io.github.yusukeiwaki.u1.widget.R;
import org.threeten.bp.LocalDateTime;

public class TimeInputLayout extends LinearLayout {

  private EditText[] editTexts;
  private boolean skipTextWatcher;

  public static class TimeOfDay {
    public final int hour;
    public final int minute;

    public TimeOfDay(int hour, int minute) {
      this.hour = hour;
      this.minute = minute;
    }
  }

  public TimeInputLayout(Context context) {
    super(context);
    initialize(context, null);
  }

  public TimeInputLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(context, attrs);
  }

  public TimeInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context, attrs);
  }

  public TimeInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize(context, attrs);
  }

  private void initialize(Context context, AttributeSet attrs) {
    setOrientation(HORIZONTAL);
    View.inflate(context, R.layout.time_input_layout, this);
    editTexts = new EditText[] {
        (EditText) findViewById(R.id.time_input_editor_hour1),
        (EditText) findViewById(R.id.time_input_editor_hour2),
        (EditText) findViewById(R.id.time_input_editor_minute1),
        (EditText) findViewById(R.id.time_input_editor_minute2)
    };

    skipTextWatcher = false;

    for (final EditText editor : editTexts) {
      editor.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View view) {
          editor.requestFocus();
          editor.selectAll();
        }
      });

      if (editor != editTexts[3]) {
        editor.addTextChangedListener(new SimpleTextWatcher(){
          @Override public void afterTextChanged(Editable s) {
            if (s.length() > 0 && !skipTextWatcher) {
              editor.focusSearch(View.FOCUS_RIGHT).requestFocus();
            }
          }
        });
      }

      if (editor != editTexts[0]) {
        editor.addTextChangedListener(new SimpleTextWatcher(){
          @Override public void afterTextChanged(Editable s) {
            if (s.length() == 0 && !skipTextWatcher) {
              editor.focusSearch(View.FOCUS_LEFT).requestFocus();
            }
          }
        });
      }
    }

    editTexts[3].addTextChangedListener(new SimpleTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        if (s.length() > 0 && !skipTextWatcher) {
          if (isValidTime()) {
            View view = editTexts[editTexts.length - 1].focusSearch(View.FOCUS_DOWN);
            if (view != null) {
              view.requestFocus();
            };
          } else {
            shake();
            editTexts[0].requestFocus();
          }
        }
      }
    });

    LocalDateTime currentTime = LocalDateTime.now();
    setTime(currentTime.getHour(), currentTime.getMinute());
  }

  private void setTime(@IntRange(from=0, to=23) int hourOfDay, @IntRange(from=0, to=59) int minutes) {
    skipTextWatcher = true;
    editTexts[0].setText(Integer.toString(hourOfDay/10));
    editTexts[1].setText(Integer.toString(hourOfDay%10));
    editTexts[2].setText(Integer.toString(minutes/10));
    editTexts[3].setText(Integer.toString(minutes%10));
    skipTextWatcher = false;
  }

  public void setTime(@Nullable TimeOfDay timeOfDay) {
    if (timeOfDay != null) {
      setTime(timeOfDay.hour, timeOfDay.minute);
    }
  }

  public void setTime(LocalDateTime localDateTime) {
    setTime(localDateTime.getHour(), localDateTime.getMinute());
  }

  public @Nullable TimeOfDay getTime() {
    if (!isValidTime()) return null;

    String hourString = editTexts[0].getText().toString() + editTexts[1].getText().toString();
    String minuteString = editTexts[2].getText().toString() + editTexts[3].getText().toString();

    int hour = Integer.parseInt(hourString, 10);
    int minute = Integer.parseInt(minuteString, 10);
    return new TimeOfDay(hour, minute);
  }

  private boolean isValidTime() {
    boolean validNumbers = true;

    for (EditText editor : editTexts) {
      if ((editor.length() != 1) || !TextUtils.isDigitsOnly(editor.getText())) {
        validNumbers = false;
        break;
      }
    }

    if (!validNumbers) return false;

    String hourString = editTexts[0].getText().toString() + editTexts[1].getText().toString();
    String minuteString = editTexts[2].getText().toString() + editTexts[3].getText().toString();

    int hour = Integer.parseInt(hourString, 10);
    int minute = Integer.parseInt(minuteString, 10);

    return hour >= 0 && hour< 24 && minute >= 0 && minute < 60;
  }

  private void shake() {
    startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
  }
}

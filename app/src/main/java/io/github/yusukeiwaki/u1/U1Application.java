package io.github.yusukeiwaki.u1;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class U1Application extends Application {
  @Override public void onCreate() {
    super.onCreate();

    AndroidThreeTen.init(this);

    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
  }
}

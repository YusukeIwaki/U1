package io.github.yusukeiwaki.u1.daily;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseLifeEventManager {
  private static final String TAG = FirebaseLifeEventManager.class.getSimpleName();

  public static DatabaseReference getDatabaseReference() {
    return FirebaseDatabase.getInstance().getReference().child("lifeevents");
  }

  private final DatabaseReference lifeEventsRef = getDatabaseReference();

  public Task<Void> create(LifeEvent lifeEvent) {
    return lifeEventsRef.push().setValue(lifeEvent).addOnFailureListener(e -> {
      Log.e(TAG, e.getMessage(), e);
    });
  }

  public Task<Void> update(String key, LifeEvent lifeEvent) {
    return lifeEventsRef.child(key).setValue(lifeEvent).addOnFailureListener(e -> {
      Log.e(TAG, e.getMessage(), e);
    });
  }

  public Task<Void> delete(String key) {
    return lifeEventsRef.child(key).removeValue().addOnFailureListener(e -> {
      Log.e(TAG, e.getMessage(), e);
    });
  }
}

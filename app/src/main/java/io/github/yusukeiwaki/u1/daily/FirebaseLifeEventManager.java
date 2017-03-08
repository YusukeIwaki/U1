package io.github.yusukeiwaki.u1.daily;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseLifeEventManager {

  DatabaseReference lifeEventsRef;

  public FirebaseLifeEventManager() {
    lifeEventsRef = FirebaseDatabase.getInstance().getReference().child("lifeevents");
  }

  public Task<Void> create(LifeEvent lifeEvent) {
    return lifeEventsRef.push().setValue(lifeEvent);
  }

  public Task<Void> update(String key, LifeEvent lifeEvent) {
    return lifeEventsRef.child(key).setValue(lifeEvent);
  }

  public Task<Void> delete(String key) {
    return lifeEventsRef.child(key).removeValue();
  }
}

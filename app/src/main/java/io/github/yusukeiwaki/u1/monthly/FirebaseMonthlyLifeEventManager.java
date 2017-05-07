package io.github.yusukeiwaki.u1.monthly;

import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import io.github.yusukeiwaki.u1.daily.FirebaseLifeEventManager;
import io.github.yusukeiwaki.u1.daily.LifeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

class FirebaseMonthlyLifeEventManager implements ChildEventListener {
  public interface Callback {
    void onLifeEventAdded(int dayOfMonth, int index, LifeEvent lifeEvent);
    void onLifeEventChanged(int dayOfMonth, int index, LifeEvent lifeEvent);
    void onLifeEventRemoved(int dayOfMonth, int index);
  }

  private static final String TAG = FirebaseMonthlyLifeEventManager.class.getSimpleName();
  private final HashMap<String, LifeEvent> snapshotMap = new HashMap<>();
  private final ArrayList<String>[] snapshotKeyLists;
  private final Query ref;
  private Callback callback;

  public FirebaseMonthlyLifeEventManager(int year, int month) {
    int numDays = LocalDate.of(year, month, 1).lengthOfMonth();
    snapshotKeyLists = new ArrayList[numDays];
    for (int i=0; i<numDays; i++) {
      snapshotKeyLists[i] = new ArrayList<>();
    }

    ref = getDatabaseReference(year, month);
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
    ref.addChildEventListener(this);
  }

  public void cleanup() {
    ref.removeEventListener(this);
  }

  private Query getDatabaseReference(int year, int month) {
    final LocalDateTime dateTimeStart = LocalDateTime.of(year, month, 1, 0, 0);
    final LocalDateTime dateTimeEnd = dateTimeStart.plusMonths(1);
    long start = dateTimeStart.toEpochSecond(LifeEvent.getTimeZone());
    long end = dateTimeEnd.toEpochSecond(LifeEvent.getTimeZone());

    return FirebaseLifeEventManager.getDatabaseReference()
        .orderByChild("timeStart")
        .startAt(start).endAt(end - 1);
  }

  private int getIndexForKey(int dayIndex, String key) {
    int index = 0;
    for (String snapshotKey : snapshotKeyLists[dayIndex]) {
      if (snapshotKey.equals(key)) {
        return index;
      } else {
        index++;
      }
    }
    return -1;
  }

  private LifeEvent getLifeEventFrom(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(LifeEvent.class);
  }

  private int getDayIndexForLifeEvent(LifeEvent lifeEvent) {
    return LocalDateTime.ofEpochSecond(lifeEvent.getTimeStart(), 0, LifeEvent.getTimeZone()).getDayOfMonth() - 1;
  }

  @Override public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
    String key = dataSnapshot.getKey();
    LifeEvent newLifeEvent = getLifeEventFrom(dataSnapshot);
    snapshotMap.put(key, newLifeEvent);
    int i = getDayIndexForLifeEvent(newLifeEvent);

    int index = previousChildKey == null ? 0 : getIndexForKey(i, previousChildKey) + 1;
    snapshotKeyLists[i].add(index, key);
    callback.onLifeEventAdded(i + 1, index, newLifeEvent);
  }

  @Override public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
    String key = dataSnapshot.getKey();
    LifeEvent oldLifeEvent = snapshotMap.get(key);
    LifeEvent newLifeEvent = getLifeEventFrom(dataSnapshot);
    snapshotMap.put(key, newLifeEvent);
    int oldI = getDayIndexForLifeEvent(oldLifeEvent);
    int newI = getDayIndexForLifeEvent(newLifeEvent);

    if (oldI == newI) {
      int index = getIndexForKey(newI, key);
      snapshotKeyLists[newI].set(index, key);
      callback.onLifeEventChanged(newI + 1, index, newLifeEvent);
    } else {
      int oldIndex = getIndexForKey(oldI, key);
      snapshotKeyLists[oldI].remove(oldIndex);
      callback.onLifeEventRemoved(oldI + 1, oldIndex);

      if (oldI < newI) {
        snapshotKeyLists[newI].add(0, key);
        callback.onLifeEventAdded(newI + 1, 0, newLifeEvent);
      } else {
        snapshotKeyLists[newI].add(key);
        callback.onLifeEventAdded(newI + 1, snapshotKeyLists[newI].size() - 1, newLifeEvent);
      }
    }
  }

  @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
    String key = dataSnapshot.getKey();
    LifeEvent oldLifeEvent = snapshotMap.get(key);
    snapshotMap.remove(key);
    int oldI = getDayIndexForLifeEvent(oldLifeEvent);

    int oldIndex = getIndexForKey(oldI, key);
    snapshotKeyLists[oldI].remove(oldIndex);
    callback.onLifeEventRemoved(oldI + 1, oldIndex);
  }

  @Override public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
    String key = dataSnapshot.getKey();
    LifeEvent lifeEvent = getLifeEventFrom(dataSnapshot);
    int i = getDayIndexForLifeEvent(lifeEvent);


    int oldIndex = getIndexForKey(i, key);
    snapshotKeyLists[i].remove(oldIndex);
    int newIndex = previousChildKey == null ? 0 : getIndexForKey(i, previousChildKey) + 1;
    snapshotKeyLists[i].add(newIndex, key);
    callback.onLifeEventRemoved(i + 1, oldIndex);
    callback.onLifeEventAdded(i + 1, newIndex, lifeEvent);
  }

  @Override public void onCancelled(DatabaseError databaseError) {
    Log.w(TAG, databaseError.getMessage(), databaseError.toException());
  }
}

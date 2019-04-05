package me.luwenjie.lazytv.util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A more type-safe event bus made of RxJava's hot observables.
 * <p>
 * By mentioning 'type-safe', we mean that code style
 *
 * @author lujuezhou
 */
public class Ark {
  public static final String TAG = "Ark";
  private static volatile Ark instance;
  private ConcurrentMap<Class, Subject> subjectMap;
  private CopyOnWriteArrayList<Object> stickyEventsList;

  private Ark() {
    subjectMap = new ConcurrentHashMap<>();
    stickyEventsList = new CopyOnWriteArrayList<>();
  }

  public static Ark get() {
    if (instance == null) {
      synchronized (Ark.class) {
        if (instance == null) {
          instance = new Ark();
        }
      }
    }

    return instance;
  }

  public <T> Bus<T> on(Class<T> clazz) {
    return new Bus<>(clazz);
  }

  @SuppressWarnings("unchecked") public void fire(Object object) {
    Subject subject = subjectMap.get(object.getClass());
    if (subject == null) {
      return;
    }
    subject.onNext(object);
  }

  /**
   * fire a sticky event
   *
   * @param object event
   */
  @SuppressWarnings("unchecked") public void fireSticky(Object object) {
    if (!stickyEventsList.contains(object)) {
      stickyEventsList.add(object);
    }
  }

  public void clear(Class eventClazz) {
    subjectMap.remove(eventClazz);
  }

  public void removeSticky(Object o) {
    stickyEventsList.remove(o);
    subjectMap.remove(o.getClass());
  }

  public interface EventCallback<T> {
    /**
     * Function that consumes a event of T
     */
    void onEvent(T event);
  }

  /**
   * Just for unfolding generic type.
   * Workaround for type inferring.
   *
   * @param <S> the delegated generic type.
   */
  public class Bus<S> {
    private final Class<S> eventIndex;

    private Bus(Class<S> event) {
      this.eventIndex = event;
    }

    @SuppressWarnings("unchecked") public Disposable listen(final EventCallback<S> callback) {
      Subject<S> subject = subjectMap.get(eventIndex);
      if (subject == null) {
        subject = PublishSubject.create();
        subjectMap.put(eventIndex, subject);
      }
      return subject.observeOn(AndroidSchedulers.mainThread()).subscribe(callback::onEvent);
    }

    @SuppressWarnings("unchecked")
    public Disposable listen(LifecycleOwner owner, Lifecycle.Event event,
        final EventCallback<S> callback) {
      Subject<S> subject = subjectMap.get(eventIndex);
      if (subject == null) {
        subject = PublishSubject.create();
        subjectMap.put(eventIndex, subject);
      }
      return subject.observeOn(AndroidSchedulers.mainThread())
          .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, event)))
          .subscribe(callback::onEvent,
              throwable -> Gakki.debug(TAG, "throwable = " + throwable.getMessage()));
    }

    @SuppressWarnings("unchecked")
    public Disposable listen(LifecycleOwner owner, final EventCallback<S> callback) {
      return listen(owner, Lifecycle.Event.ON_DESTROY, callback);
    }

    @SuppressWarnings("unchecked")
    public void listenSticky(LifecycleOwner owner, Lifecycle.Event event,
        final EventCallback<S> callback) {
      // find sticky
      if (stickyEventsList.isEmpty()) {
        return;
      }
      // onNext firstly
      PublishSubject<S> subject = PublishSubject.create();

      subject.observeOn(AndroidSchedulers.mainThread())
          .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, event)))
          .subscribe(callback::onEvent);

      for (int i = 0; i < stickyEventsList.size(); i++) {
        S stickyEvent;
        if (eventIndex == stickyEventsList.get(i).getClass()) {
          stickyEvent = (S) stickyEventsList.get(i);
          subject.onNext(stickyEvent);
          subject.onComplete();
          stickyEventsList.remove(stickyEvent);
        }
      }
    }

    @SuppressWarnings("unchecked")
    public void listenSticky(LifecycleOwner owner, final EventCallback<S> callback) {
      listenSticky(owner, Lifecycle.Event.ON_DESTROY, callback);
    }
  }
}
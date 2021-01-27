package com.mycode.base.androidevent;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by kyunghoon on 2019-03-07 <p/>
 *
 * 어떤 생명주기에서 이벤트를 받을지 설정 가능합니다. (default : {@link State#RESUMED}) <br/>
 * 설정한 생명주기 외에 발생한 이벤트는 무시합니다. 무시하지 않으려면 {@link LiveEvent}를 참고해주세요 <br/>
 * Activity or Fragment 가 destroy 되면 Observer 제거 됩니다. <p/>
 */
class DefaultEvent<T> implements Event<T> {

    private static final String TAG = "DefaultEvent";

    private List<EventObserver<T>> mEventObservers = new ArrayList<>();

    private final State DEFAULT_OBSERVABLE_STATE = State.RESUMED;

    private State mObservableState = DEFAULT_OBSERVABLE_STATE;

    private final Object mDataLock = new Object();

    private static final Object NOT_SET = new Object();

    private volatile Object mPendingData = NOT_SET;

    private String mEventTag = "";

    private final Runnable mPostValueRunnable = () -> {
        Object newValue;
        synchronized (mDataLock) {
            newValue = mPendingData;
            mPendingData = NOT_SET;
        }
        setValue((T) newValue);
    };


    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        boolean success = addObserver(owner, observer);
        if (success) {
            owner.getLifecycle().addObserver(new LifecycleObserverImple(owner));
        }
    }

    @Override
    public void setObservableState(@NonNull State sate) {
        mObservableState = sate;
    }

    @MainThread
    @Override
    public synchronized void setValue(T t) {
        assertMainThread(mEventTag, "setValue");
        notifyDataChange(t);
    }

    @Override
    public void postValue(T value) {
        boolean postTask;
        synchronized (mDataLock) {
            postTask = mPendingData == NOT_SET;
            mPendingData = value;
        }
        if (!postTask) {
            return;
        }
        ArchTaskExecutor.getInstance().postToMainThread(mPostValueRunnable);
    }

    private void notifyDataChange(T t) {
        for (EventObserver<T> eo : mEventObservers) {
            if (eo.isAtLeast(mObservableState)) {
                eo.onChanged(t);
            }
        }
    }

    private boolean addObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        for (EventObserver<T> eo : mEventObservers) {
            if (eo.contains(owner)) {
                return false;
            }
        }

        EventObserver<T> eo = new EventObserver<>(owner, observer);
        mEventObservers.add(eo);

        log("add " + eo);
        log("EventObservers size : " + mEventObservers.size());
        return true;
    }

    private void removeObserver(@NonNull LifecycleOwner owner) {
        for (int index = 0 ; index < mEventObservers.size() ; index++) {
            EventObserver<T> eo = mEventObservers.get(index);
            if (eo.contains(owner)) {
                mEventObservers.remove(eo);
                index--;

                log("remove " + eo);
                log("EventObservers size : " + mEventObservers.size());
            }
        }
    }

    final class LifecycleObserverImple implements LifecycleObserver {

        private LifecycleOwner mLifeCycleOwner;

        LifecycleObserverImple(@NonNull LifecycleOwner owner) {
            this.mLifeCycleOwner = owner;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void destroy() {
            log("ON_DESTROY invoke LifecycleObserverImple.destory");
            removeObserver(mLifeCycleOwner);
            this.mLifeCycleOwner.getLifecycle().removeObserver(this);
            this.mLifeCycleOwner = null;
        }
    }

    private static void assertMainThread(String eventName, String methodName) {
        if (!ArchTaskExecutor.getInstance().isMainThread()) {
            Log.e(TAG, "###E " + eventName + " Cannot invoke " + methodName + " on a background thread");
        }
    }

    private void log(String log) {
        StringBuilder builder = new StringBuilder("###E ");
        builder.append(mEventTag)
                .append(" ")
                .append(log);
        Log.d(TAG, builder.toString());
    }

    @Override
    public void setEventTag(String tag) {
        mEventTag = (tag == null) ? "" : tag;
    }
}

package com.mycode.base.androidevent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * Created by kyunghoon on 2019-03-07
 */
final class EventObserver<T> {
    private LifecycleOwner mOwner;
    private Observer<? super T> mObserver;

    EventObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        this.mOwner = owner;
        this.mObserver = observer;
    }

    boolean isAtLeast(Lifecycle.State state) {
        return mOwner.getLifecycle().getCurrentState().isAtLeast(state);
    }

    void onChanged(T t) {
        mObserver.onChanged(t);
    }

    boolean contains(LifecycleOwner owner) {
        return mOwner.equals(owner);
    }

}

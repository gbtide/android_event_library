package com.mycode.base.androidevent;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;

/**
 * Created by kyunghoon on 2019-03-11 <p/>
 *
 * Activity or Fragment 가 {@link Lifecycle.State#STARTED} 일 때 이벤트를 받습니다. <br/>
 * 그 외 주기에서 받은 이벤트도 {@link Lifecycle.State#STARTED} 진입 시점에 갱신됩니다. <br/>
 * Observable State 는 {@link LiveData} 기반이기 때문에, {@link Lifecycle.State#STARTED} 만 지원됩니다. <p/>
 */
class LiveEvent<T> extends LiveData<T> implements Event<T> {

    private static final String TAG = "LiveEvent";

    private String mEventTag = "";

    @Override
    @MainThread
    public void setValue(T value) {
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setObservableState(@NonNull Lifecycle.State sate) {
        Log.e(TAG, getLogPrefix() + " Unsupported Operation (setObservableState)");
    }

    @Override
    public void setEventTag(String tag) {
        mEventTag = (tag == null) ? "" : tag;
    }

    private String getLogPrefix() {
        return "###E " + mEventTag;
    }

}

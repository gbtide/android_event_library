package com.mycode.base.androidevent;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * Created by kyunghoon on 2019-03-11
 */
public interface Event<T> {

    void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    @MainThread
    void setValue(T value);

    /**
     * Main Thread 에서 업데이트 하는 것이면 setValue 를, Worker Thread 에서 업데이트 하는 것이면 이걸 쓰세요 <br/>
     * 참고로, 다수의 Worker Thread 가 동시에 달라붙어서 콜하면 가장 나중 데이터만 notify 됩니다. <br/>
     */
    void postValue(T value);

    /**
     * Activity or Fragment 가 어떤 상태일 때 Event 를 감지할지 결정합니다. <br/>
     * 상태에 대해서는 {@link Lifecycle.State} 참조 <br/>
     */
    void setObservableState(@NonNull Lifecycle.State sate);

    /**
     * 로그 출력 시 어떤 이벤트인지 확인하기 위함입니다. <br/>
     * {@link androidx.lifecycle.LiveData} 내부는 추적이 어려워서 주로 {@link DefaultEvent}에서 쓰입니다. <br/>
     */
    void setEventTag(String tag);

}

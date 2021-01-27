package com.mycode.base.androidevent;

import androidx.lifecycle.Lifecycle;

/**
 * Created by kyunghoon on 2019-03-11
 */
public class EventFactory {

    private static final String TAG = "EventFactory";

    /**
     * 어떤 생명주기에서 이벤트를 받을지 설정 가능합니다. (default : {@link Lifecycle.State#RESUMED}) <br/>
     * 설정한 생명주기 외에 발생한 이벤트는 무시합니다. 무시하지 않으려면 {@link LiveEvent}를 참고해주세요 <br/>
     * Activity or Fragment 가 destroy 되면 Observer 제거 됩니다. <p/>
     *
     * @param eventTag 로깅 시 어느 이벤트에서 왔는지 구분하기 위해 넣는 태그 문자열입니다
     * @param <T> 이벤트 발생 시 리턴받고 싶은 데이터 타입
     * @return
     */
    public static <T> Event<T> createEvent(String eventTag) {
        Event<T> event = new DefaultEvent<>();
        event.setEventTag(eventTag);
        return event;
    }

    public static <T> Event<T> createEvent() {
        return createEvent(null);
    }

    public static <T> Event<T> createEvent(String eventTag, Lifecycle.State state) {
        Event<T> event = new DefaultEvent<>();
        event.setEventTag(eventTag);
        event.setObservableState(state);
        return event;
    }

    /**
     * Activity or Fragment 가 {@link Lifecycle.State#STARTED} 일 때 이벤트를 받습니다. <br/>
     * 그 외 주기에서 받은 이벤트도 {@link Lifecycle.State#STARTED} 진입 시점에 갱신됩니다. <br/>
     * Observable State 는 {@link androidx.lifecycle.LiveData} 기반이기 때문에, {@link Lifecycle.State#STARTED} 만 지원됩니다. <p/>
     *
     * @param eventTag 로깅 시 어느 이벤트에서 왔는지 구분하기 위해 넣는 태그 문자열입니다
     * @param <T> 이벤트 발생 시 리턴받고 싶은 데이터 타입
     * @return
     */
    public static <T> Event<T> createLiveEvent(String eventTag) {
        Event<T> event = new LiveEvent<>();
        event.setEventTag(eventTag);
        return event;
    }

}

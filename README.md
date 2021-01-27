# 안드로이드 Event 라이브러리
### 간략 설명
- LiveData, RX 등을 이벤트로 사용할 수는 있으나, 서비스에서 사용하기에 불편한 점이 있어서 별도의 Event를 만들게 되었습니다.

### 새 라이브러리의 장점
- 각 수신부에서 Disposable 관리를 할 필요가 없습니다.(RX 단점) Activity나 Fragment가 destroy 되면, 스스로 Observer를 끊으므로 별도의 관리가 필요없습니다.
- 새로운 코드로 작성된 DefaultEvent를 사용할 수도, LiveData 기반의 LiveEvent를 사용할 수도 있습니다.
  - 1) DefaultEvent : 특정 생명주기에서 이벤트 수신을 할 수 있습니다. 단, 이벤트 발생 타임에 수신자가 특정 생명주기 안에 있지 않으면 발생 Event를 무시합니다. (LiveData와 다른 점)
  - 2) LiveEvent : LiveData와 메카니즘이 똑같습니다. DefaultEvent와는 달리, 수신자가 STARTED 생명주기 바깥에 있다하더라도 STARTED되는 시점에 최신 이벤트를 전달 받습니다.
  - 두 형태 모두 수신지에서 Activity나 Fragment가 죽었는지 체크할 필요가 없습니다.
  - 두 형태 모두 이벤트 발생(from 복수의 WorkerThread)에 대한 async 처리가 되어있습니다.

### 사용예시
<pre><code>

// 1. 이벤트 정의부
// 이벤트 선언부. 꼭 enum을 쓸 필요는 없습니다. 예시입니다.
public enum StaticEvent {
    NETWORK_ERROR(EventFactory.<Void>createEvent("NETWORK_ERROR")),
    ON_ATTACH_IMAGE(EventFactory.<StaticEventParams.OnAttachStoredImageParam>createEvent("ON_ATTACH_IMAGE"))
    ;
    
    static {
        // 특정 이벤트 생명주기
        NETWORK_ERROR.setObservableState(Lifecycle.State.CREATED);
    }

    StaticEvent(Event event) {
        this.mEvent = event;
        // 공통 이벤트 생명주기
        this.mEvent.setObservableState(Lifecycle.State.RESUMED);
    }

      public <T> void observe(@NonNull LifecycleOwner owner, final Observer<T> observer) {
        try {
            Event<T> event = (Event<T>) mEvent;
            event.observe(owner, observer);
        } catch (Throwable e) {
            logger.e(new InvalidParameterException(e.getMessage()));
        }
    }

    public void setObservableState(Lifecycle.State state) {
        if (state != null) {
            mEvent.setObservableState(state);
        }
    }

    public <T> void fire(T param) {
        if (param == null) {
            mEvent.postValue(null);
            return;
        }

        try {
            Event<T> event = (Event<T>) mEvent;
            event.postValue(param);
        } catch (Throwable e) {
            logger.e(new InvalidParameterException(e.getMessage()));
        }
    }
}
    
// 2. 이벤트 송신부
StaticEvent.NETWORK_ERROR.<Void>fire(null);

// 3. 이벤트 수신부
StaticEvent.NETWORK_ERROR.<Void>observe(this, aVoid -> {
    showDialog(OldDialogHelper.DIALOG_ERROR_NETWORK);
});
</code></pre>

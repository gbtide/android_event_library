package com.mycode.base.androidevent;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;

/**
 * Created by kyunghoon on 2019-03-11
 */
class DefaultTaskExecutor extends TaskExecutor {
    private final Object mLock = new Object();
    private ExecutorService mDiskIO = Executors.newFixedThreadPool(2);

    @Nullable
    private volatile Handler mMainHandler;

    @Override
    public void executeOnDiskIO(Runnable runnable) {
        mDiskIO.execute(runnable);
    }

    @Override
    public void postToMainThread(Runnable runnable) {
        if (mMainHandler == null) {
            synchronized (mLock) {
                if (mMainHandler == null) {
                    mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        mMainHandler.post(runnable);
    }

    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}

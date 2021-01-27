package com.mycode.base.androidevent;

import androidx.annotation.NonNull;

/**
 * Created by kyunghoon on 2019-03-11
 */
class ArchTaskExecutor extends TaskExecutor {
    private static volatile ArchTaskExecutor sInstance;

    @NonNull
    private TaskExecutor mDelegate;

    @NonNull
    private TaskExecutor mDefaultTaskExecutor;

    private ArchTaskExecutor() {
        mDefaultTaskExecutor = new DefaultTaskExecutor();
        mDelegate = mDefaultTaskExecutor;
    }

    @NonNull
    public static ArchTaskExecutor getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (ArchTaskExecutor.class) {
            if (sInstance == null) {
                sInstance = new ArchTaskExecutor();
            }
        }
        return sInstance;
    }


    @Override
    public void executeOnDiskIO(Runnable runnable) {
        mDelegate.executeOnDiskIO(runnable);
    }

    @Override
    public void postToMainThread(Runnable runnable) {
        mDelegate.postToMainThread(runnable);
    }

    @Override
    public boolean isMainThread() {
        return mDelegate.isMainThread();
    }
}

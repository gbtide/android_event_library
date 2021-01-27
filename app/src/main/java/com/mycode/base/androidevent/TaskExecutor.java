package com.mycode.base.androidevent;

import androidx.annotation.NonNull;

/**
 * Created by kyunghoon on 2019-03-11
 */
abstract class TaskExecutor {

    public abstract void executeOnDiskIO(@NonNull Runnable runnable);

    public abstract void postToMainThread(@NonNull Runnable runnable);

    public abstract boolean isMainThread();

}

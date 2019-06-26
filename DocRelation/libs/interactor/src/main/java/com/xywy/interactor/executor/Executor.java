package com.xywy.interactor.executor;

import com.xywy.interactor.interactors.AbstractInteractor;

public interface Executor {
    /**
     * @param interactor The interactor to run.
     */
    void execute(final AbstractInteractor interactor);
    void execute(final Runnable run);
}

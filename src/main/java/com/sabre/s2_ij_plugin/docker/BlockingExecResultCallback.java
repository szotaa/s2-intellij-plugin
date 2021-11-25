package com.sabre.s2_ij_plugin.docker;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BlockingExecResultCallback implements ResultCallback<Frame> {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final ConsoleView consoleView;

    @Override
    public void onStart(Closeable closeable) {
    }

    @Override
    public void onNext(Frame object) {
        consoleView.print(new String(object.getPayload(), StandardCharsets.UTF_8), ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void onError(Throwable throwable) {
        consoleView.print(throwable.getMessage(), ConsoleViewContentType.ERROR_OUTPUT);
    }

    @Override
    public void onComplete() {
        countDownLatch.countDown();
    }

    @Override
    public void close() throws IOException {
        countDownLatch.countDown();
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }
}

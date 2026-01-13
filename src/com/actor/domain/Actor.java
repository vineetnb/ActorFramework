package com.actor.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class Actor implements Runnable {

    private final String id;
    private final Work workFunction;
    private final ConcurrentLinkedQueue<Object> inbox;
    private final ExecutorService dispatcher;
    private volatile boolean isRunning = true;

    public Actor(String id, Work workFunction, ExecutorService dispatcher) {
        this.id = id;
        this.workFunction = workFunction;
        this.dispatcher = dispatcher;
        inbox = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        try {
            Object data;
            while ((data = inbox.poll()) != null) {
                workFunction.execute(data);
            }
        } finally {
            if (isRunning && !inbox.isEmpty()) {
                schedule();
            }
        }
    }

    private void schedule() {
        dispatcher.execute(this);
    }

    public boolean addToInbox(Object data) {
        if (!isRunning) {
            return false;
        }
        boolean isAdded = inbox.offer(data);
        if (isAdded) {
            schedule();
        }
        return isAdded;
    }

    public boolean hasPendingMessages() {
        return !inbox.isEmpty();
    }

    public void stop() {
        isRunning = false;
    }
}

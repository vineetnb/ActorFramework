package com.actor.controller.impl;

import com.actor.controller.IActorSystem;
import com.actor.domain.Actor;
import com.actor.config.ActorConfig;
import com.actor.domain.Work;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ActorSystem implements IActorSystem {

    private ActorConfig config;
    private ConcurrentHashMap<String, Actor> actors;
    private ExecutorService dispatcher;
    private volatile boolean isRunning;

    @Override
    public void init(ActorConfig config) {
        if (config == null) {
            throw new RuntimeException("ActorConfig cannot be null");
        }
        if (isRunning) {
            throw new RuntimeException("ActorSystem already initialized");
        }
        this.config = config;
        actors = new ConcurrentHashMap<>();
        dispatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        isRunning = true;
    }

    @Override
    public String createActor(Work workFunction) {
        if (!isRunning) {
            throw new RuntimeException("ActorSystem not initialized");
        }
        if (actors.size() > config.getMaxActors()) {
            System.err.println("Max Actors reached!!!");
        }
        String actorId = getUniqueId();
        actors.put(actorId, new Actor(actorId, workFunction, dispatcher));

        return actorId;
    }

    @Override
    public boolean send(String actorId, Object message) {
        Actor actor = actors.get(actorId);
        if (actor == null) {
            System.err.println("No Actor found with id / Actor is not running");
            return false;
        }
        return actor.addToInbox(message);
    }

    @Override
    public void shutdown() {
        actors.values().forEach(Actor::stop);
        isRunning = false;
        dispatcher.shutdown();
        try {
            dispatcher.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}

package com.actor.controller;

import com.actor.config.ActorConfig;
import com.actor.domain.Work;

public interface IActorSystem {

    void init(ActorConfig config);

    String createActor(Work workFunction); // Returns an Actor ID

    boolean send(String actorId, Object message); // Adds message to inbox

    void shutdown();
}

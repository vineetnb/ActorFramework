# Actor Framework

A simple lightweight Java implementation of the **Actor Framework** for concurrent and distributed computing.

## Overview

This project implements the Actor Framework, a concurrency paradigm that uses actors as the fundamental unit of computation. Each actor encapsulates state and behavior, communicating with other actors asynchronously through message passing. This model simplifies concurrent programming by eliminating shared state and locks.

## Components

### 1. **ActorConfig**
Manages the configuration of the actor system:
- `maxActors`: Maximum number of actors that can be created
- `defaultInboxSize`: Default inbox capacity for actors (default: 10)

```java
ActorConfig config = new ActorConfig(2, 5); // 2 max actors, 5 inbox size
```

### 2. **IActorSystem**
Interface defining the core operations:
- `init(ActorConfig config)` - Initialize the actor system
- `createActor(Work workFunction)` - Create a new actor with a work function
- `send(String actorId, Object message)` - Send a message to an actor
- `shutdown()` - Gracefully shut down the system

### 3. **ActorSystem**
Concrete implementation of the actor system:
- Uses `ExecutorService` with a fixed thread pool (sized to available processors)
- Manages actors in a thread-safe `ConcurrentHashMap`
- Handles message routing and actor lifecycle

### 4. **Actor**
The fundamental unit of the system:
- Each actor has a unique ID
- Maintains a concurrent inbox (`ConcurrentLinkedQueue`) for incoming messages
- Processes messages sequentially using the provided `Work` function
- Automatically schedules itself for execution when messages arrive
- Thread-safe message handling with no shared mutable state

### 5. **Work**
A functional interface for defining actor behavior (any work):
```java
public interface Work {
    void execute(Object data);
}
```

## Features

**Non-blocking Message Passing** - Actors communicate asynchronously without blocking  
**Thread-safe Operations** - Uses concurrent data structures and thread-safe collections  
**Efficient Task Scheduling** - Actors are scheduled only when they have work to do  
**Graceful Shutdown** - Proper cleanup and resource management  
**Scalable** - Uses a fixed thread pool for optimal resource utilization  

## Usage Example

```java
public static void main(String[] args) {
    System.out.println("Testing Actor Model");

    // Initialize the actor system
    ActorSystem actorSystem = new ActorSystem();
    ActorConfig config = new ActorConfig(2, 5); // 2 max actors, inbox size 5
    actorSystem.init(config);

    // Create an actor that prints messages
    String actorId = actorSystem.createActor(data ->
            System.out.println(Thread.currentThread().getName() + " processing " + data));

    // Send 20 messages to the actor
    for (int i = 1; i <= 20; i++) {
        boolean sent = actorSystem.send(actorId, "message -> " + i);
        System.out.println("Message -> " + i + " is " + (sent ? "sent" : "not sent"));
    }

    // Shutdown the system
    actorSystem.shutdown();
}
```

### Expected Output
```
Testing Actor Model
Message -> 1 is sent
pool-1-thread-1 processing message -> 1
Message -> 2 is sent
...
```

## Key Design Patterns

1. **Message-Driven Architecture** - Actors respond to messages in their inbox
2. **Thread Pool Executor** - Efficient task scheduling using a fixed thread pool
3. **Immutable Messages** - Messages are passed by reference without modification
4. **Location Transparency** - Actors are identified by unique IDs, enabling future distribution
5. **Mailbox Pattern** - Each actor has a queue of messages to process

## Thread Safety

- **ConcurrentLinkedQueue** - Lock-free data structure for inbox
- **ConcurrentHashMap** - Thread-safe actor registry
- **Volatile flags** - For visibility of state changes across threads
- **ExecutorService** - Manages thread creation and task scheduling safely

## System Requirements

- Java 8 or higher
- No external dependencies (uses only Java standard library)

## License

This project is provided as-is for educational and development purposes.

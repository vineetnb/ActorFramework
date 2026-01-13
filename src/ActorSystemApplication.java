import com.actor.config.ActorConfig;
import com.actor.controller.impl.ActorSystem;

public class ActorSystemApplication {
    public static void main(String[] args) {
        System.out.println("----------------Testing Actor Model----------------");

        ActorSystem actorSystem = new ActorSystem();
        ActorConfig actorConfig = new ActorConfig(2, 5);
        actorSystem.init(actorConfig);
        String actorId = actorSystem.createActor(data ->
                System.out.println(Thread.currentThread().getName() +" processing " + data));

        for (int i = 1; i <= 20; i++) {
            boolean sent = actorSystem.send(actorId, "message -> " + i);
            System.out.println("Message -> " + i + " is " + (sent ? "sent" : "not sent"));
        }

        actorSystem.shutdown();
    }
}

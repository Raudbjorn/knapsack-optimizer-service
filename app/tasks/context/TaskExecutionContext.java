package tasks.context;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskExecutionContext extends CustomExecutionContext {

    @Inject
    public TaskExecutionContext(ActorSystem actorSystem) {
        // uses a custom thread pool defined in application.conf
        super(actorSystem, "tasks.dispatcher");
    }
}

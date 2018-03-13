/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

/**
 *
 * @author breeze
 */
public class MyTimerActor extends AbstractActorWithTimers {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private static Object TICK_KEY = "TickKey";

    private static final class FirstTick {
    }

    private static final class Tick {
    }

    public MyTimerActor() {
        getTimers().startSingleTimer(TICK_KEY, new FirstTick(), Duration.create(500, TimeUnit.MILLISECONDS));
    }

    public static Props props() {
        return Props.create(MyTimerActor.class);
    }

    @Override
    public void postStop() {
        log.info("MyTimerActor {} stopped", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FirstTick.class, message -> {
                    // do something useful here
                    log.info("FirstTick-->" + message);

                    getTimers().startPeriodicTimer(TICK_KEY, new Tick(), Duration.create(1, TimeUnit.SECONDS));
                })
                .match(Tick.class, message -> {
                    // do something useful here  
                    log.info("Tick-->" + message);
                    //getContext().getSystem().terminate();
                    //getSelf().tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
                })
                .build();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.client.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.remote.WireFormats;
import akka.routing.FromConfig;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author breeze
 */
public class LookupActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public LookupActor() {

        //ActorSelection actorRef = getContext().actorSelection("akka://master-system@127.0.0.1:2552/parent/master");//akka://master-system@127.0.0.1:2552/user/master
        ActorRef actorRef = getContext().actorOf(FromConfig.getInstance().props(), "helly");
        System.out.println("--->" + actorRef.toString());
        for (int i = 1; i <= 10_0000; i++) {
            actorRef.tell(String.valueOf(i), getSelf());
            if (i % 1000 == 0) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    log.error("", ex);
                }
            }
        }

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(o -> log.info("received unknown message {}", o))
                .build();
    }

}

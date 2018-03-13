/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.actor.StartStopActor1;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author breeze
 */
public class StartStopActorTest {

    public static void main(String[] args) throws Exception {
        
        ActorSystem system = ActorSystem.create("testSystem");

        ActorRef first = system.actorOf(Props.create(StartStopActor1.class), "first");
        first.tell("stop", ActorRef.noSender());

        TimeUnit.SECONDS.sleep(3);
        system.terminate();
    }
}

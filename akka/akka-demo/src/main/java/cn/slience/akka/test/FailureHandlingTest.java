/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.actor.SupervisingActor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author breeze
 */
public class FailureHandlingTest {

    public static void main(String[] args) throws Exception {

        ActorSystem system = ActorSystem.create("testSystem");

        ActorRef supervisingActor = system.actorOf(Props.create(SupervisingActor.class), "supervising-actor");
        supervisingActor.tell("failChild", ActorRef.noSender());

        TimeUnit.SECONDS.sleep(10);
        system.terminate();
    }

}

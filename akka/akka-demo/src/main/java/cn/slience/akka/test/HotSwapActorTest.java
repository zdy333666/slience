/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import cn.slience.akka.actor.HotSwapActor;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author breeze
 */
public class HotSwapActorTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        ActorSystem system = ActorSystem.create("HotSwapActor-system");

        // Create top level supervisor
        ActorRef hotSwapActor = system.actorOf(HotSwapActor.props(), "HotSwapActor");
        hotSwapActor.tell("foo", ActorRef.noSender());
        hotSwapActor.tell("foo", ActorRef.noSender());

        //TimeUnit.SECONDS.sleep(5);
        //system.terminate();
        
        try {
            System.in.read();
        } finally {
            system.terminate();
        }
    }

}

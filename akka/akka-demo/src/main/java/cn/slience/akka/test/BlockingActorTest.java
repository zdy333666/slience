/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.actor.BlockingFutureActor;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class BlockingActorTest {

    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create("BlockingFutureActor-system");

        // Create top level supervisor
        ActorRef actor = system.actorOf(Props.create(BlockingFutureActor.class), "BlockingFutureActor");

        for (int i = 0; i < 100; i++) {
            actor.tell(i, ActorRef.noSender());

        }

//        try {
//            System.in.read();
//        } finally {
//            system.terminate();
//        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.CoordinatedShutdown;
import cn.slience.akka.actor.MyTimerActor;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class MyTimerActorTest {

    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create("myTimer-system");

        // Create top level supervisor
        ActorRef supervisor = system.actorOf(MyTimerActor.props(), "myTimerActor");

        CoordinatedShutdown.get(system).addJvmShutdownHook(()
                -> System.out.println("custom JVM shutdown hook...")
        );
    }

}

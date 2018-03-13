/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.actor.Master;
import cn.slience.akka.actor.Work;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class MasterTest {

    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create("Master-system");

        // Create top level supervisor
        ActorRef actor = system.actorOf(Props.create(Master.class), "Master");

        for (int i = 0; i < 10; i++) {
            actor.tell(new Work(String.valueOf(i)), ActorRef.noSender());
        }
      
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.client;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.client.actor.LookupActor;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class Application {

    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create("client-system");

        ActorRef actor = system.actorOf(Props.create(LookupActor.class), "client");
//        ActorSelection selection = system.actorSelection("akka.tcp://master-system@127.0.0.1:2552/user/master");
//        System.out.println("--->" + selection.toString());
//        for (int i = 0; i < 10; i++) {
//            selection.tell(String.valueOf(i), ActorRef.noSender());
//        }

        //actor.tell("end", ActorRef.noSender());
    }
}

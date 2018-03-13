/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.actor.Swapper;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class SwapperApp {

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("SwapperSystem");
        ActorRef swapper = system.actorOf(Props.create(Swapper.class), "swapper");
        swapper.tell("Swap", ActorRef.noSender()); // logs Hi
        swapper.tell("Swap", ActorRef.noSender()); // logs Ho
        swapper.tell("Swap", ActorRef.noSender()); // logs Hi
        swapper.tell("Swap", ActorRef.noSender()); // logs Ho
        swapper.tell("Swap", ActorRef.noSender()); // logs Hi
        swapper.tell("Swap", ActorRef.noSender()); // logs Ho
        
         try {
            System.in.read();
        } finally {
            system.terminate();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.slience.akka.server.actor.Master;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class Application {

    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create("master-system");

        // Create top level supervisor
        ActorRef actor = system.actorOf(Props.create(Master.class), "master");

    }
}

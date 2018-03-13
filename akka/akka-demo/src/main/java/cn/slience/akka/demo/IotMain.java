/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import cn.slience.akka.iot.actor.IotSupervisor;
import java.io.IOException;

/**
 *
 * @author breeze
 */
public class IotMain {

    public static void main(String[] args) throws IOException {
        
        ActorSystem system = ActorSystem.create("iot-system");
        System.out.println(system.settings());

        try {
            // Create top level supervisor
            ActorRef supervisor = system.actorOf(IotSupervisor.props(), "iot-supervisor");

            System.out.println("Press ENTER to exit the system");
            System.in.read();

        } finally {
            system.terminate();
        }
    }

}

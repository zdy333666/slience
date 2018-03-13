/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import akka.actor.AbstractActor;

/**
 *
 * @author breeze
 */
public class BlockingActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, i -> {
                    Thread.sleep(500); //block for 5 seconds, representing blocking I/O, etc
                    System.out.println("Blocking operation finished: " + i);
                })
                .build();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import akka.actor.AbstractActor;
import scala.Option;

/**
 *
 * @author breeze
 */
public class SupervisedActor extends AbstractActor {

    @Override
    public void preStart() {
        System.out.println(this+"--supervised actor started");
    }

    @Override
    public void postStop() {
        System.out.println(this+"--supervised actor stopped");
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        System.out.println(this+"--supervised actor preRestart");
    }

    public void postRestart(Throwable reason) throws Exception {
        System.out.println(this+"--supervised actor postRestart");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("fail", f -> {
                    System.out.println("supervised actor fails now");
                    throw new Exception("I failed!");
                })
                .build();
    }
}

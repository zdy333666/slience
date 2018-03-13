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
public class Child extends AbstractActor {

    int state = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Exception.class, exception -> {
                    throw exception;
                })
                .match(Integer.class, i -> state = i)
                .matchEquals("get", s -> getSender().tell(state, getSelf()))
                .build();
    }
}

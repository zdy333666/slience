/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import akka.actor.AbstractLoggingActor;

/**
 *
 * @author breeze
 */
public class Swapper extends AbstractLoggingActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Swap", s -> {
                    log().info("Hi");
                    getContext().become(receiveBuilder().
                            matchEquals("Swap", x -> {
                                log().info("Ho");
                                getContext().unbecome(); // resets the latest 'become' (just for fun)
                            }).build(), false); // push on top instead of replace
                }).build();
    }
}

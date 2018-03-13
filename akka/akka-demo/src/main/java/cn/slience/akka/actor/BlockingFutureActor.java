/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import akka.actor.AbstractActor;
import akka.dispatch.Futures;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

/**
 *
 * @author breeze
 */
public class BlockingFutureActor extends AbstractActor {

    ExecutionContext ec = getContext().getSystem().dispatchers().lookup("my-blocking-dispatcher");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, i -> {
                    System.out.println("Calling blocking Future: " + i);
                    Future<Integer> f = Futures.future(() -> {
                        Thread.sleep(5000);
                        System.out.println("Blocking future finished: " + i);
                        return i;
                    }, ec);
                })
                .build();
    }
}

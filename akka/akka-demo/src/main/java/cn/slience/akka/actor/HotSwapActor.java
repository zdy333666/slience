/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 *
 * @author breeze
 */
public class HotSwapActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private AbstractActor.Receive angry;
    private AbstractActor.Receive happy;

    public HotSwapActor() {
        angry
                = receiveBuilder()
                        .matchEquals("foo", s -> {
                            getSender().tell("I am already angry?", getSelf());
                            log.info(angry+"-->"+s);
                        })
                        .matchEquals("bar", s -> {
                            getContext().become(happy);
                        })
                        .build();

        happy = receiveBuilder()
                .matchEquals("bar", s -> {
                    getSender().tell("I am already happy :-)", getSelf());
                })
                .matchEquals("foo", s -> {
                    getContext().become(angry);
                })
                .build();
    }

    public static Props props() {
        return Props.create(HotSwapActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("foo", s -> {
                    log.info(this+"-->"+s);
                    getContext().become(angry);
                }
                )
                .matchEquals("bar", s -> {
                    log.info(this+"-->"+s);
                    getContext().become(happy);
                }
                )
                .build();
    }

}

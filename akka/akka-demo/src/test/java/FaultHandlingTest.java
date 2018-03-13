/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.testkit.TestProbe;
import akka.testkit.ErrorFilter;
import akka.testkit.EventFilter;
import akka.testkit.TestEvent;
import scala.concurrent.duration.Duration;
import static java.util.concurrent.TimeUnit.SECONDS;
import static akka.japi.Util.immutableSeq;
import static akka.pattern.Patterns.ask;
import akka.testkit.javadsl.TestKit;
import cn.slience.akka.actor.Child;
import cn.slience.akka.actor.Supervisor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;

/**
 *
 * @author breeze
 */
public class FaultHandlingTest {

    public FaultHandlingTest() {
    }

    static ActorSystem system;
    Duration timeout = Duration.create(5, SECONDS);

    @BeforeClass
    public static void start() {
        system = ActorSystem.create("FaultHandlingTest");
    }

    @AfterClass
    public static void cleanup() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void mustEmploySupervisorStrategy() throws Exception {
        // code here

        Props superprops = Props.create(Supervisor.class);
        ActorRef supervisor = system.actorOf(superprops, "supervisor");
        ActorRef child = (ActorRef) Await.result(ask(supervisor,
                Props.create(Child.class), 5000), timeout);

        child.tell(42, ActorRef.noSender());
        assert Await.result(ask(child, "get", 5000), timeout).equals(42);
        child.tell(new ArithmeticException(), ActorRef.noSender());
        assert Await.result(ask(child, "get", 5000), timeout).equals(42);

        child.tell(new NullPointerException(), ActorRef.noSender());
        assert Await.result(ask(child, "get", 5000), timeout).equals(0);

        final TestProbe probe = new TestProbe(system);
        probe.watch(child);
        child.tell(new IllegalArgumentException(), ActorRef.noSender());
        probe.expectMsgClass(Terminated.class);

    }

}

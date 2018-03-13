package cn.slience.akka.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import cn.slience.akka.iot.actor.Device;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
public class DeviceTest {

//    @Test
//    public void testReplyWithEmptyReadingIfNoTemperatureIsKnown() {
//         ActorSystem system = ActorSystem.create("iot-system");
//        TestKit probe = new TestKit(system);
//        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));
//        deviceActor.tell(new Device.ReadTemperature(42L), probe.g.getRef());
//        Device.RespondTemperature response = probe.expectMsgClass(Device.RespondTemperature.class);
//        assertEquals(42L, response.requestId);
//        assertEquals(Optional.empty(), response.value);
//    }

}

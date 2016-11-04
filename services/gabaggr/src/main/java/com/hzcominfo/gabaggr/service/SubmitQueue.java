/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.service;

import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.hzcominfo.gabaggr.pojo.SubmitInput;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author cominfo4
 */
public class SubmitQueue {

    private static final BlockingQueue<SubmitInput> queue = new LinkedBlockingQueue<>(GABAggrConfiguration.submit_queue_max_size);

    public static boolean offer(SubmitInput tuple) {
        return queue.offer(tuple);
    }

    public static void put(SubmitInput tuple) throws InterruptedException {
        queue.put(tuple);
    }

    public static SubmitInput take() throws InterruptedException {
        return queue.take();
    }

    public static boolean isEmpty() {
        return queue.isEmpty();
    }

}

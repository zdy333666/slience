/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.fork.join.demo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 *
 * @author breeze
 */
public class Test {

    public static void main(String[] args) throws Exception {
        // 创建随机数组成的数组:
        long[] array = new long[4000];
        fillRandom(array);
        // fork/join task:
        ForkJoinPool fjp = new ForkJoinPool(4); // 最大并发数4
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        Long result = fjp.invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }

    public static void fillRandom(long[] array) {
        int len = array.length;
        for (int i = 0; i < len; i++) {
            array[i] = i;
        }
    }
}

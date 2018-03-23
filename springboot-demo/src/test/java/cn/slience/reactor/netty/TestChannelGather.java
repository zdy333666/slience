/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.reactor.netty;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author breeze
 */
public class TestChannelGather {

    public static void main(String[] args) {
        gather();
    }

    public static void gather() {
        ByteBuffer header = ByteBuffer.allocate(10);
        ByteBuffer body = ByteBuffer.allocate(10);

        byte[] b1 = {'0', '1'};
        byte[] b2 = {'2', '3'};
        header.put(b1);
        body.put(b2);

        ByteBuffer[] buffs = {header, body};

        FileOutputStream os = null;
        try {
            os = new FileOutputStream("src/main/resources/scattingAndGather.txt");
            FileChannel channel = os.getChannel();
            channel.write(buffs);
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}

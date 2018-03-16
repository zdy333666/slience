/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.reactor.netty;

import java.io.IOException;
import org.springframework.web.client.RestTemplate;
import org.xerial.snappy.Snappy;

/**
 *
 * @author breeze
 */
public class TestSnappy {

    public static void main(String[] args) {

        String str = new RestTemplate().getForObject("https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/#boot-features-cors", String.class);

        System.out.println("str length:" + str.getBytes().length);

        byte[] bytes = compressHtml(str);
        System.out.println("compressHtml length:" + bytes.length);

        str = decompressHtml(bytes);
        System.out.println("decompressHtml length:" + str.getBytes().length);

    }

    public static byte[] compressHtml(String html) {
        try {
            return Snappy.compress(html.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompressHtml(byte[] bytes) {
        try {
            return new String(Snappy.uncompress(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

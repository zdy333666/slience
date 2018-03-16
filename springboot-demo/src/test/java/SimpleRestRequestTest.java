
import org.springframework.web.client.RestTemplate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
public class SimpleRestRequestTest {

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        long stime = System.currentTimeMillis();
        for (int n = 0; n < 100; n++) {
            restTemplate.getForEntity("http://localhost:8080/cache", String.class);
        }
        long etime = System.currentTimeMillis();

        System.out.println("spen time:" + (etime - stime) + " ms");
    }

}

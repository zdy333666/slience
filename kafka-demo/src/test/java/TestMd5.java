
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
public class TestMd5 {

    private static final Logger logger = LoggerFactory.getLogger(TestMd5.class);

    public static void main(String[] args) {

        Set<String> md5Set = new HashSet<>();

        long st = System.currentTimeMillis();
        for (int i = 0; i < 1000_0000; i++) {
            String str = UUID.randomUUID().toString();
            md5Set.add(MD5Util.MD5(str));
        }
        long nt = System.currentTimeMillis();

        logger.info("spend time:{} ms", nt - st);
        logger.info("md5Set size:{}", md5Set.size());
    }

}

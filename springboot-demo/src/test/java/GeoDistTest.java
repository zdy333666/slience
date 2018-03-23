
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
public class GeoDistTest {

    private static final Logger logger = LoggerFactory.getLogger(GeoPosTest.class);

    //@Test
    public static void main(String[] args) {

        Jedis jedis = null;
        try {
            jedis = new Jedis("192.168.10.85", 6379);
            jedis.select(15);

            Double distance = jedis.geodist("OLI_TIRS_GEO", "LC81470282017263LGN00", "LC81290412013222LGN00", GeoUnit.MI);

            logger.info("distance:{} m", distance);

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

}

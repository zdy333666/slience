
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
public class GeoRadiusTest {

    private static final Logger logger = LoggerFactory.getLogger(GeoPosTest.class);

    //@Test
    public static void main(String[] args) {

        Jedis jedis = null;
        try {
            jedis = new Jedis("192.168.10.85", 6379);
            jedis.select(15);

            List<GeoRadiusResponse> items = jedis.georadiusByMember("OLI_TIRS_GEO", "LC81290412013222LGN00", 1000, GeoUnit.M,
                    GeoRadiusParam.geoRadiusParam().withDist().withCoord().sortAscending().count(100));

            AtomicInteger counter = new AtomicInteger(0);
            for (GeoRadiusResponse item : items) {
                logger.info("count:{} -- member:{} distance:{} coordinate:{}",
                        counter.incrementAndGet(), item.getMemberByString(), item.getDistance(), item.getCoordinate());
            }

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

}

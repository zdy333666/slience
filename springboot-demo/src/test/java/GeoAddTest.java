
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class GeoAddTest {

    private static final Logger logger = LoggerFactory.getLogger(GeoAddTest.class);

    //@Test
    public static void main(String[] args) {

        Jedis jedis = null;
        try {
            jedis = new Jedis("192.168.10.85", 6379);
            jedis.select(15);

            Set<String> keys = jedis.hkeys("OLI_TIRS");
            logger.info("keys size:{}", keys.size());

            AtomicInteger counter = new AtomicInteger(0);
            ObjectMapper mapper = new ObjectMapper();

            for (String key : keys) {
                String value = jedis.hget("OLI_TIRS", key);
                Map<String, Object> item = mapper.readValue(value, Map.class);

                //logger.info("item:{}", item);
                double longitude = (double) item.get("ct_long");
                double latitude = (double) item.get("ct_lat");
                String member = (String) item.get("dataid");

                logger.info("count:{} -- longitude:{} latitude:{} member:{}", counter.incrementAndGet(), longitude, latitude, member);
                jedis.geoadd("OLI_TIRS_GEO", longitude, latitude, member);
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

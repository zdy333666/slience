
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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
public class GeoDatasetQueryTest {

    private static final Logger logger = LoggerFactory.getLogger(GeoDatasetQueryTest.class);

    //@Test
    public static void main(String[] args) throws IOException, InterruptedException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.set("Cookie", "upost=\"up:9nmptyYlaBkC9Sa3nEpjSh_ASzUL_P99tMcU1ULIufSLsx-wy0Aq0isSG6XnaI6Y97E\"; csrftoken=HYVU5LU49iXWM3T6CpORUDJaD0qWjiMR");
        headers.set("DNT", "1");
        headers.setOrigin("http://www.gscloud.cn");
        headers.set("X-CSRFToken", "HYVU5LU49iXWM3T6CpORUDJaD0qWjiMR");

        Map<String, Object> sortItem = new LinkedHashMap<>();
        sortItem.put("id", "id"); //datadate
        sortItem.put("sort", "asc");

        List<Map<String, Object>> sortSet = new LinkedList<>();
        sortSet.add(sortItem);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("datatype", "OLI_TIRS");

        AtomicInteger counter = new AtomicInteger(0);
        AtomicInteger offset = new AtomicInteger(-2000);
        ObjectMapper mapper = new ObjectMapper();
        int pageSize = 100;
        int total = 245973;

        Jedis jedis = new Jedis("192.168.10.85", 6379);
        jedis.select(15);

        ExecutorService executors = Executors.newFixedThreadPool(1);
        while (offset.addAndGet(pageSize) < total) {
            int curr_offset = offset.get();
            executors.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        Map<String, Object> tableInfo = new LinkedHashMap<>();
                        tableInfo.put("offset", curr_offset);
                        tableInfo.put("pageSize", pageSize);
                        //        tableInfo.put("totalPage", 24598);
                        //        tableInfo.put("totalSize", 245973);
                        tableInfo.put("sortSet", sortSet);
                        tableInfo.put("filterSet", new LinkedHashMap<>());

                        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                        params.add("tableInfo", mapper.writeValueAsString(tableInfo));
                        params.add("data", mapper.writeValueAsString(data));
                        params.add("datatype", "OLI_TIRS");

                        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

                        long startTime = System.currentTimeMillis();
                        Map<String, Object> result = new RestTemplate().postForObject("http://www.gscloud.cn/sources/query_dataset/411", request, Map.class);
                        logger.info("spend time:{} ms", System.currentTimeMillis() - startTime);

                        List<Map<String, Object>> result_data = (List<Map<String, Object>>) result.remove("data");
                        logger.info("result:{}", result);
                        logger.info("result data size:{}", result_data.size());

                        if (result_data.isEmpty()) {
                            return;
                        }

                        Map<String, String> batch = new LinkedHashMap<>();
                        for (Map<String, Object> item : result_data) {
                            String id = ((Integer) item.get("id")).toString();
                            batch.put(id, mapper.writeValueAsString(item));
                        }
                        jedis.hmset("OLI_TIRS", batch);

                        int curr_count = counter.addAndGet(result_data.size());
                        logger.info("curr_count:{}", curr_count);

                    } catch (Exception ex) {

                    }
                }

            });
        }

        while (counter.get() < total) {
            TimeUnit.SECONDS.sleep(1);
        }
        executors.shutdown();

        jedis.close();
    }

}

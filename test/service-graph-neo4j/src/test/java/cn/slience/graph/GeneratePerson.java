/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.graph;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author cominfo4
 */
public class GeneratePerson {

    public static void main(String[] args) {

        MongoClient client = null;
        try {
            Logger log = LoggerFactory.getLogger(GeneratePerson.class);

            DataSource source = new DriverManagerDataSource("jdbc:neo4j://localhost:7474?user=neo4j,password=123456");
            JdbcTemplate template = new JdbcTemplate(source);

            String GET_PERSON_QUERY = "MATCH (n:Person {key:{1}}) RETURN n.zjhm LIMIT 1";
            String ADD_PERSON_QUERY = "CREATE (n:Person {zjhm: {1}})";

            MongoClientURI clientURI = new MongoClientURI("mongodb://hzga:hzga5678@hzga4:30012/hzga");
            client = new MongoClient(clientURI);
            DBCursor cursor = client.getDB(clientURI.getDatabase()).getCollection("gazhk_CZRK").find(new BasicDBObject(), new BasicDBObject().append("GMSFHM", 1));
            cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

            int n = 0;
            long start = 0;
            while (cursor.hasNext()) {
                n++;

                DBObject doc = cursor.next();
                String key = (String) doc.get("GMSFHM");
                if (key == null || key.trim().isEmpty()) {
                    continue;
                }

                start = System.currentTimeMillis();

                if (!(template.queryForList(GET_PERSON_QUERY, key).isEmpty())) {
                    continue;
                }

                template.update(ADD_PERSON_QUERY, key);

                if (n % 2000 == 0) {
                    log.info(new StringBuilder().append(n).append(" ---------- spend time : ").append(System.currentTimeMillis() - start).append(" ms").toString());
                }

            }
            cursor.close();
        } catch (UnknownHostException ex) {
            java.util.logging.Logger.getLogger(GeneratePerson.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (client != null) {
            client.close();
        }

    }

}

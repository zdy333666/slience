/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author breeze
 */
public class NewEmptyJUnitTest {

    public NewEmptyJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {

        List<Object> list = new ArrayList();
        list.add("11");
        list.add("22");
        list.add("33");

        final StringBuilder builder = new StringBuilder();
        builder.append("Batch{list=");
        list.stream().forEachOrdered(e -> {
            builder.append(e);
            builder.append(",");
        });
        int len = builder.length();
        builder.replace(len - 1, len, "}");

        System.out.println(builder);

    }
}

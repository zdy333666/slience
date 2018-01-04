
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author breeze
 */
public class MsgpackTest {

    public static void main(String[] args) {

        List<String> list =new ArrayList<>();
        list.add("111");
        list.add("222");
        list.add("333");
        
        MessagePack msgpack =new MessagePack();
        
        try {
            byte[] raw=msgpack.write(list);
            System.out.println("raw -->"+raw.length);
            
            List<String> dst =msgpack.read(raw, Templates.tList(Templates.TString));
            System.out.println("dst -->"+dst);
            
            IntStream stream1 = IntStream.range(0, 100);
            OfInt it =stream1.iterator();
            while(it.hasNext()){
                System.out.println("next -->"+it.nextInt());
            }
            


            
            
        } catch (IOException ex) {
            Logger.getLogger(MsgpackTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}

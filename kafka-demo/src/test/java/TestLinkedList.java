
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
public class TestLinkedList {

    public static void main(String[] args) {

        LinkedList<String> list = new LinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println(list.poll());
        System.out.println(list);
        
//        Iterator it = list..descendingIterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }

//        list.addLast("11");
//        System.out.println(list);
//        Spliterator<String> a = list.spliterator();
//        Spliterator<String> b = a.trySplit();
//        Spliterator<String> c = b.trySplit();
//        c.forEachRemaining(str -> {
//            System.out.println(str);
//        });
//        System.out.println("-------------------------");
//        b.forEachRemaining(str -> {
//            System.out.println(str);
//        });
//        Iterator it =list.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next());
//        }
//        ListIterator listIt = list.listIterator(3);
//        while (listIt.hasNext()) {
//            System.out.println(listIt.next());
//        }
//        list.set(0, "00");
//        System.out.println(list);
    }

}

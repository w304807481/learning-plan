package com.github.opensharing.archdesign.pattern.iterator;

public class TestIterator {

    public static void main(String[] args) {
       /* Collection c = new ArrayList();
        for(int i=0;i<15;i++) {
        	c.add(new Cat(i));
        }
        System.out.println(c.size());*/

        Collection c = new LinkedList();
        for (int i = 0; i < 15; i++) {
            c.add(new Cat(i));
        }
        System.out.println(c.size());

        Iterator it = c.iterator();
        while (it.hasNext()) {
            Cat cat = (Cat) it.next();
            System.out.print(cat + " ");

        }
    }

}

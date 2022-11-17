import java.util.ArrayList;

public class MyListTest {
    public static void main(String[] args) {
        ArrayList obj1 = new ArrayList();
        obj1.add(0,"A");
        obj1.add(1,"B");
        obj1.add(2,'C');
        obj1.remove(2);
        obj1.remove("B");
        System.out.println(obj1);
    }
}

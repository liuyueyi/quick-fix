package git.hui.fix.test.ognl.bean;

/**
 * Created by @author yihui in 09:06 19/11/28.
 */
public class StaticDemo {

    private static int num = (int) (Math.random() * 100);

    public static int showDemo(int a) {
        System.out.println("static show demo: " + a);
        return a;
    }
}

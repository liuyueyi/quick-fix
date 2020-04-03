package git.hui.fix.test;

import com.github.liuyueyi.fix.core.endpoint.ServletFixEndPoint;

/**
 * Created by @author yihui in 15:43 19/8/29.
 */
public class DefaultServletFixEndPointTest {
    public static void main(String[] args) throws InterruptedException {
        ServletFixEndPoint.getInstance();
        Thread.sleep(20 * 3600);
    }
}

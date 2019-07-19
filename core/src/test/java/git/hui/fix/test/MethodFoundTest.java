package git.hui.fix.test;


import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.reflect.ReflectUtil;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by @author yihui in 09:40 19/3/14.
 */
public class MethodFoundTest {

    public String rand(String name, int seed) {
        return name + " | " + seed;
    }

    public static class Ac {
        String name = UUID.randomUUID().toString();

        private void rand(String name) {
            System.out.println("ac rand");
        }
    }

    public static class Bc extends Ac {
        int age = 20;
        String name = age + "|" + super.name;

        private void rand(String name, int seed) {
            System.out.println(name + " | " + seed);
        }
    }

    public void pc(Ac c) {
        System.out.println(c.name);
    }

    public static Method getMethod(Class clz, String method, Object[] args) {
        try {
            Class[] paramsClz = new Class[args.length];
            int i = 0;
            for (Object o : args) {
                paramsClz[i++] = o.getClass();
            }

            return clz.getDeclaredMethod(method, paramsClz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testBasicGetMethod() {
        Class clz = MethodFoundTest.class;
        String method = "rand";
        Object[] args = new Object[]{"hello", 123};
        System.out.println(getMethod(clz, method, args));

        Ac bc = new Bc();
        System.out.println(getMethod(clz, "pc", new Object[]{bc}));

        Ac ac = new Ac();
        System.out.println(getMethod(clz, "pc", new Object[]{ac}));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testReflectMethodGet() {
        Class clz = MethodFoundTest.class;
        String method = "rand";

        ImmutablePair<Type, Object>[] args = new ImmutablePair[2];
        args[0] = ImmutablePair.of(String.class, "hello");
        args[1] = ImmutablePair.of(Integer.class, 123);

        System.out.println(ReflectUtil.getMethod(clz, method, args));

        Ac bc = new Bc();
        System.out.println(ReflectUtil.getMethod(clz, "pc", new ImmutablePair[]{ImmutablePair.of(Ac.class, bc)}));

        Ac ac = new Ac();
        System.out.println(ReflectUtil.getMethod(clz, "pc", new ImmutablePair[]{ImmutablePair.of(Ac.class, ac)}));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSuperMethodGet() {
        Class clz = Bc.class;
        String method = "rand";
        ImmutablePair<Type, Object>[] arg1 =
                new ImmutablePair[]{ImmutablePair.of(String.class, "name"), ImmutablePair.of(int.class, 1)};
        ImmutablePair<Type, Object>[] arg2 = new ImmutablePair[]{ImmutablePair.of(String.class, "name"),};

        System.out.println(ReflectUtil.getMethod(clz, method, arg1));
        System.out.println(ReflectUtil.getMethod(clz, method, arg2));
    }

    @Test
    public void testParamNameFound() throws NoSuchMethodException {
        Method method = MethodFoundTest.class.getMethod("rand", String.class, int.class);
        Parameter[] parameters = method.getParameters();

        for (Parameter p : parameters) {
            System.out.println(p.getName());
        }
    }
}

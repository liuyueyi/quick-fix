package git.hui.fix.test.ognl;

import com.alibaba.fastjson.JSON;
import git.hui.fix.test.ognl.bean.PrintDemo;
import git.hui.fix.test.ognl.model.ADemo;
import ognl.*;
import org.junit.Test;

import java.util.Map;

/**
 * Created by @author yihui in 16:33 19/11/4.
 */
public class OgnlTest {

    @Test
    public void testOgnl() throws OgnlException {
        ADemo a = new ADemo();
        a.setName("yihui");
        a.setAge(10);

        PrintDemo print = new PrintDemo();
        print.setPrefix("ognl");
        print.setADemo(a);


        // 构建一个OgnlContext对象
        // 扩展，支持传入class类型的参数
        OgnlContext context = (OgnlContext) Ognl
                .createDefaultContext(this, new DefaultMemberAccess(true), new DefaultClassResolver(),
                        new DefaultTypeConverter() {
                            public Object convertValue(Map context, Object value, Class toType) {
                                if (value instanceof String && ((String) value).startsWith("class@")) {
                                    try {
                                        String sub = ((String) value).substring(6, ((String) value).length());
                                        return JSON.parseObject(sub, toType);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                return OgnlOps.convertValue(value, toType);
                            }
                        });
        context.setRoot(print);
        context.put("print", print);
        context.put("a", a);


        // 普通实例方法调用
        Object ans = Ognl.getValue(Ognl.parseExpression("#a.name"), context, context.getRoot());
        System.out.println(ans);


        // 静态类方法调用
        ans = Ognl.getValue(Ognl.parseExpression("@git.hui.fix.test.ognl.bean.StaticDemo@showDemo(20)"), context,
                context.getRoot());
        System.out.println(ans);

        // class传参
        ans = Ognl.getValue(Ognl.parseExpression(
                "#print.print(\"{'name':'xx', 'age': 20}\", @git.hui.fix.test.ognl.model.ADemo@class)"), context,
                context.getRoot());
        System.out.println(ans);

        // class传参
        ans = Ognl.getValue(Ognl.parseExpression("#print.print(\"{'name':'haha', 'age': 10}\", #a.getClass())"),
                context, context.getRoot());
        System.out.println(ans);

        // 枚举传参
        ans = Ognl.getValue(
                Ognl.parseExpression("#print.print(\"print enum\", @git.hui.fix.test.ognl.model.OgnlEnum@CONSOLE)"),
                context, context.getRoot());
        System.out.println(ans);

        // null传递
        ans = Ognl.getValue(Ognl.parseExpression("#print.print(null)"), context, context.getRoot());
        System.out.println(ans);

        // 对象传递，借助json序列化方式
        ans = Ognl.getValue(Ognl.parseExpression(
                "#print.print(\"序列化: \", @com.alibaba.fastjson.JSON@parseObject(\"{'name':'haha', 'age': 10}\", " +
                        "@git.hui.fix.test.ognl.model.ADemo@class))"), context, context.getRoot());
        System.out.println(ans);


        ans = Ognl.getValue(Ognl.parseExpression("#print.print(\"class@{'name':'dd', 'age': 10}\")"), context,
                context.getRoot());
        System.out.println(ans);

        ans = Ognl.getValue(Ognl.parseExpression(
                "#print.print(\"---\", " + "#@git.hui.fix.test.ognl.model.ADemo@{'name':'dd', 'age': 10})"), context,
                context.getRoot());
        System.out.println(ans);

    }


    @Test
    public void testBean() throws OgnlException {
        ADemo a = new ADemo();
        a.setName("yihui");
        a.setAge(10);

        PrintDemo print = new PrintDemo();
        print.setPrefix("ognl");
        print.setADemo(a);


        // 构建一个OgnlContext对象
        // 扩展，支持传入class类型的参数
        OgnlContext context = (OgnlContext) Ognl
                .createDefaultContext(this, new DefaultMemberAccess(true), new DefaultClassResolver(),
                        new DefaultTypeConverter() {
                            public Object convertValue(Map context, Object value, Class toType) {
                                if (value instanceof String && ((String) value).startsWith("Class@")) {
                                    try {
                                        String sub = ((String) value).substring(6, ((String) value).length());
                                        return JSON.parseObject(sub, toType);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                return OgnlOps.convertValue(value, toType);
                            }
                        });
        context.setRoot(print);
        context.put("print", print);
        context.put("a", a);

        Object s = Ognl.getValue(Ognl.parseExpression("#a.name"), context, context.getRoot());
        System.out.println(s);

        Object ex = Ognl.getValue(Ognl.parseExpression("#{'name':'dd', 'age': 10}"), context, context.getRoot());
        System.out.println(ex);

        ex = Ognl.parseExpression("new git.hui.fix.test.ognl.model.ADemo(\"test\", 20)");
        System.out.println(ex);

        ex = Ognl.parseExpression("(#demo=new git.hui.fix.test.ognl.model.ADemo(), #demo.setName(\"一灰灰\"), #demo)");
        System.out.println(ex);

        ex = Ognl.parseExpression(
                "#print.print(\"---\", #@git.hui.fix.test.ognl.model.ADemo@{'name':'dd', 'age': 10})");
        Object ans = Ognl.getValue(ex, context, context.getRoot());
        System.out.println(ans);
    }

}

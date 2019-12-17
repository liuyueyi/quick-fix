package git.hui.fix.test.ognl;

import com.alibaba.fastjson.JSON;
import git.hui.fix.test.ognl.bean.BDemo;
import git.hui.fix.test.ognl.bean.PrintDemo;
import git.hui.fix.test.ognl.bean.ADemo;
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

        BDemo b = new BDemo();
        b.setName("b name");
        b.setAge(20);
        b.setAddress("测试ing");
        context.put("b", b);

        // 普通实例方法调用
        Object ans =
                Ognl.getValue(Ognl.parseExpression("#print.sayHello(\"一灰灰blog\", 18)"), context, context.getRoot());
        System.out.println("实例方法执行： " + ans);

        ans = Ognl.getValue(Ognl.parseExpression("#a.name=\"一灰灰Blog\""), context, context.getRoot());
        System.out.println("实例属性设置： " + ans);

        ans = Ognl.getValue(Ognl.parseExpression("#a.name"), context, context.getRoot());
        System.out.println("实例属性访问： " + ans);

        ans = Ognl.getValue(Ognl.parseExpression("#b.name"), context, context.getRoot());
        System.out.println("实例父类属性访问：" + ans);

        System.out.println("---------------- 分割线 -------------");

        // 静态类方法调用
        ans = Ognl.getValue(Ognl.parseExpression("@git.hui.fix.test.ognl.bean.StaticDemo@showDemo(20)"), context,
                context.getRoot());
        System.out.println("静态类方法执行：" + ans);

        ans = Ognl.getValue(Ognl.parseExpression("@git.hui.fix.test.ognl.bean.StaticDemo@num"), context,
                context.getRoot());
        System.out.println("静态类成员访问：" + ans);

        try {
            ans = Ognl.getValue(Ognl.parseExpression("@git.hui.fix.test.ognl.bean.StaticDemo@num=1314"), context,
                    context.getRoot());
            System.out.println("静态类成员设置：" + ans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("---------------- 分割线 -------------");

        // class传参
        ans = Ognl.getValue(Ognl.parseExpression(
                "#print.print(\"{'name':'xx', 'age': 20}\", @git.hui.fix.test.ognl.bean.ADemo@class)"), context,
                context.getRoot());
        System.out.println("class 参数方法执行：" + ans);

        // class传参
        ans = Ognl.getValue(Ognl.parseExpression("#print.print(\"{'name':'haha', 'age': 10}\", #a.getClass())"),
                context, context.getRoot());
        System.out.println("class 参数方法执行：" + ans);

        System.out.println("---------------- 分割线 -------------");


        // 枚举传参
        ans = Ognl.getValue(
                Ognl.parseExpression("#print.print(\"print enum\", @git.hui.fix.test.ognl.model.OgnlEnum@CONSOLE)"),
                context, context.getRoot());
        System.out.println("枚举参数方法执行：" + ans);
        System.out.println("---------------- 分割线 -------------");

        // null传递
        ans = Ognl.getValue(Ognl.parseExpression("#print.print(null)"), context, context.getRoot());
        System.out.println("null 传参：" + ans);

        ans = Ognl.getValue(Ognl.parseExpression("#print.print(null, null)"), context, context.getRoot());
        System.out.println("null 传参：" + ans);
        System.out.println("---------------- 分割线 -------------");

        // 对象传递，借助json序列化方式
        ans = Ognl.getValue(Ognl.parseExpression(
                "#print.print(\"序列化: \", @com.alibaba.fastjson.JSON@parseObject(\"{'name':'haha', 'age': 10}\", " +
                        "@git.hui.fix.test.ognl.bean.ADemo@class))"), context, context.getRoot());
        System.out.println(ans);


        ans = Ognl.getValue(Ognl.parseExpression("#print.print(\"class@{'name':'dd', 'age': 10}\")"), context,
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

        ex = Ognl.parseExpression("new git.hui.fix.test.ognl.bean.ADemo(\"test\", 20)");
        System.out.println(ex);

        ex = Ognl.parseExpression("(#demo=new git.hui.fix.test.ognl.bean.ADemo(), #demo.setName(\"一灰灰\"), #demo)");
        System.out.println(ex);

        System.out.println("---------- 分割 -----------");

        ex = Ognl.parseExpression("#print.print(\"对象构建\", new git.hui.fix.test.ognl.bean.ADemo(\"test\", 20))");
        Object ans = Ognl.getValue(ex, context, context.getRoot());
        System.out.println("对象传参：" + ans);

        ex = Ognl.parseExpression(
                "#print.print(\"对象构建\", (#demo=new git.hui.fix.test.ognl.bean.ADemo(), #demo.setName(\"一灰灰\"), #demo))");
        ans = Ognl.getValue(ex, context, context.getRoot());
        System.out.println("对象传参：" + ans);

        ex = Ognl.parseExpression("#demo.name");
        ans = Ognl.getValue(ex, context, context.getRoot());
        System.out.println("临时对象：" + ans);

        System.out.println("---------- 分割 -----------");


        ex = Ognl.parseExpression("#print.print({1, 3, 5})");
        ans = Ognl.getValue(ex, context, context.getRoot());
        System.out.println("List传参：" + ans);

        ex = Ognl.parseExpression("#print.print(#{\"A\": 1, \"b\": 3, \"c\": 5})");
        ans = Ognl.getValue(ex, context, context.getRoot());
        System.out.println("Map传参：" + ans);
        System.out.println("---------- 分割 -----------");


        ans = Ognl.getValue(Ognl.parseExpression("1 + 3 + 4"), context, context.getRoot());
        System.out.println("表达式执行: " + ans);

        ans = Ognl.getValue(Ognl.parseExpression("#fact = :[#this<=1? 1 : #this*#fact(#this-1)], #fact(3)"), context,
                context.getRoot());
        System.out.println("lambda执行: " + ans);

        //        内部类的使用姿势
        ans = Ognl.getValue(Ognl.parseExpression("@git.hui.fix.test.ognl.OgnlTest$InnerClz@class"), context,
                context.getRoot());
        System.out.println("内部类: " + ans);

        // 静态类成员修改
        ans = Ognl.getValue(
                Ognl.parseExpression("@git.hui.fix.test.ognl.OgnlTest$InnerClz@class.getField(\"age\").set(null, 30)"),
                context, context.getRoot());
        System.out.println("静态成员: " + ans);
    }


    public static class InnerClz {
        public static Integer age = 10;
    }

}

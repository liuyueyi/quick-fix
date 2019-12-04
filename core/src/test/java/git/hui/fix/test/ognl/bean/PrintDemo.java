package git.hui.fix.test.ognl.bean;

import com.alibaba.fastjson.JSON;
import git.hui.fix.test.ognl.model.OgnlEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by @author yihui in 16:47 19/11/4.
 */
@Data
public class PrintDemo {

    private String prefix;

    private ADemo aDemo;

    public void sayHello(String name, int age) {
        System.out.println("name: " + name + " age: " + age);
    }

    private void print(ADemo a) {
        System.out.println(prefix + " => " + a);
    }

    public <T> T print(String str, Class<T> clz) {
        T obj = JSON.parseObject(str, clz);
        System.out.println("class: " + obj);
        return obj;
    }

    public void print(String str, String clz) {
        System.out.println("str2a: " + str + " clz: " + clz);
    }

    public void print(String str, OgnlEnum ognlEnum) {
        System.out.println("enum: " + str + ":" + ognlEnum);
    }

    public void print(String str, ADemo a) {
        System.out.println("obj: " + str + ":" + a);
    }

    public void show(Class clz) {
        System.out.println(clz.getName());
    }

    public void print(List<Integer> args) {
        System.out.println(args);
    }

    public void print(Map<String, Integer> args) {
        System.out.println(args);
    }
}

package git.hui.fix.test;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.core.reflect.ArgumentParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author yihui in 22:14 19/3/11.
 */
public class ArgumentParserTest {

    @Test
    public void testArugmentParser() {
        String[] params = new String[]{"Hello World", "int#120", "long#330", "BigDecimal#1.2", "boolean#true"};

        Object[] result = ArgumentParser.parse(params);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void testPOJO() {
        PoJo pojo = new PoJo("一灰灰", 18, true);
        String s = JSON.toJSONString(pojo);

        String[] params = new String[]{"git.hui.fix.test.PoJo#" + s};

        Object[] result = ArgumentParser.parse(params);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void testClass() {
        String[] params = new String[]{"Class#git.hui.fix.test.PoJo"};
        Object[] result = ArgumentParser.parse(params);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void testGenericClass() {
        Map<String, Long> demo = new HashMap<>();
        demo.put("hello", 123L);
        demo.put("world", 456L);

        String[] params = new String[]{"java.util.Map#java.lang.String#java.lang.Long#" + JSON.toJSONString(demo)};
        Object[] result = ArgumentParser.parse(params);
        System.out.println(JSON.toJSONString(result));
    }

}

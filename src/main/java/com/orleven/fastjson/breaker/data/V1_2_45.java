package com.orleven.fastjson.breaker.data;

import com.orleven.fastjson.breaker.BlackInfo;

import java.util.LinkedList;

public class V1_2_45 {
    private static BlackInfo data;

    public static BlackInfo getData() {
        if (data == null) {
            data = new BlackInfo();
            data.version = 1245;
            data.known = new LinkedList<BlackInfo.BlockItem>() {{
            }};
            data.unknown = new LinkedList<BlackInfo.BlockItem>() {{
            }};
        }
        return data;
    }
}
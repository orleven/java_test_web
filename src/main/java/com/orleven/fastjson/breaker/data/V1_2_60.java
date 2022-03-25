package com.orleven.fastjson.breaker.data;

import com.orleven.fastjson.breaker.BlackInfo;

import java.util.LinkedList;

public class V1_2_60 {
    private static BlackInfo data;

    public static BlackInfo getData() {
        if (data == null) {
            data = new BlackInfo();
            data.version = 1260;
            data.known = new LinkedList<BlackInfo.BlockItem>() {{
                add(new BlackInfo.BlockItem(3688179072722109200L, "com.zaxxer.hikari.")); //0x332f0b5369a18310L
            }};
            data.unknown = new LinkedList<BlackInfo.BlockItem>() {{
            }};
        }
        return data;
    }
}
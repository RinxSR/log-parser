package com.aci.jd2015;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {

        MyLogParser myLogParser = new MyLogParser();

        File inputFile = new File("D:\\Work by Rinat\\Projects\\log-parser\\src\\test\\resources\\testExample2.log");
        File outputFile = new File("D:\\Work by Rinat\\Projects\\log-parser\\src\\main\\resources\\OutFile.log");

        FileInputStream fileInputStream = new FileInputStream(inputFile);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        myLogParser.process(fileInputStream, fileOutputStream);
    }
}

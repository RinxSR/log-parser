package com.aci.jd2015;

import com.aci.jd2015.logic.LogParser;
import com.aci.jd2015.logic.MyLogParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;

public class LogParserTest {
  public static void startProcessCheck(LogParser parser, String inFileName, String checkFileName)
      throws IOException {
    File tmpFile = File.createTempFile("jd2015", ".log");
    try {
      try (InputStream is = LogParserTest.class.getResourceAsStream(inFileName);
          OutputStream os = new FileOutputStream(tmpFile)) {
        parser.process(is, os);
      }
      try (
          BufferedReader readerCheck = new BufferedReader(
              new InputStreamReader(LogParserTest.class.getResourceAsStream(checkFileName), "UTF-8"));
          BufferedReader readerResult = new BufferedReader(
              new InputStreamReader(new FileInputStream(tmpFile.getPath()), "UTF-8"))) {
        String check;
        String result;

        int lineNo = 0;
        while (true) {
          check = readerCheck.readLine();
          result = readerResult.readLine();
          if (null == check && null == result) {
            break;
          }
          lineNo++;
          if (!StringUtils.equals(check, result)) {
            throw new RuntimeException("Error on the line #" + lineNo);
          }
        }
      }
    } finally {
      tmpFile.delete();
    }
  }

  @Test
  public void testExample1Process() throws Exception {
    startProcessCheck(new MyLogParser(), "/testExample1.log", "/testExample1_answer.log");
  }

  @Test
  public void testExample2Process() throws Exception {
    startProcessCheck(new MyLogParser(), "/testExample2.log", "/testExample2_answer.log");
  }
}

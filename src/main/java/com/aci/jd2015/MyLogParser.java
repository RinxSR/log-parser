package com.aci.jd2015;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Реализация обработки лог-файла.
 */
public class MyLogParser implements LogParser {

    private ArrayList<String> listOfTmeString;
    private ArrayList<String> listOfSimpleString;
    private ArrayList<String> listOfCkecksumString;

    /**
     * Инициализация MyLogParser.
     */
    public MyLogParser() {
        listOfTmeString = new ArrayList<>();
        listOfSimpleString = new ArrayList<>();
        listOfCkecksumString = new ArrayList<>();
    }

    @Override
    public void process(InputStream is, OutputStream os) throws IOException {


        String inputString;

        PrintWriter writer = new PrintWriter(os);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        while ((inputString = reader.readLine()) != null) {

            switch (stringChecker(inputString)) {
                case TIME_STRING:
                    listOfTmeString.add(inputString);
                    Collections.sort(listOfTmeString);
                    break;
                case SIMPLE_STRING:
                    listOfSimpleString.add(inputString);
                    break;
                case CHECKSUM_STRING:
                    listOfCkecksumString.add(inputString);


                    for (int i = 0; i < listOfCkecksumString.size(); i++) {
                        for (int j = 0; j < listOfTmeString.size(); j++) {

                            String checkSum = listOfCkecksumString.get(i).substring(4);
                            ArrayList<String> tempList = getListToCheck(listOfTmeString.get(j));


                            for (int k = (int) Math.pow(2, tempList.size()) - 1; k > 1; k--) {

                                String binaryForm = Integer.toBinaryString(k);


                                ArrayList<String> listForCheckMD5 = new ArrayList<>();

                                for (int l = 0; l < binaryForm.length(); l++) {
                                    if (binaryForm.charAt(l) == '1') {
                                        listForCheckMD5.add(tempList.get(l));
                                    }
                                }

                                if (checkSumMD5(listForCheckMD5).equals(checkSum)) {

                                    for (String s : listForCheckMD5) {
                                        writer.println(s);
                                        listOfSimpleString.remove(s);
                                    }

                                    writer.println(listOfCkecksumString.get(i));
                                    writer.flush();

                                    listOfTmeString.remove(j);
                                    listOfCkecksumString.remove(i);

                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Определение типа строки.
     *
     * @param s - проверяемая строка
     * @return тип строки (Enum StringType)
     */
    public StringType stringChecker(String s) {

        if (checkOnTimeString(s)) {
            return StringType.TIME_STRING;
        } else if (checkOnChecksumString(s)) {
            return StringType.CHECKSUM_STRING;
        } else {
            return StringType.SIMPLE_STRING;
        }
    }

    /**
     * Проверка строки на соответствие типу TIME_STRING.
     *
     * @param stringToCheck - проверяемая строка
     * @return true если регуярное выражение присутствует в строке
     */
    public boolean checkOnTimeString(String stringToCheck) {

        String regexForTimeString = "\\d\\d\u002E\\d\\d\u002E\\d\\d\\d\\d\u0020\\d\\d\u003A\\d\\d\u003A\\d\\d\u002E\\d\\d\\d";

        Matcher matcher = Pattern.compile(regexForTimeString).matcher(stringToCheck);
        return matcher.lookingAt();
    }

    /**
     * Проверка строки на соответствие типу CHECKSUM_STRING.
     *
     * @param stringToCheck - проверяемая строка
     * @return true если строка начинается с CRC_
     */
    public boolean checkOnChecksumString(String stringToCheck) {
        return stringToCheck.startsWith("CRC_");
    }

    /**
     * Вычисление контрольной суммы по алгоритму MD5 для строки.
     *
     * @param stringToCheckSum входная строка для вычисления контрольной суммы
     * @return возвращает строку с контрольной суммой
     */
    public String checkSumMD5(String stringToCheckSum) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(stringToCheckSum.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Вычисление контрольной суммы по алгоритму MD5 для списка.
     *
     * @param listToCheckSum входной список для вычисления контрольной суммы
     * @return возвращает строку с контрольной суммой
     */
    public String checkSumMD5(List<String> listToCheckSum) {

        StringBuilder tempString = new StringBuilder();

        for (String s : listToCheckSum) {
            tempString.append(s);
        }

        return checkSumMD5(tempString.toString());
    }


    /**
     * Создание коллекции из TimeString и набора свободных SimpleString.
     *
     * @param timeString - входящая строка с датой
     * @return возвращает созданную коллекцию
     */
    public ArrayList<String> getListToCheck(String timeString) {
        ArrayList<String> tempList = new ArrayList<>();

        tempList.add(timeString);

        for (String simpleString : listOfSimpleString) {
            tempList.add(simpleString);
        }
        return tempList;
    }
}

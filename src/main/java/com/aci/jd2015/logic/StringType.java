package com.aci.jd2015.logic;

/**
 * Перечисление возможного состояни строк сообщения.
 */
public enum StringType {

    /**
     * Строка, содержащая контрольную сумму.
     */
    CHECKSUM_STRING,

    /**
     * Обычная строка с текстом.
     */
    SIMPLE_STRING,

    /**
     * Строка, содержащая дату сообщения.
     */
    TIME_STRING
}
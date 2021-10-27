package com.testmanage.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomString {

    /**
     * count 创建一个随机字符串，其长度是指定的字符数,字符将从参数的字母数字字符集中选择，如参数所示。
     * letters true,生成的字符串可以包括字母字符
     * numbers true,生成的字符串可以包含数字字符
     */
    public static String random(int count, boolean letters, boolean numbers) {
        String random = RandomStringUtils.random(count, letters, numbers);
        return random;
    }

    /**
     * 创建一个随机字符串，其长度是指定的字符数。
     * 将从所有字符集中选择字符
     */
    public static String random(int count) {
        String random = RandomStringUtils.random(count);
        return random;
    }

    /**
     * 创建一个随机字符串，其长度是10。
     * 将从所有字符集中选择字符
     */
    public static String random() {
        String random = RandomStringUtils.random(10);
        return random;
    }

    /**
     * 创建一个随机字符串，其长度是指定的字符数。
     * 字符将从字符串指定的字符集中选择，不能为空。如果NULL，则使用所有字符集。
     */
    public static String random(int count, String chars) {
        String random = RandomStringUtils.random(count, chars);
        return random;
    }

    /**
     * 产生一个长度为指定的随机字符串的字符数，字符将从拉丁字母（a-z、A-Z的选择）。
     * count:创建随机字符串的长度
     */
    public static String randomAlphabetic(int count) {
        String random = RandomStringUtils.randomAlphabetic(count);
        return random;
    }


    /**
     * 创建一个随机字符串，其长度介于包含最小值和最大最大值之间,，字符将从拉丁字母（a-z、A-Z的选择）。
     * minLengthInclusive ：要生成的字符串的包含最小长度
     * maxLengthExclusive ：要生成的字符串的包含最大长度
     */
    public static String randomAlphabetic(int minLengthInclusive, int maxLengthExclusive) {
        String random = RandomStringUtils.randomAlphabetic(minLengthInclusive, maxLengthExclusive);
        return random;
    }


    /**
     * 创建一个随机字符串，其长度是指定的字符数，字符将从拉丁字母（a-z、A-Z）和数字0-9中选择。
     * count ：创建的随机数长度
     */
    public static String randomAlphanumeric(int count) {
        String random = RandomStringUtils.randomAlphanumeric(count);
        return random;
    }


    /**
     * 创建一个随机字符串，其长度介于包含最小值和最大最大值之间,字符将从拉丁字母（a-z、A-Z）和数字0-9中选择。
     * minLengthInclusive ：要生成的字符串的包含最小长度
     * maxLengthExclusive ：要生成的字符串的包含最大长度
     */
    public static String randomAlphanumeric(int minLengthInclusive, int maxLengthExclusive) {
        String random = RandomStringUtils.randomAlphanumeric(minLengthInclusive, maxLengthExclusive);
        return random;
    }


}

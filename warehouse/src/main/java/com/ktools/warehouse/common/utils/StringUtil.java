package com.ktools.warehouse.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年08月24日 17:36
 */
public class StringUtil {

    /**
     * 字符串驼峰转下划线
     *
     * @param str 驼峰字符串
     * @return {@link String} 下划线字符串
     * @author lsl
     * @date 2023/11/22 16:58
     */
    public static String toUnderlineCase(CharSequence str) {
        char symbol = '_';
        if (str == null) {
            return null;
        }

        final int length = str.length();
        final StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < length; i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                final Character preChar = (i > 0) ? str.charAt(i - 1) : null;
                final Character nextChar = (i < str.length() - 1) ? str.charAt(i + 1) : null;

                if (null != preChar) {
                    if (symbol == preChar) {
                        // 前一个为分隔符
                        if (null == nextChar || Character.isLowerCase(nextChar)) {
                            //普通首字母大写，如_Abb -> _abb
                            c = Character.toLowerCase(c);
                        }
                        //后一个为大写，按照专有名词对待，如_AB -> _AB
                    } else if (Character.isLowerCase(preChar)) {
                        // 前一个为小写
                        sb.append(symbol);
                        if (null == nextChar || Character.isLowerCase(nextChar) || isNumber(nextChar)) {
                            //普通首字母大写，如aBcc -> a_bcc
                            c = Character.toLowerCase(c);
                        }
                        // 后一个为大写，按照专有名词对待，如aBC -> a_BC
                    } else {
                        //前一个为大写
                        if (null != nextChar && Character.isLowerCase(nextChar)) {
                            // 普通首字母大写，如ABcc -> A_bcc
                            sb.append(symbol);
                            c = Character.toLowerCase(c);
                        }
                        // 后一个为大写，按照专有名词对待，如ABC -> ABC
                    }
                } else {
                    // 首字母，需要根据后一个判断是否转为小写
                    if (null == nextChar || Character.isLowerCase(nextChar)) {
                        // 普通首字母大写，如Abc -> abc
                        c = Character.toLowerCase(c);
                    }
                    // 后一个为大写，按照专有名词对待，如ABC -> ABC
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 自定义分割函数，返回全部
     *
     * @param str   待分割的字符串
     * @param delim 分隔符
     * @return 分割后的返回结果
     */
    public static List<String> split(String str, final String delim) {
        if (null == str) {
//            return new ArrayList<>(0);
            return Collections.emptyList();
        }

        if (isEmpty(delim)) {
            List<String> result = new ArrayList<>(1);
            result.add(str);

            return result;
        }

        final List<String> stringList = new ArrayList<>();
        while (true) {
            int index = str.indexOf(delim);
            if (index < 0) {
                stringList.add(str);
                break;
            }
            stringList.add(str.substring(0, index));
            str = str.substring(index + delim.length());
        }
        return stringList;
    }

    public static void main(String[] args) {
        String str = "aa";
        List<String> split = split(str, ",");
        System.out.println(split);
    }
}

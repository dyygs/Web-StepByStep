package utils;

/**
 * Created by dy on 2017/7/6.
 */
public class StringUtil {

    /**
     * 字符串拼接
     * @param subStrings 可变字符串数组
     * @return 拼接后的字符串
     */
    public static String connectStrings(String... subStrings) {
        StringBuilder sb = new StringBuilder();
        for (String str : subStrings) {
            sb.append(str);
        }
        return sb.toString();
    }
}

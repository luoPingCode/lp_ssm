package com.lp.util;

import java.util.regex.Pattern;

/**
 * @author LuoPing
 * @date 2022/10/16 16:53
 */
public class XssUtil {
    public static Pattern scriptPattern1 = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    public static Pattern scriptPattern2 = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static Pattern scriptPattern3 = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static Pattern scriptPattern4 = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    public static Pattern scriptPattern5 = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static Pattern scriptPattern6 = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static Pattern scriptPattern7 = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static Pattern scriptPattern8 = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    public static Pattern scriptPattern9 = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    public static Pattern scriptPattern10 = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static Pattern scriptPattern11 = Pattern.compile(".*<.*", Pattern.CASE_INSENSITIVE );

    public static String striptXSS(String value) {
        if (value != null) {

            value = value.replaceAll("", "");
            value = scriptPattern1.matcher(value).replaceAll("");
            value = scriptPattern2.matcher(value).replaceAll("");
            value = scriptPattern3.matcher(value).replaceAll("");
            value = scriptPattern4.matcher(value).replaceAll("");
            value = scriptPattern5.matcher(value).replaceAll("");
            value = scriptPattern6.matcher(value).replaceAll("");
            value = scriptPattern7.matcher(value).replaceAll("");
            value = scriptPattern8.matcher(value).replaceAll("");
            value = scriptPattern9.matcher(value).replaceAll("");
            value = scriptPattern10.matcher(value).replaceAll("");
            value = scriptPattern11.matcher(value).replaceAll("");
        }
        return value;
    }
    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     * @param s
     * @return
     */
    public static String xssEncode(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append('＞');//全角大于号
                    break;
                case '<':
                    sb.append('＜');//全角小于号
                    break;
                case '\'':
                    sb.append('‘');//全角单引号
                    break;
                case '\"':
                    sb.append('“');//全角双引号
                    break;
                case '&':
                    sb.append('＆');//全角
                    break;
                case '\\':
                    sb.append('＼');//全角斜线
                    break;
                case '#':
                    sb.append('＃');//全角井号
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = xssEncode("<img class=\"logo\" src=\"/doc21xnv/image/hero.png\" alt=\"文档合集\">\nhello");
        System.out.println(s);
        String s1 = striptXSS("<img class=\"logo\" src=\"/doc21xnv/image/hero.png\" alt=\"文档合集\">\nhello");
        System.out.println(s1);
    }
}

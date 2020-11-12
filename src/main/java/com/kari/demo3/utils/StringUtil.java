package com.kari.demo3.utils;

import org.apache.logging.log4j.util.Strings;

public class StringUtil {

    public static void main(String[] args) {
        System.out.println(deleteBar("Shopping-cartController"));
        System.out.println(escapeCharacter("{\"cha\":12}"));
    }

    /**
     * pure method
     * @param component
     * @return
     */
    public static String upperHeader(String component) {
        if(Strings.isEmpty(component)){
            return component;
        }

        char header = component.charAt(0);
        char[] h = new char[1];
        h[0] = (char)(header - 32);
        String suffix = component.substring(1);
        return new StringBuilder(new String(h)).append(suffix).toString();
    }

    /**
     * pure method
     * @param s
     * @return
     */
    public static String deleteBar(String s){
        if (null == s || !s.contains("-")){
            return s;
        }

        char[] src = s.toCharArray();
        int count = 0;
        for(char c : src){
            if (c == '-'){
                continue;
            }
            count ++;
        }
        char[] dest = new char[count];
        boolean upperNextChar = false;
        int i = 0;
        for(char c : src){
            if (c == '-'){
                upperNextChar = true;
                continue;
            }
            if(c<97 || c>122){
                dest[i++] = c;
                continue;
            }
            if (upperNextChar){
                c -= 32;
                upperNextChar = false;
            }
            dest[i++] = c;
        }

        return new String(dest);
    }

    public static String escapeCharacter(String s){
        if (Strings.isEmpty(s)){
            return s;
        }
        if(s.contains("\\")){
            s = s.replace("\\", "\\\\");
        }
        if(s.contains("\"")){
            s = s.replace("\"", "\\\"");
        }
        return s;
    }
}

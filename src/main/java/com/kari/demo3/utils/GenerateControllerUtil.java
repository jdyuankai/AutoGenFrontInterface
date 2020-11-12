package com.kari.demo3.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.*;

public class GenerateControllerUtil {

    public static void main(String[] args) {
        File f = new File("");
        System.out.println(f.getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));
        createClassFile("HelloController:/hello", "add:/add", new ArrayList<String>(){{add("String:key");add("String:token");}}, "{\"\"code\"\":0, \"\"msg\"\":\"\"success\"\"}");
    }


    public static void createClassFile(String className, String methodName, List<String> params, String resultData){
        String path = System.getProperty("user.dir") + "/src/main/java/com/kari/demo3/controller/" + className.split(":")[0] + ".java";
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                String[] sp = className.split(":");
                fillWithClassInfo(file, sp[0],sp[1]);
            }
            appendMethod(file, methodName, params, resultData);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(path);
        }

    }

    private static void appendMethod(File file, String methodName, List<String> params, String resultData) {
        //
        Configuration cfg = new Configuration();
        try {
            deleteLastLine(file);
            cfg.setClassForTemplateLoading(GenerateControllerUtil.class, "/templates/");
            Template template = cfg.getTemplate("MethodTemplate.ftl");
            Map<String, String> data = new HashMap<>();
            data.put("methodName", methodName.split(":")[0]);
            data.put("methodMapping", methodName.split(":")[1]);
            StringBuilder builder = new StringBuilder();
            Iterator<String> it = params.iterator();
            if(it.hasNext()) {
                String p = it.next();
                for (; ; p = it.next()) {
                    builder.append(p.split(":")[0]).append(" ").append(p.split(":")[1]);
                    if (it.hasNext()) {
                        builder.append(", ");
                    } else {
                        break;
                    }
                }
            }
            data.put("params", builder.toString());
            data.put("result", resultData);
            Writer writer = new FileWriter(file, true);
            template.process(data, writer);
            writer.flush();
            writer.close();
            addLastLine(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void addLastLine(File file) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write("\n}");
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void deleteLastLine(File file) {
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Iterator<String> it = br.lines().iterator();
            if(it.hasNext()) {
                String line = it.next();
                for (; it.hasNext(); line = it.next()) {
                    if (!it.hasNext()) {
                        continue;
                    }
                    buffer.append(line).append("\n");
                }
            }
            br.close();
            FileWriter writer = new FileWriter(file);
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void fillWithClassInfo(File file, String s, String s1) {
        Configuration cfg = new Configuration();
        try {
            cfg.setClassForTemplateLoading(GenerateControllerUtil.class, "/templates/");
            Template template = cfg.getTemplate("ControllerTemplate.ftl");
            Map<String, String> data = new HashMap<>();
            data.put("className", s);
            data.put("classMapping", s1);
            Writer writer = new FileWriter(file);
            template.process(data, writer);
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

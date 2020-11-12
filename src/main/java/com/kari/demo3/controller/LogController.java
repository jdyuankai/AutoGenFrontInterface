package com.kari.demo3.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.kari.demo3.utils.GenerateControllerUtil;
import com.kari.demo3.utils.StringUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LogController {

    private static Map<String, Boolean> exists = new HashMap<String, Boolean>();
    private Map<String, String> classNameMapping = new HashMap<String, String>(){{
        put("","");
        put("dd","ddd");
        put("dd","ddd");
    }};


    @RequestMapping("/log/addLog")
    public void log(@RequestBody Data data) {
        FileWriter writer = null;
        try {
            if (exists.keySet().contains(data.path)){
                return;
            }
            exists.put(data.path, true);

            String url = removePrefix(data.path);

            String[] components = url.split("/");
            // 如果是两个就一个类名一个方法名，如果大于俩就前两个类名后面方法名
            String className = "";
            String classMapping = "";
            String methodName = "";
            String methodMapping = "";
            if (components.length == 2) {
                className = toCotrollerName(StringUtil.upperHeader(components[0]));
                classMapping = new StringBuilder("/").append(components[0]).toString();
                methodName = components[1];
                methodMapping = new StringBuilder("/").append(components[1]).toString();
            }else {
                className = toCotrollerName(new StringBuilder(StringUtil.upperHeader(components[0])).append(StringUtil.upperHeader(components[1])).toString());
                classMapping = new StringBuilder("/").append(components[0]).append("/").append(components[1]).toString();
                StringBuilder tmpMethodName = new StringBuilder();
                StringBuilder tmpMethodMapping = new StringBuilder();
                for(int i=2; i<components.length; i++){
                    if (i > 2){
                        tmpMethodName.append(StringUtil.upperHeader(components[i]));
                    }else{
                        tmpMethodName.append(components[i]);
                    }
                    tmpMethodMapping.append("/").append(components[i]);
                }
                methodName = tmpMethodName.toString();
                methodMapping = tmpMethodMapping.toString();
            }
            className = StringUtil.deleteBar(className);
            methodName = StringUtil.deleteBar(methodName);

            // 处理参数
            List<String> params = new ArrayList<>();
            if(data.arg != null) {
                JSONObject obj = JSONObject.parseObject(data.arg);
                for (Map.Entry entry: obj.entrySet()){
                    StringBuilder builder = new StringBuilder();
                    if (entry.getValue() instanceof String){
                        builder.append("String").append(":").append(entry.getKey());
                    }
                    else if (entry.getValue() instanceof Integer){
                        builder.append("Integer").append(":").append(entry.getKey());
                    }
                    else if (entry.getValue() instanceof Boolean) {
                        builder.append("Boolean").append(":").append(entry.getKey());
                    }
                    else {
                        builder.append("String").append(":").append(entry.getKey());
                    }
                    params.add(builder.toString());
                }
            }

            GenerateControllerUtil.createClassFile(className+":"+classMapping, methodName+":"+methodMapping, params, StringUtil.escapeCharacter(data.result));

            writer = new FileWriter("./interface.json", true);
            writer.write(data.path + "\n");
            writer.write( data.arg + "\n");
            writer.write( data.result + "\n");
            writer.write("\n");
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String toCotrollerName(String str) {
        return new StringBuilder(str).append("Controller").toString();
    }

    // 移除 https://api.it120.cc/wxapi
    private String removePrefix(String path) {
        return path.replace("https://api.it120.cc/wxapi/", "");
    }
}

class Data{
    String path; String arg; String result;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
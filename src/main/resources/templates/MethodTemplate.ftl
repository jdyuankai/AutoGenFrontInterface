
    @RequestMapping("${methodMapping}")
    @ResponseBody
    public Response ${methodName}(${params}) {
        String res = "${result}";
        //String res = "";
        return JSONObject.parseObject(res, Response.class);
    }
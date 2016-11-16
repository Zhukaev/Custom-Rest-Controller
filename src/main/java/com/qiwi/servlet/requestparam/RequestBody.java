package com.qiwi.servlet.requestparam;

public class RequestBody implements RequestParam {
    private Class clazz;

    public RequestBody(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }
}

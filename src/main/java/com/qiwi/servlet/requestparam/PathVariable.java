package com.qiwi.servlet.requestparam;

public class PathVariable implements RequestParam {
    private Class clazz;

    public PathVariable(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}

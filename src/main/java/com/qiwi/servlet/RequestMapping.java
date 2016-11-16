package com.qiwi.servlet;

import java.util.Map;
import java.util.regex.Pattern;

public interface RequestMapping {
    Pattern getPath();

    Map<String, MethodDefinition> getMethodMap();
}

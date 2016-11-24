package com.qiwi.servlet.requestmapping;

import java.util.Map;
import java.util.regex.Pattern;

public interface RequestMapping {
    Pattern getPath();

    Map<String, MethodDefinition> getMethodMap();
}

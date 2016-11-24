package com.qiwi.servlet.requestmapping;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@Service
public class RequestMappingService {

    private List<RequestMapping> methods;

    public RequestMappingService() {
        this.methods = new ArrayList<RequestMapping>();
    }

    public RequestMapping getMethodDefine(String path) {
        for (RequestMapping methodDefine : methods) {
            Matcher matcher = methodDefine.getPath().matcher(path);
            if (matcher.matches()) {
                return methodDefine;
            }
        }
        return null;
    }

    public void addMethodDefine(MethodDefinition newMethodDefine, String path, String httpMethod) {
        if (methods.size() == 0) {
            RequestMappingImpl newRequestMapping = new RequestMappingImpl(path);
            newRequestMapping.getMethodMap().put(httpMethod, newMethodDefine);
            this.methods.add(newRequestMapping);
        } else {
            for (RequestMapping requestMapping : methods) {
                //Matcher matcher = requestMapping.getPath().matcher(path);
                //System.out.println(requestMapping.getPath() + " " + path);
                if (path.equals(requestMapping.getPath().toString())) {
                    requestMapping.getMethodMap().put(httpMethod, newMethodDefine);
                    return;
                }
            }
            RequestMappingImpl newRequestMapping = new RequestMappingImpl(path);
            newRequestMapping.getMethodMap().put(httpMethod, newMethodDefine);
            this.methods.add(newRequestMapping);
        }
    }


    public RequestMapping getRequestMapping(String path) {
        for (RequestMapping methodDefine : methods) {
            Matcher matcher = methodDefine.getPath().matcher(path);
            if (matcher.matches()) {
                //System.out.println(methodDefine.getPath() + " " + path);
                return methodDefine;
            }
        }
        return null;
    }
}

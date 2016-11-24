package com.qiwi.servlet;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private List<String> audit;

    public AuditServiceImpl() {
        this.audit = new ArrayList<String>();
    }

    public List<String> getAudit() {
        return audit;
    }

    @Override
    public void addAudit(String auditMsg) {
        System.out.println(auditMsg);
        this.audit.add(auditMsg);
    }

}

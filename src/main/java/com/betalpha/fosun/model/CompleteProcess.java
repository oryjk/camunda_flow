package com.betalpha.fosun.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CompleteProcess {
    @Id
    private String processInstanceId;
    private String createTime;
    private String bondCode;
    private String grade;
    private String submissionDepartment;
    private String submitter;
    private String name;
    private String issuer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubmissionDepartment() {
        return submissionDepartment;
    }

    public void setSubmissionDepartment(String submissionDepartment) {
        this.submissionDepartment = submissionDepartment;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public CompleteProcess(String processInstanceId, String createTime, String bondCode, String grade, String submissionDepartment, String submitter, String name, String issuer) {
        this.processInstanceId = processInstanceId;
        this.createTime = createTime;
        this.bondCode = bondCode;
        this.grade = grade;
        this.submissionDepartment = submissionDepartment;
        this.submitter = submitter;
        this.name = name;
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public CompleteProcess() {
    }
}


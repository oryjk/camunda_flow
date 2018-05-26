package com.betalpha.fosun.api.process;

import lombok.Data;

import java.io.Serializable;

/**
 * created on 2018/5/16
 *
 * @author huzongpeng
 */
@Data
public class Task implements Serializable {

    private static final long serialVersionUID = 5164783702332382580L;

    public Task(String processInstanceId, String status, String createTime, String bondCode, String grade, String submissionDepartment, String submitter, String issuer) {
        this.processInstanceId = processInstanceId;
        this.status = status;
        this.createTime = createTime;
        this.bondCode = bondCode;
        this.grade = grade;
        this.submissionDepartment = submissionDepartment;
        this.submitter = submitter;
        this.issuer = issuer;
    }

    private String processInstanceId;
    private String status;
    private String createTime;
    private String bondCode;
    private String grade;
    private String submissionDepartment;
    private String submitter;
    private String issuer;
}

package com.betalpha.fosun.listener;


import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.Set;


/**
 * Created by WangRui on 13/05/2018.
 */
@Slf4j
public class InvestListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        Integer loopCounter = (Integer) delegateTask.getVariable("loopCounter");
        if (loopCounter+1 == candidates.size()) {
            log.info("Complete vote");
            Integer agree = (Integer) delegateTask.getVariable("agree");
            Integer disagree = (Integer) delegateTask.getVariable("disagree");
            if (agree > disagree) {
                delegateTask.setVariable("x", 1);
            } else {
                delegateTask.setVariable("x", 2);
            }
            return;
        }
        log.info("Not complete");
    }
}

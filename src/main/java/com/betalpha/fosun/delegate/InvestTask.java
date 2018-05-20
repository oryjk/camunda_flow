package com.betalpha.fosun.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

/**
 * Created by WangRui on 12/05/2018.
 */
public class InvestTask implements JavaDelegate {

    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("InvestTask execute");
    }
}

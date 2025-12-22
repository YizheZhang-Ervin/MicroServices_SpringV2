package listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution){
        System.out.println(jobExecution.getJobInstance().getJobName()+"BEFORE");
    }
    @Override
    public void afterJob(JobExecution jobExecution){
        System.out.println(jobExecution.getJobInstance().getJobName()+"AFTER");
    }
}

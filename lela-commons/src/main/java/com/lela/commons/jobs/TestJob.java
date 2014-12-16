package com.lela.commons.jobs;

import com.lela.commons.jobs.java.JavaExecutionContext;
import com.lela.commons.jobs.java.JavaJob;
import com.lela.commons.jobs.java.JavaJobDetail;

public class TestJob extends JavaJobDetail implements JavaJob{
	private static final long serialVersionUID = 1649021447829172570L;

	@SuppressWarnings("static-access")
	@Override
	public void execute(JavaExecutionContext context){
		context.message("Starting TestJob and pausing for 15 seconds...");
		try {
			Thread.currentThread().sleep(15000);
		} catch (Exception e){
			e.printStackTrace();
		}
		context.message("Stopping TestJob...");
		
	}

    @Override
    public JavaJob getJob() {
        return this;
    }
}

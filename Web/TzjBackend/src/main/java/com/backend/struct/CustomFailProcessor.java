package com.backend.struct;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.FailProcessor;

public class CustomFailProcessor extends FailProcessor{
	
	private static final Log log = Logs.get();

	@Override
	public void init(NutConfig config, ActionInfo ai) {
	    view = evalView(config, ai, ai.getFailView());
	}

	@Override
	public void process(ActionContext ac) throws Throwable {
	    Object err = ac.getError();
		if(err!=null){
			log.error(ac.getError().getMessage(), ac.getError());
		}
		super.process(ac);
	}
}

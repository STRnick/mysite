package com.douzone.mysite.web.mvc.board;

import com.douzone.web.mvc.Action;
import com.douzone.web.mvc.ActionFactory;

public class BoardActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		Action action = null;

		if ("writeform".equals(actionName)) {
			action = new writeFormAction();
		} else if ("write".equals(actionName)) {
			action = new writeAction();
		} else if ("view".equals(actionName)) {
			action = new viewformAction();
		} else if ("remove".equals(actionName)) {
			action = new removeAction();
		} else if ("modifyform".equals(actionName)) {
			action = new modifyFormAction();
		} else if ("modify".equals(actionName)) {
			action = new modifyAction();
		} else {
			action = new IndexAction();
		}

		return action;
	}
}

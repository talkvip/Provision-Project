package com.cpn.vsp.provision.logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoggingMessage implements Serializable {

	private static final long serialVersionUID = -6044166545588941638L;

	public static enum State {
		ENTER {
			@Override
			public String toString() {
				return "Enter";
			}
		},
		EXIT {
			@Override
			public String toString() {
				return "Exit";
			}
		},
		THROWN {
			@Override
			public String toString() {
				return "Thrown";
			}
		}

	}

	Date createdOn = new Date();
	State state;
	String threadId;
	String className;
	String methodName;
	List<Object> arguments = new ArrayList<>();
	Object result;

	@Override
	public String toString() {
		String out = createdOn.toString() + " " + threadId + " " + state.toString() + " " + className + "." + methodName + "(";
		for (Object o : arguments) {
			out += o.toString() + ",";
		}
		if (arguments.size() > 0) {
			out = out.substring(0, out.length() - 1);
		}
		out += ")";
		if (state.equals(State.EXIT) || state.equals(State.THROWN)) {
			out += " Output: " + result;
		}
		return out;
	}
}

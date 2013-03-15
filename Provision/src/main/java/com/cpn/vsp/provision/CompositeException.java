package com.cpn.vsp.provision;

import java.util.ArrayList;
import java.util.List;

public class CompositeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 944685431442287324L;
	List<Exception> list = new ArrayList<>();

	public CompositeException() {
	}

	public CompositeException(List<Exception> aList) {
		list = aList;
	}

	public CompositeException add(Exception e) {
		list.add(e);
		return this;
	}

	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		for (Exception e : list) {
			sb.append(e.getMessage());
		}
		return sb.toString();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		List<StackTraceElement> elements = new ArrayList<>();
		for (Exception e : list) {
			for (StackTraceElement el : e.getStackTrace()) {
				elements.add(el);
			}
		}
		return (StackTraceElement[]) elements.toArray();
	}

}

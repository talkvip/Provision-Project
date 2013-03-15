package com.cpn.os4j.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AbstractOpenStackModel implements Serializable{

	private static final long serialVersionUID = 9183090528014815593L;
	List<Link> links;

	@JsonIgnore
	public String getSelfRef(){
		for(Link l : links){
			if("self".equals(l.getRel())){
				return l.getHref();
			}
		}
		return null;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
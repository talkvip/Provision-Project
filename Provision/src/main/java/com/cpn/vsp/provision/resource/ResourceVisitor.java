package com.cpn.vsp.provision.resource;

public interface ResourceVisitor {

	void visit(VirtualResource aResource);
	void visit(PhysicalResource physicalResource);
	
}

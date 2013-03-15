package com.cpn.vsp.provision.mobile;

import java.util.UUID;

import com.cpn.xml.XMLNode;

public class RemovalPasswordPayload extends XMLNode{

	public RemovalPasswordPayload(XMLNode.XMLNodeFactory xm, String aPayloadOrganization, String aPayloadIdentifier, String aRemovalPassword) {
		super();
		node = xm.l("dict",
				xm.l("key", "PayloadDescription"),
				xm.l("string", ""),
				xm.l("key", "PayloadDisplayName"),
				xm.l("string", ""),
				xm.l("key", "PayloadIdentifier"),
				xm.l("string", aPayloadIdentifier + ".passcodepolicy8"),
				xm.l("key", "PayloadOrganization"),
				xm.l("string", aPayloadOrganization),
				xm.l("key", "PayloadType"),
				xm.l("string", "com.apple.profileRemovalPassword"),
				xm.l("key", "PayloadUUID"),
				xm.l("string", UUID.randomUUID().toString().toUpperCase()),
				xm.l("key", "PayloadVersion"),
				xm.l("integer", "1"),
				xm.l("key", "RemovalPassword"),
				xm.l("string", aRemovalPassword)
		).node;
	}
}

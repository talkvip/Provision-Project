package com.cpn.vsp.provision.mobile;

import java.util.UUID;

import com.cpn.xml.XMLNode;

public class NetworkingPayload extends XMLNode{

	public NetworkingPayload(XMLNode.XMLNodeFactory xm, IPSecPayload anIpSecPayload, String anOrganization, String aVPNPayloadDisplayName, String aPayloadIdentifier) {
		super();
		node = xm.l("dict", 
				xm.l("key", "IPSec"),
				anIpSecPayload,
				xm.l("key", "IPv4"),
				xm.l("dict", 
						xm.l("key", "OverridePrimary"),
						xm.l("integer", "0")
				),
				xm.l("key", "PayloadDescription"),
				xm.l("string", "Configures VPN settings, including authentication."),
				xm.l("key", "PayloadDisplayName"),
				xm.l("string", "VPN (" + aVPNPayloadDisplayName + ")"),
				xm.l("key", "PayloadIdentifier"),
				xm.l("string", aPayloadIdentifier + ".vpn1"),
				xm.l("key", "PayloadOrganization"),
				xm.l("string", anOrganization),
				xm.l("key", "PayloadType"),
				xm.l("string", "com.apple.vpn.managed"),
				xm.l("key", "PayloadUUID"),
				xm.l("string", UUID.randomUUID().toString().toUpperCase()),
				xm.l("key", "PayloadVersion"),
				xm.l("integer", "1"),
				xm.l("key", "Proxies"),
				xm.l("dict"),
				xm.l("key", "UserDefinedName"),
				xm.l("string", aVPNPayloadDisplayName),
				xm.l("key", "VPNType"),
				xm.l("string", "IPSec")
		).node;
		
	}
}

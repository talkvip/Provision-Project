package com.cpn.vsp.provision.mobile;

import com.cpn.xml.XMLNode;

public class IPSecPayload extends XMLNode {

	public IPSecPayload(XMLNode.XMLNodeFactory xm, CertificatePayload authCert, String aRemoteAddress, String xauthName, String xauthPassword) {
			super();
			node = xm.l("dict", 
					xm.l("key", "AuthenticationMethod"),
					xm.l("string", "Certificate"),
					xm.l("key", "OnDemandEnabled"),
					xm.l("integer", "1"),
					xm.l("key", "OnDemandMatchDomainsAlways"),
					xm.l("array", 
							xm.l("string", "a"),
							xm.l("string", "b"),
							xm.l("string", "c"),
							xm.l("string", "d"),
							xm.l("string", "e"),
							xm.l("string", "f"),
							xm.l("string", "g"),
							xm.l("string", "h"),
							xm.l("string", "i"),
							xm.l("string", "j"),
							xm.l("string", "k"),
							xm.l("string", "l"),
							xm.l("string", "m"),
							xm.l("string", "n"),
							xm.l("string", "o"),
							xm.l("string", "p"),
							xm.l("string", "q"),
							xm.l("string", "r"),
							xm.l("string", "s"),
							xm.l("string", "t"),
							xm.l("string", "u"),
							xm.l("string", "v"),
							xm.l("string", "w"),
							xm.l("string", "x"),
							xm.l("string", "y"),
							xm.l("string", "z"),
							xm.l("string", "1"),
							xm.l("string", "2"),
							xm.l("string", "3"),
							xm.l("string", "4"),
							xm.l("string", "5"),
							xm.l("string", "6"),
							xm.l("string", "7"),
							xm.l("string", "8"),
							xm.l("string", "9"),
							xm.l("string", "0")
					),
					xm.l("key", "PayloadCertificateUUID"),
					xm.l("string", authCert.uuid),
					xm.l("key", "PromptForVPNPIN"),
					xm.l("false"),
					xm.l("key", "RemoteAddress"),
					xm.l("string", aRemoteAddress),
					xm.l("key", "XAuthEnabled"),
					xm.l("integer", "1"),
					xm.l("key", "XAuthName"),
					xm.l("string", xauthName),
					xm.l("key", "XAuthPassword"),
					xm.l("string", xauthPassword)
			).node;
	}
	
}

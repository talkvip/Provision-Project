package com.cpn.vsp.provision.mobile;

import java.util.UUID;

import com.cpn.xml.XMLNode;

public class CertificatePayload extends XMLNode {

	public final String uuid;
	public final String displayName;
	public final String organization;
	public final String identifier;

	public enum Type {
		IDENTITY {
			@Override
			public String toString() {
				return "com.apple.security.pkcs12";
			}
		},
		INTERMEDIATE_CA {
			public String toString() {
				return "com.apple.security.pkcs1";
			}
		},
		CA {
			public String toString() {
				return "com.apple.security.root";
			}
		}
	}

	public CertificatePayload(XMLNode.XMLNodeFactory xm, String aPayloadDisplayName, String aCertificateBody, String aPayloadOrganization, String payloadIdentifier, int aCounter, Type aCertificateType) {
		super();
		uuid = UUID.randomUUID().toString().toUpperCase();
		displayName = aPayloadDisplayName;
		organization = aPayloadOrganization;
		identifier = payloadIdentifier;
		node = xm.l("dict",
				xm.l("key", "Password"),
				xm.l("string", "password"),
				xm.l("key", "PayloadCertificateFileName"),
				xm.l("string", aPayloadDisplayName),
				xm.l("key", "PayloadContent"),
				xm.l("data", "\n" + aCertificateBody + "\n"), 
//				"MIIFXjCCBEagAwIBAgIJAK2UgT+DLLeJMA0GCSqGSIb3DQEBBQUA"+
//				"MIHPMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTET"+
//				"MBEGA1UEBxMKRWwgU2VndW5kbzEbMBkGA1UEChMSQ2xlYXJQYXRo"+
//				"IE5ldHdvcmtzMR8wHQYDVQQLExZFbmdpbmVlcmluZyBEZXBhcnRt"+
//				"ZW50MTEwLwYDVQQDEyhDbGVhclBhdGggTmV0d29ya3MgQ2VydGlm"+
//				"aWNhdGUgQXV0aG9yaXR5MSUwIwYJKoZIhvcNAQkBFhZjZXJ0c0Bj"+
//				"bGVhcnBhdGhuZXQuY29tMB4XDTExMTIwMjE5MDcyMloXDTE1MTIw"+
//				"MTE5MDcyMlowgc8xCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxp"+
//				"Zm9ybmlhMRMwEQYDVQQHEwpFbCBTZWd1bmRvMRswGQYDVQQKExJD"+
//				"bGVhclBhdGggTmV0d29ya3MxHzAdBgNVBAsTFkVuZ2luZWVyaW5n"+
//				"IERlcGFydG1lbnQxMTAvBgNVBAMTKENsZWFyUGF0aCBOZXR3b3Jr"+
//				"cyBDZXJ0aWZpY2F0ZSBBdXRob3JpdHkxJTAjBgkqhkiG9w0BCQEW"+
//				"FmNlcnRzQGNsZWFycGF0aG5ldC5jb20wggEiMA0GCSqGSIb3DQEB"+
//				"AQUAA4IBDwAwggEKAoIBAQDMKtzIG2TLLlgVGp+xMrKadjfyKAv5"+
//				"oAEiQy1mklEX7kAD46nvt6xugURfk2VPyILHC7NbqcTMjr3giQ5y"+
//				"ZtzcwjcTM29D8QZ9KxdCzcXpwona68GO9DSui1y/qufPSLQSErxk"+
//				"HfSCcOHSBhNsZgcs8mif+TbroTFPLGZcZVLKsBL2qrw0GbP1P3WG"+
//				"0c53TBRG3shsM3L5HuZiec/sdjMKZ9RON1I5BPPiUOfzewIzijJV"+
//				"nAgPS5jVgpjRQ8UXHdB3zIrFPYZJz9mM3Fxmf7T4S6WDXPoLsY2u"+
//				"eq4ZowaUVdi/9THpGeYIjP3RSnVaH++ERixXfmvTuCHAIo2mMIqf"+
//				"AgMBAAGjggE5MIIBNTAdBgNVHQ4EFgQUlCkuXbHJr5z2eNohw9tN"+
//				"YvpfF/AwggEEBgNVHSMEgfwwgfmAFJQpLl2xya+c9njaIcPbTWL6"+
//				"XxfwoYHVpIHSMIHPMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2Fs"+
//				"aWZvcm5pYTETMBEGA1UEBxMKRWwgU2VndW5kbzEbMBkGA1UEChMS"+
//				"Q2xlYXJQYXRoIE5ldHdvcmtzMR8wHQYDVQQLExZFbmdpbmVlcmlu"+
//				"ZyBEZXBhcnRtZW50MTEwLwYDVQQDEyhDbGVhclBhdGggTmV0d29y"+
//				"a3MgQ2VydGlmaWNhdGUgQXV0aG9yaXR5MSUwIwYJKoZIhvcNAQkB"+
//				"FhZjZXJ0c0BjbGVhcnBhdGhuZXQuY29tggkArZSBP4Mst4kwDAYD"+
//				"VR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAlgJrrHqg7M8N"+
//				"Hb8ndHzOy6eYD8RvJx3STgGqLbd0TkBNh9zs8jWhUKlyek6jjgur"+
//				"kFi8XDvBNButu0FRr/6oh1hsE4vbY8OfUqf5zBvb3yGJom5SJauB"+
//				"zoYUMUpAgk6zKSneSs+K0Hb+Th3GSByUHOA5opHuiHRiOQwhjyvt"+
//				"+NjRKhkEPbfhFT71gzV2KSjgGysv8xsdLlxNSUx9J0nvM2AkctPq"+
//				"jH7iPVhMJm9+7u2EnqKhnqGu2mLqydAMOBv3HW867n/LBTlt2ad/"+
//				"UD2UhZNlY+oHcySpKhm4pNZ9L6nKDueh0X9dRCmYdupzt/w1cnEx"+
//				"pVcXDsWJVh8rOPXkoQ=="

				xm.l("key", "PayloadDescription"),
				xm.l("string", "Provides device authentication (certificate or identity)."),
				xm.l("key", "PayloadDisplayName"),
				xm.l("string", aPayloadDisplayName),
				xm.l("key", "PayloadIdentifier"),
				xm.l("string", payloadIdentifier + ".credential" + aCounter),
				xm.l("key", "PayloadOrganization"),
				xm.l("string", aPayloadOrganization),
				xm.l("key", "PayloadType"),
				xm.l("string", aCertificateType.toString()),
				xm.l("key", "PayloadUUID"),
				xm.l("string", uuid),
				xm.l("key", "PayloadVersion"),
				xm.l("integer", "1")
		).node;
	}
}

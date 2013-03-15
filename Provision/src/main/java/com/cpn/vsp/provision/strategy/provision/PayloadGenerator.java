package com.cpn.vsp.provision.strategy.provision;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.execute.SystemExecutor;
import com.cpn.execute.SystemExecutorException;
import com.cpn.vsp.provision.certificate.Certificate;

@Service
public class PayloadGenerator {
	private static final String autorunPayload = "#!/bin/sh\ncp /tmp/payload/management.conf /etc/network/\ncp -rf /tmp/payload/identity /etc/\nmv /tmp/payload/ca-bundle.pem /etc/\ncp /tmp/payload/deviceInfo /etc/device.info\n";

	private static final String managementConfPayload = "# firmware payload delivered management.conf file\ntls-client\npull\n\ndev tun\nnobind\n\nremote %%MANAGER_HOSTNAME%% 7000\n\nremote-random\nresolv-retry infinite\n\ntls-cipher AES256-SHA\nca   /etc/ca-bundle.pem\ncert /etc/identity/snap.cert\nkey  /etc/identity/snap.key\n\npersist-key\nmlock\n\ncomp-lzo no\n\ntls-timeout   10\nkeepalive 5 45\n\nverb 3\n";
	
	@Autowired
	String managementHostName;

	private static String randomFile() {
		return UUID.randomUUID().toString();
	}

	private static byte[] readFile(File aFile) {
		try (FileInputStream fstream = new FileInputStream(aFile); DataInputStream in = new DataInputStream(fstream)) {
			byte[] out = new byte[new Long(aFile.length()).intValue()];
			in.read(out);
			return out;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static void writeFile(File aFile, String aString) throws IOException {
		try (FileOutputStream outputFile = new FileOutputStream(aFile, true); FileChannel outChannel = outputFile.getChannel();) {
			ByteBuffer buf = ByteBuffer.allocate(aString.getBytes().length);
			buf.put(aString.getBytes());
			buf.flip();
			outChannel.write(buf);
		}
	}

	private static void delete(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			System.out.println("Why didn't I delete " + f + "?");
	}

	public byte[] buildPayload(String aHostName, String anIpAddress, String certificate, Set<Certificate> caErs, String caBundle) {
		String dir = randomFile();
		File tempDir = new File(dir);
		tempDir.mkdir();
		try {
			File payloadDir = new File(dir + "/payload");
			File identity = new File(dir + "/payload/identity");
			File caBundleDir = new File(dir + "/payload/caBundle");

			payloadDir.mkdir();
			identity.mkdir();
			caBundleDir.mkdir();
			File autorun = new File(dir + "/payload/autorun.sh");

			writeFile(autorun, autorunPayload);
			autorun.setExecutable(true);

			// File key = new File(dir + "/payload/identity/snap.key");
			File cert = new File(dir + "/payload/identity/snap.cert");
			File id = new File(dir + "/payload/identity/id");
			File caBundleFile = new File(dir + "/payload/ca-bundle.pem");
			File deviceInfo = new File(dir + "/payload/deviceInfo");
			File managementConf = new File(dir + "/payload/management.conf");

			for (Certificate ca : caErs) {
				File f = new File(dir + "/payload/caBundle/" + ca.getSubject().getCommonName() + ".cert");
				writeFile(f, ca.getCert());
			}

			// writeFile(key, aCertificate.getPrivateKey());
			writeFile(cert, certificate);
			writeFile(caBundleFile, caBundle);
			writeFile(id, aHostName);
			writeFile(deviceInfo,  "platform=runt\nlocale=en_US\n");
			writeFile(managementConf, managementConfPayload.replaceAll("%%MANAGER_HOSTNAME%%", managementHostName));
			SystemExecutor executor = new SystemExecutor();
			executor.setWorkingDirectory(new File(dir).getAbsolutePath());
			try {
				executor.runCommand("tar -zcvf payload.tgz payload/");
			} catch (SystemExecutorException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			byte[] payload = readFile(new File(dir + "/payload.tgz"));
			return payload;

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			delete(tempDir);
		}
	}

}

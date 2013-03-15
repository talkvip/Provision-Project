import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(value = "/WEB-INF/Provision-servlet.xml")
public class ExampleTest {

	@PersistenceContext
	EntityManager manager;

	@Test
	public void testMe() throws Exception {
		System.out.println("ok");
		Set<InetAddress> addresses = new HashSet<>();

		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface iface : Collections.list(ifaces)) {
			Enumeration<NetworkInterface> virtualIfaces = iface.getSubInterfaces();
			for (NetworkInterface viface : Collections.list(virtualIfaces)) {
				Enumeration<InetAddress> vaddrs = viface.getInetAddresses();
				for (InetAddress vaddr : Collections.list(vaddrs)) {
					addresses.add(vaddr);
				}
			}
			Enumeration<InetAddress> raddrs = iface.getInetAddresses();
			for (InetAddress raddr : Collections.list(raddrs)) {
				addresses.add(raddr);
			}
		}
		System.out.println(InetAddress.getLocalHost().getHostAddress());

	}

	@Test
	public void testReverseDNS() throws Exception {
		String ip = "98.137.49.56";
		String retVal = null;
		final String[] bytes = ip.split("\\.");
		if (bytes.length == 4) {
			try {
				final java.util.Hashtable<String, String> env = new java.util.Hashtable<String, String>();
				env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
				final javax.naming.directory.DirContext ctx = new javax.naming.directory.InitialDirContext(env);
				final String reverseDnsDomain = bytes[3] + "." + bytes[2] + "." + bytes[1] + "." + bytes[0] + ".in-addr.arpa";
				final javax.naming.directory.Attributes attrs = ctx.getAttributes(reverseDnsDomain, new String[] { "PTR", });
				for (final javax.naming.NamingEnumeration<? extends javax.naming.directory.Attribute> ae = attrs.getAll(); ae.hasMoreElements();) {
					final javax.naming.directory.Attribute attr = ae.next();
					final String attrId = attr.getID();
					for (final java.util.Enumeration<?> vals = attr.getAll(); vals.hasMoreElements();) {
						String value = vals.nextElement().toString();
						// System.out.println(attrId + ": " + value);

						if ("PTR".equals(attrId)) {
							final int len = value.length();
							if (value.charAt(len - 1) == '.') {
								// Strip out trailing period
								value = value.substring(0, len - 1);
							}
							retVal = value;
						}
					}
				}
				ctx.close();
			} catch (final javax.naming.NamingException e) {
				
			}
		}

		if (null == retVal) {
			try {
				retVal = java.net.InetAddress.getByName(ip).getCanonicalHostName();
			} catch (final java.net.UnknownHostException e1) {
				retVal = ip;
			}
		}

		System.out.println(retVal);

	}

}

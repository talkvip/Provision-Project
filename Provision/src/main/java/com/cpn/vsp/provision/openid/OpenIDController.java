package com.cpn.vsp.provision.openid;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ax.FetchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cpn.vsp.provision.resource.Resource;
import com.cpn.vsp.provision.resource.ResourceDAO;
import com.cpn.vsp.provision.user.User;
import com.cpn.vsp.provision.user.UserDAO;
import com.cpn.vsp.provision.user.session.Session;
import com.cpn.vsp.provision.user.session.SessionDAO;

@Controller
@RequestMapping("/openID")
@Transactional
public class OpenIDController {

	private static Logger log = LoggerFactory.getLogger(OpenIDController.class);
	public ConsumerManager manager = new ConsumerManager();

	@Autowired
	SessionDAO sessionDAO;

	@Autowired
	ResourceDAO resourceDAO;

	@Autowired
	UserDAO userDAO;

	@RequestMapping(method = RequestMethod.GET)
	public void test(@RequestParam("open_id") String id, @RequestParam("accountId") String accountId, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			// configure the return_to URL where your application will receive
			// the authentication responses from the OpenID provider
			String returnToUrl = "http://tomcat.dev.intercloud.net/";
			User user = userDAO.findByOpenId(id);
			if(user == null){
				throw new RuntimeException("Couldn't find the user for OpenID: " + id);
			}
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.HOUR, 1);

			Resource resource = resourceDAO.find(accountId);
			if (resource == null) {
				throw new RuntimeException("Couldn't find the resource by id: " + accountId);
			}
			boolean admin = false;
			if (user.equals(resource.getOwner())) {
				admin = true;
			} else if (!resource.getMembers().contains(user)) {
				throw new RuntimeException("This user is not authorized for that resource.");
			}
			Session session = sessionDAO.createSessionForUser(user, cal.getTime());
			sessionDAO.addDataToSession(session, "resourceId", accountId);
			sessionDAO.addDataToSession(session, "admin", new Boolean(admin).toString());
			
			Cookie cookie = new Cookie("sessionId", session.getId());
			cookie.setPath("/");
			response.addCookie(cookie);

			// --- Forward proxy setup (only if needed) ---
			// ProxyProperties proxyProps = new ProxyProperties();
			// proxyProps.setProxyName("proxy.example.com");
			// proxyProps.setProxyPort(8080);
			// HttpClientFactory.setProxyProperties(proxyProps);

			// perform discovery on the user-supplied identifier
			@SuppressWarnings("rawtypes")
			List discoveries = manager.discover(id);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			request.getSession().setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

			// Attribute Exchange example: fetching the 'email' attribute
			FetchRequest fetch = FetchRequest.createFetchRequest();
			fetch.addAttribute("email",
			// attribute alias
					"http://schema.openid.net/contact/email", // type URI
					true); // required

			// attach the extension to the authentication request
			authReq.addExtension(fetch);

			// Option 1: GET HTTP-redirect to the OpenID Provider endpoint
			// The only method supported in OpenID 1.x
			// redirect-URL usually limited ~2048 bytes
			response.sendRedirect(authReq.getDestinationUrl(true));
			return;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.sendRedirect("/?error=" + URLEncoder.encode(e.getMessage(), org.apache.commons.lang.CharEncoding.UTF_8));
			return;
		}

	}

}

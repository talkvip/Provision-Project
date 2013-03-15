package com.cpn.vsp.provision.notify;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.os4j.ComputeEndpoint;
import com.cpn.vsp.provision.EndpointFactory;
import com.cpn.vsp.provision.order.ProductOrder;
import com.cpn.vsp.provision.order.ProductOrderDAO;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.product.ProductDAO;
import com.cpn.vsp.provision.resource.Resource;
import com.cpn.vsp.provision.resource.ResourceController;
import com.cpn.vsp.provision.resource.ResourceDAO;
import com.cpn.vsp.provision.resource.VirtualResource;
import com.cpn.vsp.provision.user.User;
import com.cpn.vsp.provision.user.UserService;
import com.cpn.vsp.provision.user.session.Session;
import com.cpn.vsp.provision.user.session.SessionDAO;

@Controller
@RequestMapping("/notify")
@Transactional
public class NotificationController {

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	ProductDAO productDAO;
	@Autowired
	ResourceController resourceController;

	@Autowired
	ProductOrderDAO productOrderDAO;

	@Autowired
	UserService userService;
	@Autowired
	ResourceDAO resourceDAO;

	@Autowired
	SessionDAO sessionDAO;

	@Autowired
	EndpointFactory endpointFactory;

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	private abstract class EventHandler {
		public Result handleEvent(Event anEvent) {
			try {
				String resultString = run(anEvent);
				Result result = new Result();
				result.setSuccess("true");
				String accountId = getAccountId();
				if (accountId != null) {
					result.setAccountIdentifier(accountId);
				}
				result.setMessage(resultString);
				return result;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				Result result = new Result();
				result.setSuccess("false");
				result.setMessage(e.getMessage());
				return result;
			}

		}

		public abstract String run(Event anEvent) throws Exception;

		public String getAccountId() {
			return null;
		}

	}

	@RequestMapping("/subscription/create")
	@Logged
	public @ResponseBody
	Result subscriptionCreate(@RequestParam String url, @RequestParam("productId") final String aProductId) throws Exception {
		final ResourceController myResourceController = resourceController;
		return new EventHandler() {
			String accountId;

			@Override
			public String getAccountId() {
				return accountId;
			}

			@Override
			public String run(Event anEvent) throws Exception {
				Product p = productDAO.getProduct(aProductId);
				if (p == null) {
					throw new Exception("Couldn't find the product: " + aProductId);
				}
				VirtualResource resource = myResourceController.newResource(p);
				try {
					ProductOrder productOrder = anEvent.getPayload().getOrder();
					User creator = userService.findOrCreate(anEvent.getCreator());
					if (productOrder == null) {
						productOrder = new ProductOrder();
					}
					productOrder.setCreator(creator);
					productOrder.setProduct(p);
					productOrderDAO.persist(productOrder);
					resource.setOrder(productOrder);
					resource.setOwner(creator);
					resource = (VirtualResource) resourceDAO.merge(resource);

					Calendar cal = new GregorianCalendar();
					cal.add(Calendar.YEAR, 1);

					User masterUser = userService.findMasterUser();
					Session session = sessionDAO.createSessionForUser(masterUser, cal.getTime());
					sessionDAO.addDataToSession(session, "resourceId", resource.getId());
					sessionDAO.addDataToSession(session, "admin", new Boolean(true).toString());
					
					try {
						ComputeEndpoint cp = endpointFactory.getComputeEndpoint();
						cp.renameServer(resource.getServerId(), "VCG for " + creator.getEmail() + " (" + anEvent.getPayload().getCompany().getName() + ")");
					} catch (Exception e) {
						logger.warn("Couldn't rename a server...", e);
					}
				} catch (Exception e) {
					resourceController.terminateResouce(resource.getId());
					throw new Exception(e.getMessage(), e);
				}
				accountId = resource.getId();
				return "Account established.";
			}
		}.handleEvent(getEventForToken(url));

	}

	@RequestMapping("/subscription/change")
	@Logged
	public @ResponseBody
	Result subscriptionChange(@RequestParam String url, @RequestParam("productId") final String aProductId) throws Exception {
		return new EventHandler() {
			@Override
			public String run(Event anEvent) throws Exception {
				Resource resource = resourceDAO.find(anEvent.getPayload().getAccount().getAccountIdentifier());
				ProductOrder productOrder = anEvent.getPayload().getOrder();
				Product p = productDAO.getProduct(aProductId);
				if (p == null) {
					throw new Exception("Couldn't find the product: " + aProductId);
				}
				User user = userService.findOrCreate(anEvent.getCreator());
				if (productOrder == null) {
					productOrder = new ProductOrder();
				}
				productOrder.setCreator(user);
				productOrder.setProduct(p);
				productOrderDAO.persist(productOrder);
				if (resource != null) {
					resource.setOwner(user);
					resource.setOrder(productOrder);
					resourceDAO.merge(resource);
				}
				return "Account updated.";
			}
		}.handleEvent(getEventForToken(url));
	}

	@RequestMapping("/subscription/cancel")
	public @ResponseBody
	Result subscriptionCancel(@RequestParam String url) throws Exception {
		return new EventHandler() {
			@Override
			public String run(Event anEvent) throws Exception {
				Resource resource = resourceDAO.find(anEvent.getPayload().getAccount().getAccountIdentifier());
				if (resource != null) {
					resourceController.terminateResouce(resource.getId());
				}
				return "Subscription canceled.";
			}
		}.handleEvent(getEventForToken(url));

	}

	@RequestMapping("/subscription/status")
	public @ResponseBody
	Result subscriptionStatus(@RequestParam String url) throws Exception {
		Result result = new Result();
		result.setSuccess("true");
		return result;
	}

	@RequestMapping("/user/assign")
	public @ResponseBody
	Result userAssign(@RequestParam String url) throws Exception {
		return new EventHandler() {
			@Override
			public String run(Event anEvent) throws Exception {
				if (!anEvent.getPayload().getAccount().getAccountIdentifier().equals("dummy-account")) {
					Resource resource = resourceDAO.find(anEvent.getPayload().getAccount().getAccountIdentifier());
					if (resource == null) {
						throw new Exception("Couldn't find the account: " + anEvent.getPayload().getAccount().getAccountIdentifier());
					}
					User user = userService.findOrCreate(anEvent.getPayload().getUser());
					resource.getMembers().add(user);
					resourceDAO.merge(resource);
				}
				return "User assigned to resource.";
			}
		}.handleEvent(getEventForToken(url));
	}

	@RequestMapping("/user/unassign")
	public @ResponseBody
	Result userUnassign(@RequestParam String url) throws Exception {
		return new EventHandler() {
			@Override
			public String run(Event anEvent) throws Exception {
				Resource resource = resourceDAO.find(anEvent.getPayload().getAccount().getAccountIdentifier());
				if (resource == null) {
					throw new Exception("Couldn't find the account: " + anEvent.getPayload().getAccount().getAccountIdentifier());
				}
				User user = userService.findOrCreate(anEvent.getPayload().getUser());
				resource.getMembers().remove(user);
				resourceDAO.merge(resource);
				return "User unassigned from resource.";
			}
		}.handleEvent(getEventForToken(url));
	}

	public static Event getEventForToken(String token) throws Exception {
		EventQuery query = new EventQuery(token);
		return query.execute();

	}
}

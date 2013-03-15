package com.cpn.vsp.provision.server;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.os4j.model.Flavor;
import com.cpn.vsp.provision.EndpointFactory;

@Controller
@RequestMapping(value = "/server/flavor")
public class FlavorController {

	@Autowired
	EndpointFactory endPointFactory;

	@RequestMapping(method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	List<Flavor> listFlavors() throws IOException {
		return endPointFactory.getComputeEndpoint().listFlavors();
	}

}

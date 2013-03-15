package com.cpn.vsp.provision.capability;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cpn.vsp.provision.AbstractRestController;

@Controller
@RequestMapping("/capability")
@Transactional

public class CapabilityController extends AbstractRestController<Capability>{

	@Override
	public Class<Capability> getPersistenceClass() {
		return Capability.class;
	}
}

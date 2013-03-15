package com.cpn.vsp.provision.image;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.os4j.model.Image;
import com.cpn.vsp.provision.EndpointFactory;

@Controller
public class ImageController {

	@Autowired
	EndpointFactory endPointFactory;

	@RequestMapping(value = "/image", method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	List<Image> listImages() throws ServletException, IOException {
		return endPointFactory.getComputeEndpoint().listImages();
	}

}
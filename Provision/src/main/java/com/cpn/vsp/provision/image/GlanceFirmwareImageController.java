package com.cpn.vsp.provision.image;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.os4j.glance.GlanceEndPoint;
import com.cpn.os4j.glance.GlanceImage;

@Controller
@RequestMapping(value="/firmware")
public class GlanceFirmwareImageController {

	@PersistenceContext
	EntityManager entityManager;
	
	@RequestMapping(method = RequestMethod.GET)
	@Logged
	public @ResponseBody List<GlanceFirmwareImage> listImages(){
		return entityManager.createQuery("from GlanceFirmwareImage", GlanceFirmwareImage.class).getResultList();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@Logged
	public @ResponseBody
	void test(@PathVariable String id, HttpServletResponse aResponse) throws IOException {
		GlanceFirmwareImage image = entityManager.find(GlanceFirmwareImage.class, id);
		image.download(aResponse.getOutputStream());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/checksum")
	@Logged
	public @ResponseBody
	String getChecksum(@PathVariable String id) throws IOException {
		GlanceFirmwareImage image = entityManager.find(GlanceFirmwareImage.class, id);
		return image.getChecksum();
	}
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, value="/{arch}/{name}")
	@Transactional
	@Logged
	public @ResponseBody GlanceFirmwareImage createImage(HttpServletRequest aRequest, @PathVariable String name, @PathVariable String arch) throws IOException{
		GlanceImage image = GlanceEndPoint.upload(name, aRequest.getInputStream(), Long.parseLong(aRequest.getHeader("content-length")));
		GlanceFirmwareImage firmware = new GlanceFirmwareImage();
		firmware.setUri("http://archive:9292/v1/images/" + image.getId());
		firmware.setRevision(new Date());
		firmware.setArchitecture(arch);
		firmware.setName(name);
		firmware.setChecksum(image.getChecksum());
		firmware.setSize(Long.parseLong(aRequest.getHeader("content-length")));
		entityManager.persist(firmware);
		return firmware;
	}

}

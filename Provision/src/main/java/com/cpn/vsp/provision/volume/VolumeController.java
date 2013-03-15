package com.cpn.vsp.provision.volume;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/volume")
@Transactional
public class VolumeController {

//	@Autowired
//	EndPoint endPoint;
//
//	@RequestMapping(method = RequestMethod.GET)
//	@Logged
//	public @ResponseBody
//	List<Volume> listVolumes() throws IOException {
//		return endPoint.getVolumes();
//	}
//
//	@RequestMapping(method = RequestMethod.POST)
//	@Logged
//	public @ResponseBody
//	Volume newVolume(@RequestBody VolumeRequest aRequest) throws Exception {
//		return endPoint.createVolume(aRequest.getAvailabilityZone(), aRequest.getSize());
//	}
//	
//	@RequestMapping(method = RequestMethod.PUT)
//	@Logged
//	public @ResponseBody
//	Volume modifyVolume(@RequestBody Volume aVolume) throws Exception {
//		Volume real = endPoint.getVolume(aVolume.getKey());
//		resolveDifferences(real, aVolume);
//		return endPoint.getVolume(aVolume.getKey());
//	}
//
//	private void resolveDifferences(Volume realVolume, Volume aVolume) throws ServerErrorException, IOException {
//		List<VolumeAttachment> realAttachments = realVolume.getVolumeAttachments();
//		List<VolumeAttachment> expectedAttachments = aVolume.getVolumeAttachments();
//		outer: for(VolumeAttachment real : realAttachments){
//			for(VolumeAttachment expected : expectedAttachments){
//				if(real.getInstanceId().equals(expected.getInstanceId())){
//					continue outer;
//				}
//			}
//			realVolume.detach();
//		}
//		outer: for(VolumeAttachment expected : realAttachments){
//			for(VolumeAttachment real : expectedAttachments){
//				if(expected.getInstanceId().equals(real.getInstanceId())){
//					continue outer;
//				}
//			}
//			realVolume.attachToInstance(endPoint.getInstance(expected.getInstanceId()), expected.getDevice());
//		}
//	}
//
//	@RequestMapping(value = "/{volumeId}", method = RequestMethod.GET)
//	@Logged
//	public @ResponseBody
//	Volume queryVolume(@PathVariable("volumeId") String aVolumeId) throws IOException {
//		return endPoint.getVolume(aVolumeId);
//	}
//
//	@RequestMapping(value = "/{volumeId}", method = RequestMethod.DELETE)
//	@Logged
//	public @ResponseBody
//	void deleteVolume(@PathVariable("volumeId") String aVolumeId) throws Exception {
//		Volume v = endPoint.getVolume(aVolumeId);
//		if(v.getVolumeAttachments().size() > 0){
//			v.detach().waitUntilAvailable(120000);
//		}
//		v.delete().waitUntilDeleted(120000);
//		return;
//	}
//	
//	@RequestMapping(value = "/{volumeId}/force", method = RequestMethod.DELETE)
//	@Logged
//	public @ResponseBody
//	void terminateInstance(@PathVariable("volumeId") String aVolumeId) throws Exception {
//		Volume v = endPoint.getVolume(aVolumeId);
//		if(v.getVolumeAttachments().size() > 0){
//			v.forceDetach().waitUntilAvailable(120000);
//		}
//		v.delete().waitUntilDeleted(120000);
//		return;
//	}

}

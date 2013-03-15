package com.cpn.os4j.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.cpn.os4j.ComputeEndpoint;

public class Server extends AbstractOpenStackModel {


	private static final long serialVersionUID = 8308147727947512720L;
	String id;
	String name;
	String updated;
	String created;
	String hostId;
	String status;
	@JsonProperty("user_id")
	String userId;
	int progress;
	String accessIPv4;
	String accessIPv6;
	@JsonProperty("key_name")
	String keyName;
	@JsonProperty("tenant_id")
	String tenantId;
	Image image;
	Flavor flavor;
	String adminPass;
	IPAddresses addresses;
	@JsonProperty("config_drive")
	String configDrive;
	Map<String, String> metadata;
	@JsonProperty("OS-EXT-STS:power_state")
	String powerState;
	@JsonProperty("OS-EXT-STS:vm_state")
	String vmState;
	@JsonProperty("OS-DCF:diskConfig")
	String diskConfig;
	@JsonProperty("OS-EXT-STS:task_state")
	String taskState;

	@JsonIgnore
	ComputeEndpoint computeEndpoint;

	@JsonIgnore
	public Server waitUntilRunning(long aTimeout) throws InterruptedException {
		Server server = this;
		while (!("ACTIVE".equals(server.getStatus()))) {
			if (aTimeout < 0) {
				return server;
			}
			if ("ERROR".equals(server.getStatus())) {
				throw new RuntimeException("Error while waiting for an instance to become available. State is: " + server.getStatus());
			}
			Thread.sleep(1000);
			aTimeout -= 1000;
			server = getComputeEndpoint().getServerDetails(this);
		}
		return server;
	}

	public Server associateIp(IPAddress address) {
		return associateIp(address.getIp());
	}

	public Server associateIp(String address){
		return getComputeEndpoint().associateIp(this.getId(), address);
	}

	@JsonIgnore
	public Server delete() {
		getComputeEndpoint().deleteServer(this);
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getAccessIPv4() {
		return accessIPv4;
	}

	public void setAccessIPv4(String accessIPv4) {
		this.accessIPv4 = accessIPv4;
	}

	public String getAccessIPv6() {
		return accessIPv6;
	}

	public void setAccessIPv6(String accessIPv6) {
		this.accessIPv6 = accessIPv6;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}

	public IPAddresses getAddresses() {
		return addresses;
	}

	public void setAddresses(IPAddresses addresses) {
		this.addresses = addresses;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getPowerState() {
		return powerState;
	}

	public void setPowerState(String powerState) {
		this.powerState = powerState;
	}

	public String getVmState() {
		return vmState;
	}

	public void setVmState(String vmState) {
		this.vmState = vmState;
	}

	public String getDiskConfig() {
		return diskConfig;
	}

	public void setDiskConfig(String diskConfig) {
		this.diskConfig = diskConfig;
	}

	public String getConfigDrive() {
		return configDrive;
	}

	public void setConfigDrive(String configDrive) {
		this.configDrive = configDrive;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("name", name).append("updated", updated).append("created", created).append("hostId", hostId).append("status", status).append("userId", userId).append("progress", progress).append("accessIPv4", accessIPv4)
				.append("accessIPv6", accessIPv6).append("keyName", keyName).append("tenantId", tenantId).append("image", image).append("flavor", flavor).append("adminPass", adminPass).append("addresses", addresses).append("configDrive", configDrive)
				.append("metadata", metadata).append("powerState", powerState).append("vmState", vmState).append("diskConfig", diskConfig).append("taskState", taskState);
		return builder.toString();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}

	public ComputeEndpoint getComputeEndpoint() {
		return computeEndpoint;
	}

	public Server setComputeEndpoint(ComputeEndpoint computeEndpoint) {
		this.computeEndpoint = computeEndpoint;
		return this;
	}

}
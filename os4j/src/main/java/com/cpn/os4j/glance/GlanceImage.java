package com.cpn.os4j.glance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class GlanceImage {

	private String id;
	private String uri;
	private String name;
	private String diskFormat;
	private String containerFormat;
	private long size;
	private String checksum;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private String status;
	private boolean deleted;
	private boolean isPublic;
	private boolean isProtected;
	private long minRam;
	private long minDisk;
	private String owner;
	private Map<String, String> properties = new HashMap<>();

	@JsonIgnore
	private transient HttpClient client = new HttpClient();

	public GlanceImage() {

	}

	public GlanceImage(String aUri) {
		uri = aUri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty(value = "disk_format")
	public String getDiskFormat() {
		return diskFormat;
	}

	@JsonProperty(value = "disk_format")
	public void setDiskFormat(String diskFormat) {
		this.diskFormat = diskFormat;
	}

	@JsonProperty(value = "container_format")
	public String getContainerFormat() {
		return containerFormat;
	}

	@JsonProperty(value = "container_format")
	public void setContainerFormat(String containerFormat) {
		this.containerFormat = containerFormat;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@JsonProperty(value = "created_at")
	public Date getCreatedAt() {
		return createdAt;
	}

	@JsonProperty(value = "created_at")
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty(value = "updated_at")
	public Date getUpdatedAt() {
		return updatedAt;
	}

	@JsonProperty(value = "updated_at")
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@JsonProperty(value = "deleted_at")
	public Date getDeletedAt() {
		return deletedAt;
	}

	@JsonProperty(value = "deleted_at")
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty(value = "is_public")
	public boolean isPublic() {
		return isPublic;
	}

	@JsonProperty(value = "is_public")
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@JsonProperty(value = "min_ram")
	public long getMinRam() {
		return minRam;
	}

	@JsonProperty(value = "min_ram")
	public void setMinRam(long minRam) {
		this.minRam = minRam;
	}

	@JsonProperty(value = "min_disk")
	public long getMinDisk() {
		return minDisk;
	}

	@JsonProperty(value = "min_disk")
	public void setMinDisk(long minDisk) {
		this.minDisk = minDisk;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void populate() throws HttpException, IOException {
		HeadMethod method = new HeadMethod(uri);
		client.executeMethod(method);
		populateFromHeaders(method.getResponseHeaders());
	}

	public void populateFromHeaders(Header[] someHeaders) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (Header h : someHeaders) {
			if (h.getName().equals("x-image-meta-uri")) {
				setUri(h.getValue());
			}
			if (h.getName().equals("x-image-meta-name")) {
				setName(h.getValue());
			}
			if (h.getName().equals("x-image-meta-disk_format")) {
				setDiskFormat(h.getValue());
			}
			if (h.getName().equals("x-image-meta-container_format")) {
				setContainerFormat(h.getValue());
			}
			if (h.getName().equals("x-image-meta-size")) {
				setSize(Long.parseLong(h.getValue()));
			}
			if (h.getName().equals("x-image-meta-checksum")) {
				setChecksum(h.getValue());
			}
			if (h.getName().equals("x-image-meta-created_at")) {
				try {
					setCreatedAt(dateFormat.parse(h.getValue()));
				} catch (ParseException e) {
				}
			}
			if (h.getName().equals("x-image-meta-updated_at")) {
				try {
					setUpdatedAt(dateFormat.parse(h.getValue()));
				} catch (ParseException e) {
				}
			}
			if (h.getName().equals("x-image-meta-status")) {
				setStatus(h.getValue());
			}
			if (h.getName().equals("x-image-meta-is-public")) {
				setPublic(Boolean.parseBoolean(h.getValue()));
			}
			if (h.getName().equals("x-image-meta-min-ram")) {
				setMinRam(Long.parseLong(h.getValue()));
			}
			if (h.getName().equals("x-image-meta-min-disk")) {
				setMinDisk(Long.parseLong(h.getValue()));
			}
			if (h.getName().equals("x-image-meta-owner")) {
				setOwner(h.getValue());
			}
			if (h.getName().contains("x-image-meta-property-")) {
				getProperties().put(h.getName().replaceAll("x-image-meta-property-", ""), h.getValue());
			}
		}
	}

	public GlanceImage setMetadata() {
		PutMethod method = new PutMethod(uri);
		try {
			method.addRequestHeader("x-image-meta-name", getName());
			if (getDiskFormat() != null) {
				method.addRequestHeader("x-image-meta-disk_format", getDiskFormat());
			}
			if (getDiskFormat() != null) {
				method.addRequestHeader("x-image-meta-disk_format", getDiskFormat());
			}
			if (getContainerFormat() != null) {
				method.addRequestHeader("x-image-meta-container_format", getContainerFormat());
			}
			if (getSize() > 0l) {
				method.addRequestHeader("x-image-meta-size", new Long(getSize()).toString());
			}
			if (getChecksum() != null) {
				method.addRequestHeader("x-image-meta-checksum", getChecksum());
			}
			method.addRequestHeader("x-image-meta-checksum", new Boolean(isPublic()).toString());
			if (getMinRam() > 0) {
				method.addRequestHeader("x-image-meta-min-ram", new Long(getMinRam()).toString());
			}
			if (getMinDisk() > 0) {
				method.addRequestHeader("x-image-meta-min-disk", new Long(getMinDisk()).toString());
			}
			if (getOwner() != null) {
				method.addRequestHeader("x-image-meta-owner", getOwner());
			}
			for (Entry<String, String> kv : getProperties().entrySet()) {
				method.addRequestHeader("x-image-meta-property-" + kv.getKey(), kv.getValue());
			}
			client.executeMethod(method);
			populateFromHeaders(method.getResponseHeaders());
			return this;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
	}

	public void download(OutputStream stream) {
		GetMethod method = new GetMethod(getUri());
		try {
			client.executeMethod(method);
			byte[] buffer = new byte[4096];
			int n = 0;
			InputStream input = method.getResponseBodyAsStream();
			while (-1 != (n = input.read(buffer))) {
				stream.write(buffer, 0, n);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public HttpClient getClient() {
		return client;
	}

	@JsonIgnore
	public void setClient(HttpClient client) {
		this.client = client;
	}

	@JsonProperty(value = "deleted")
	public boolean isDeleted() {
		return deleted;
	}

	@JsonProperty(value = "deleted")
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonProperty(value = "protected")
	public boolean isProtected() {
		return isProtected;
	}

	@JsonProperty(value = "protected")
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

}

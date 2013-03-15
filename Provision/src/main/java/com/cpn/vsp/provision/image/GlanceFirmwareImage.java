package com.cpn.vsp.provision.image;

import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.cpn.os4j.glance.GlanceImage;

@Entity
public class GlanceFirmwareImage {

	@Id
	
	private String id = UUID.randomUUID().toString();
	
	private String uri;
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	private Date revision;
	private String architecture;
	private String checksum;
	private long size;
	
	
	public String getId() {
		return id;
	}

	public void setId(String anId) {
		id = anId;
	}
	
	@JsonIgnore
	public void download(OutputStream aStream) {
		GlanceImage image = new GlanceImage(uri);
		image.download(aStream);
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

	public Date getRevision() {
		return revision;
	}

	public void setRevision(Date revision) {
		this.revision = revision;
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("uri", uri).append("name", name)
				.append("revision", revision)
				.append("architecture", architecture)
				.append("checksum", checksum).append("size", size);
		return builder.toString();
	}
	
}

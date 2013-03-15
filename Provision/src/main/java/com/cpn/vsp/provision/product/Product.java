package com.cpn.vsp.provision.product;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.cpn.vsp.provision.capability.Capability;
import com.cpn.vsp.provision.metadata.Metadata;
import com.cpn.vsp.provision.order.ProductOrder;

@Entity
public class Product implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6742801837274664245L;
	@Id
	private String id = UUID.randomUUID().toString();
	private String name;
	private String description;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createdOn = new Date();

	private String provisioningStrategy;
	
	@ManyToMany(targetEntity = Capability.class, fetch = FetchType.EAGER)
	@JoinTable(name = "Capability_Product", joinColumns = { @JoinColumn(name = "product_id") }, inverseJoinColumns = { @JoinColumn(name = "capability_id") })
	private Set<Capability> capabilities = new HashSet<>();
	private String locale;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Metadata.class, fetch = FetchType.EAGER)
	@JoinTable(name = "Product_Metadata", joinColumns = { @JoinColumn(name = "product_id") }, inverseJoinColumns = { @JoinColumn(name = "metadata_id") })
	private Set<Metadata> metadata = new HashSet<>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch=FetchType.EAGER)
	private Set<ProductOrder> orders = new HashSet<>();

	
	private String project_id ="";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvisioningStrategy() {
		return provisioningStrategy;
	}

	public void setProvisioningStrategy(String provisioningStrategy) {
		this.provisioningStrategy = provisioningStrategy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Capability> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Set<Capability> capabilities) {
		this.capabilities = capabilities;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Set<Metadata> getMetadata() {
		return metadata;
	}

	public void setMetadata(Set<Metadata> metadata) {
		this.metadata = metadata;
	}

	@JsonAnySetter
	public void setAny(String name, String value) {
		for (Metadata m : metadata) {
			if (m.getName().equals(name)) {
				m.setValue(value.toString());
				return;
			}
		}
		metadata.add(new Metadata(name, value));
	}

	@JsonIgnore
	public String getMetadata(String aName) {
		for (Metadata m : metadata) {
			if (m.getName().toLowerCase().equals(aName.toLowerCase())) {
				return m.getValue();
			}
		}
		return null;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("name", name)
				.append("description", description)
				.append("createdOn", createdOn)
				.append("provisioningStrategy", provisioningStrategy)
				.append("capabilities", capabilities).append("locale", locale)
				.append("metadata", metadata);
		return builder.toString();

	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public Set<ProductOrder> getOrders() {
		return orders;
	}

	public void setOrders(Set<ProductOrder> orders) {
		this.orders = orders;
	}

}
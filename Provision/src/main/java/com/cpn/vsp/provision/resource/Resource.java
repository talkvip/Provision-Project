package com.cpn.vsp.provision.resource;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;

import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.database.DataTransferObject;
import com.cpn.vsp.provision.order.ProductOrder;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.user.User;

@Entity
@JsonTypeInfo(use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "fqdn")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "resource_type", discriminatorType = DiscriminatorType.STRING)
public class Resource implements DataTransferObject{


	private static final long serialVersionUID = 125022647768659293L;

	@Id
	private String id = UUID.randomUUID().toString();

	@OneToOne
	private Product product;

	@OneToOne(fetch=FetchType.EAGER)
	private ProductOrder productOrder;
	
	@OneToOne
	private User owner;
	
	private String ipAddress;
	private String hostName;
	
	@ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinTable(name = "Resource_User", joinColumns = { @JoinColumn(name = "resource_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private Set<User> members = new HashSet<>();

	@OneToOne(cascade = CascadeType.REMOVE)
	private Certificate certificate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public void accept(ResourceVisitor abstractResourceVisitor){
		throw new UnsupportedOperationException();
	}

	public void setOrder(ProductOrder anOrder) {
		productOrder = anOrder;
	}

	public ProductOrder getOrder() {
		return productOrder;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	
	

}

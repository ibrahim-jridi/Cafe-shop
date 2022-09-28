package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@NamedQuery(name="Product.getProducts",query="select new wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
@NamedQuery(name="Product.updateProductStatus",query="update Product p set p.status=:status where p.id=:id")
@NamedQuery(name="Product.getProductByCategory",query="select new wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")
@NamedQuery(name="Product.getProductById",query="select new wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")



@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable{
	public static final Long serialVersionUid = 123456L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_PD",nullable=false)
	private Category category;
	
	@Column(name="description")
	private String description;
	
	@Column(name="price")
	private Integer price;
	
	@Column(name = "status")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

package com.example.recipebuddyapp.model;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "recipeid", nullable = false, unique = true)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = true)
	private Date createDate;

	@Column(name = "category", nullable = false)
	private String category;

	@Column(name = "description", nullable = false, columnDefinition = "varchar(30000)")
	private String description;

	@Lob
	@Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;
	
	@ManyToOne
	@JoinColumn(name = "id")
	private User user;

	public Recipe() {
	}

	public Recipe(String name, Date createDate, String category, String description, byte[] image, User user) {
		super();
		this.name = name;
		this.createDate = createDate;
		this.category = category;
		this.description = description;
		this.image = image;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "recipe [id=" + id + ", name=" + name + ", createDate=" + createDate + ", Category=" + category
				+ ", Description=" + description + ", image=" + Arrays.toString(image) + "]";

	}
}

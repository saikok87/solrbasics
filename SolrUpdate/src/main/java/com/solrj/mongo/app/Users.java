package com.solrj.mongo.app;

import org.apache.solr.client.solrj.beans.Field;

public class Users {
	
	@Field("id")
	String id;
	
	@Field("name")
	String name;
	
	@Field("age")
	String age;
	
	@Field("createdDate")
	String createdDate;
	
	public Users() {}
	
	public Users(String id, String name, String age, String createdDate) {
		super();
		this.id=id;
		this.name=name;
		this.age=age;
		this.createdDate=createdDate;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	

}

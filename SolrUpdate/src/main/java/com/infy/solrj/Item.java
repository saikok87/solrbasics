package com.infy.solrj;

import org.apache.solr.client.solrj.beans.Field;

public class Item {
	@Field("id")
    String id;
    @Field("title")
    String title;
   /* @Field
    Float price;
    */
    public Item(){} // Empty constructor is required
    
    public Item(String id, String name)
    {
    	super();
    	this.id=id;
    	this.title=title;
    	//this.price=price;
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
	public String getTitle() {
		return title;
	}

	/**
	 * @param name the name to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the price
	 *//*
	public Float getPrice() {
		return price;
	}

	*//**
	 * @param price the price to set
	 *//*
	public void setPrice(Float price) {
		this.price = price;
	}
    */
    
}

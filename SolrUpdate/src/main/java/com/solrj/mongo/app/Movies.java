package com.solrj.mongo.app;

import org.apache.solr.client.solrj.beans.Field;

public class Movies {
	
	@Field("id")
	private String id;
	
	@Field("moviename")
	private String movieName;
	
	@Field("releaseyear")
	private String releaseYear;
	
	public Movies() { }
	
	
	public Movies(String id, String name, String year) {
		super();
		this.id=id;
		this.movieName=name;
		this.releaseYear=year;
	}

	/**
	 * @return the movieName
	 */
	public String getMovieName() {
		return movieName;
	}

	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	/**
	 * @return the releaseYear
	 */
	public String getReleaseYear() {
		return releaseYear;
	}

	/**
	 * @param releaseYear the releaseYear to set
	 */
	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
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
	
	

}

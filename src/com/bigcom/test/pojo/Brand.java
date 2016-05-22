package com.bigcom.test.pojo;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Brand {
	@JsonIgnore
	int id;
	@JsonIgnore
	String image_file;
	@JsonIgnore
	String meta_keywords;
	@JsonIgnore
	String search_keywords;
	@JsonIgnore
	String meta_description;
	String page_title;
	String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage_file() {
		return image_file;
	}
	public void setImage_file(String image_file) {
		this.image_file = image_file;
	}
	public String getPage_title() {
		return page_title;
	}
	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}
	public String getMeta_keywords() {
		return meta_keywords;
	}
	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}
	public String getSearch_keywords() {
		return search_keywords;
	}
	public void setSearch_keywords(String search_keywords) {
		this.search_keywords = search_keywords;
	}
	public String getMeta_description() {
		return meta_description;
	}
	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
	}
}

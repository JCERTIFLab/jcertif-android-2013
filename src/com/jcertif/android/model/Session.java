package com.jcertif.android.model;

import java.util.Date;
import java.util.List;

public class Session {

	private int id;
	private String title;
	private String summary;
	private String description;
	private String status;
	private String keyword;
	private List<Categorie> category;
	private Date end;
	private List<Speaker> speakers;
	private int salle;
	private String version;
	
	public Session() {
		super();
	}

	public Session(int id, String title, String summary, String description,
			String status, String keyword, List<Categorie> category, Date end,
			List<Speaker> speakers, int salle, String version) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.description = description;
		this.status = status;
		this.keyword = keyword;
		this.category = category;
		this.end = end;
		this.speakers = speakers;
		this.salle = salle;
		this.version = version;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Categorie> getCategory() {
		return category;
	}

	public void setCategory(List<Categorie> category) {
		this.category = category;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(List<Speaker> speakers) {
		this.speakers = speakers;
	}

	public int getSalle() {
		return salle;
	}

	public void setSalle(int salle) {
		this.salle = salle;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}

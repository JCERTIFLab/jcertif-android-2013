package com.jcertif.android.model;



import java.util.List;



public class Room extends JCertifObject {

	
	private String id;
    private String name;	
    private String site;
    private String seats;
    private String description;
    private String photo;
   
	public Room() {
		super();
	}

	public Room(String id, String name, String site, String seats,
			String description, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.site = site;
		this.seats = seats;
		this.description = description;
		this.photo = photo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
    
    
    
    
}
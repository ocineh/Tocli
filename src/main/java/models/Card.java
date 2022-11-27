package models;

import java.util.Date;

public class Card {
	private final Date added;
	private Date due;
	private String name;
	private String description;

	public Card(String name, String description, Date due) {
		this.name = name;
		this.description = description;
		this.due = due;
		this.added = new Date();
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

	public Date getDue() {
		return due;
	}

	public void setDue(Date due) {
		this.due = due;
	}
}

package com.quaresma.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity
@Table(name = "credenciais")
public class Credenciais implements Serializable {

	@Id
	@GeneratedValue(generator = "fk_credenciais_id_user")
	@org.hibernate.annotations.GenericGenerator(name = "fk_credenciais_id_user", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
	private int id;

	@OneToOne
	private User user;

	@Column(name = "user_name", nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column
	private String token;

	public Credenciais(User user, String username, String password, String token) {
		super();
		this.user = user;
		this.username = username;
		this.password = password;
		this.token = token;
	}

	public Credenciais() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credenciais other = (Credenciais) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
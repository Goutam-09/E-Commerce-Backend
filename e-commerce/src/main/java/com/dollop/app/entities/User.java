package com.dollop.app.entities;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
	public User(String userId2) {
		this.userId = userId2;
	}

	@Id
	private String userId;
	@Column(name = "user_name")
	private String name;
	@Column(name = "user_email",unique = true)
	private String email;
	@Column(name = "user_password",length = 15)
	private String password;
	@Column(name = "user_gender",length = 6)
	private String gender;
	@Column(name = "user_about",length = 100)
	private String about;

	@ElementCollection 
	@CollectionTable(
			name = "image_table",
			joinColumns = @JoinColumn(name="userId")			
			)
	private List<String> imageName;
	
	@OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY,mappedBy = "user")
	private List<Order> ordersList;
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	private Set<Role> roles;
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
	private Cart cart;
	
}

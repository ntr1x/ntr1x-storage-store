package com.ntr1x.storage.store.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ntr1x.storage.core.model.Image;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.security.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "offers"
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Offer extends Resource {

	@XmlElement
	@JsonManagedReference
	@ManyToOne
    @JoinColumn(name = "RelateId", nullable = true, updatable = false)
    private Resource relate;
	
	@XmlElement
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = true, updatable = false)
	private User user;
	
	@XmlElement
	@JsonManagedReference
    @ManyToOne
	@JoinColumn(name = "ImageId", nullable = true)
	private Image image;
	
	@Column(name = "Title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "Promo", nullable = true)
	private String promo;
	
	@Lob
	@ResourceExtra
	@Column(name = "Body", nullable = true)
	private String body;
}

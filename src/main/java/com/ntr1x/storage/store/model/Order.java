package com.ntr1x.storage.store.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ntr1x.storage.core.converter.ConverterProvider.LocalDateTimeConverter;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.security.model.User;

import io.swagger.annotations.ApiModelProperty;
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
	name = "orders"
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Order extends Resource {
	
	public enum State {
		
		NEW,
		ACCEPTED,
		DECLINED,
		CANCELLED,
		;
		
		public EnumSet<State> arrows() {
			
			switch (this) {
			case NEW:
				return EnumSet.of(ACCEPTED, DECLINED, CANCELLED);
			case ACCEPTED:
				return EnumSet.noneOf(Order.State.class);
			case DECLINED:
				return EnumSet.noneOf(Order.State.class);
			case CANCELLED:
				return EnumSet.noneOf(Order.State.class);
			default:
				throw new IllegalStateException();
			}
		}
	}
	
	@Column(name = "State", nullable = false)
	private State state;
	
	@XmlElement
	@JsonManagedReference
	@ManyToOne
    @JoinColumn(name = "RelateId", nullable = false, updatable = false)
    private Resource relate;
	
	@XmlElement
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = false)
	private User user;
		
	@Column(name = "Created")
	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
	@ApiModelProperty(example="2016-10-07T04:05")
	private LocalDateTime created;
	
	@Column(name = "Quantity", nullable = true, scale = 2, precision = 10)
	private BigDecimal quantity;
}

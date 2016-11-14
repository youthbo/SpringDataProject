package se.plushogskolan.sdj.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "created_date", nullable = false)
    @CreatedDate
    private ZonedDateTime createdDate;
 
    @Column(name = "updated_date")
    @LastModifiedDate
    private ZonedDateTime updatedDate;
    
    public Long getId() {
		return id;
	}
}

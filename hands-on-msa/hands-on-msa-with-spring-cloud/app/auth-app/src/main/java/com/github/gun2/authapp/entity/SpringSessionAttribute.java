package com.github.gun2.authapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "SPRING_SESSION_ATTRIBUTES")
public class SpringSessionAttribute {
    @EmbeddedId
    private SpringSessionAttributeId id;

    @MapsId("sessionPrimaryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "SESSION_PRIMARY_ID", nullable = false)
    private SpringSession sessionPrimary;

    @NotNull
    @Lob
    @Column(name = "ATTRIBUTE_BYTES", nullable = false, columnDefinition = "BLOB")
    private byte[] attributeBytes;

}
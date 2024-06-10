package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="especificos")
public class Especificos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "especificoid")
    private Long especificoid;
    private String nombreespecifico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEspecificoid() {
        return especificoid;
    }

    public void setEspecificoid(Long especificoid) {
        this.especificoid = especificoid;
    }

    public String getNombreespecifico() {
        return nombreespecifico;
    }

    public void setNombreespecifico(String nombreespecifico) {
        this.nombreespecifico = nombreespecifico;
    }
}
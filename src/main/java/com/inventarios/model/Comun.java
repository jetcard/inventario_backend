package com.inventarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="comun")
public class Comun implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Custodio custodio;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Tipo tipo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    private String descripcomun;
    //private String descripcortacomun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Custodio getResponsable() {
        return custodio;
    }

    public void setResponsable(Custodio custodio) {
        this.custodio = custodio;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Categoria getGrupo() {
        return categoria;
    }

    public void setGrupo(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcomun() {
        return descripcomun;
    }

    public void setDescripcomun(String descripcomun) {
        this.descripcomun = descripcomun;
    }

}

package com.macedo.swplanetapi.domain;

import jakarta.persistence.Entity;

@Entity
public class Planet {
    
    private Long id;
    private String name;
    private String climente;
    private String terrain;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getClimente() {
        return climente;
    }
    public void setClimente(String climente) {
        this.climente = climente;
    }
    public String getTerrain() {
        return terrain;
    }
    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    
}

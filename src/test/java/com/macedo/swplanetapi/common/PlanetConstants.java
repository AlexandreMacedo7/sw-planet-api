package com.macedo.swplanetapi.common;

import java.util.ArrayList;
import java.util.List;

import com.macedo.swplanetapi.domain.Planet;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("name","climate","terrain");
    public static final Planet INVALID_PLANET = new Planet("","","");

    public static final Planet TATOOINE = new Planet(1L, "Tatooine","arid","desert");
    public static final Planet ALDERAAN = new Planet(2L,"Alderaan","temperate","grassl");
    public static final Planet YAVINV = new Planet(3L,"Yavinv", "temperate, tropical","terrain");
    public static final List<Planet> PLANETS = new ArrayList<>(){
        {
            add(TATOOINE);
            add(ALDERAAN);
            add(YAVINV);
        }
    };
}

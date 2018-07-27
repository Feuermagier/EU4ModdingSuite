package firemage.moddingsuite.model;

import javafx.scene.paint.Color;

public enum TerrainType {

    GRASSLANDS("Grasland", new Color(86/255.0, 124/255.0, 27/255.0, 1), 100, false),
    HILLS("Hügel", new Color(0/255.0, 86/255.0, 6/255.0, 1), 112, false),
    DESERT_MOUNTAIN("Wüste (Bergig)", new Color(112/255.0, 74/255.0, 31/255.0, 1), 125, false),
    DESERT("Wüste", new Color(206/255.0, 169/255.0, 99/255.0, 1), 100, false),
    PLAINS("Steppe", new Color(200/255.0, 214/255.0, 107/255.0, 1), 100, false),
    MOUNTAIN("Berg", new Color(65/255.0, 42/255.0, 17/255.0, 1), 125, false),
    DESERT_MOUNTAIN_LOW("Wüste (Hügelig)", new Color(158/255.0, 130/255.0, 77/255.0, 1), 112, false),
    MARSH("Sumpf", new Color(75/255.0, 147/255.0, 174/255.0, 1), 100, false),
    OCEAN("Ozean", new Color(8/255.0, 31/255.0, 130/255.0, 1), 40, true),
    SNOW("Gipfel", new Color(1, 1, 1, 1), 150, false),
    INLAND_OCEAN_17("Meer", new Color(55/255.0, 90/255.0, 220/255.0, 1), 80, true),
    COASTAL_DESERT_18("Küstenwüste", new Color(203/255.0, 191/255.0, 103/255.0, 1), 96, false),
    COASTLINE("Küste", new Color(255/255.0, 247/255.0, 0/255.0, 1), 96, false);
    //not used - wrong colors! check in terrain.txt (bottom)
        /*TERRAIN_5("terrain_5 (=Grasland)", Color.valueOf("13,96,62")),
        TERRAIN_8("terrain_8 (=Hügel)", Color.valueOf("53,77,17")),
        TERRAIN_10("terrain_10 (=Ackerland)", Color.valueOf("155,155,155")),
        TERRAIN_11("terrain_11 (=Ackerland)", Color.valueOf("255,0,0")),
        //Wald nicht vorhanden?
        TERRAIN_12("terrain_12 (=Wald)", Color.valueOf("42,55,22")),
        TERRAIN_13("terrain_13 (=Wald)", Color.valueOf("213,144,199")),
        TERRAIN_14("terrain_14 (=Wald)", Color.valueOf("127,24,60"))*/;


    public String text;
    public Color color;
    public int height;
    public boolean isSea;

    TerrainType(String text, Color terrainColor, int height, boolean isSea) {
        this.text = text;
        this.color = terrainColor;
        this.height = height;
        this.isSea = isSea;
    }

    public static TerrainType findTerrainByColor(Color c) {
        for(TerrainType t : values()) {
            if(t.color.equals(c)) return t;
        }
        return null;
    }
}

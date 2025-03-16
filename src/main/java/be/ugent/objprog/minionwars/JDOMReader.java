package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.effects.EffectFactory;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.minions.MinionTypeImage;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.TileFactory;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JDOMReader {
    private String filename;
    private List<Minion> minionList;
    private List<Power> powers;
    private List<Tile> tiles;
    private List<Effect> effectList;
    private TileFactory tileFactory;
    private EffectFactory effectFactory;
    public JDOMReader(String filename) throws IOException {
        this.filename = filename;
        tiles = new ArrayList<>();
        tileFactory = new TileFactory();
        effectFactory = new EffectFactory();
        minionList = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document = saxBuilder.build(new File(Objects.requireNonNull(getClass().getResource(this.filename)).getFile()));
            // Get root element (configuration)
            Element root = document.getRootElement();

            // Process <minions>
            Element minionsElement = root.getChild("minions");
            if (minionsElement != null) {
                for (Element minionElement : minionsElement.getChildren()) {
                    String type = minionElement.getName();
                    String name = minionElement.getAttributeValue("name");
                    int cost = Integer.parseInt(minionElement.getAttributeValue("cost"));
                    int movement = Integer.parseInt(minionElement.getAttributeValue("movement"));
                    Integer[] range = Arrays.stream(minionElement.getAttributeValue("range").split(" ")).sequential().map(Integer::parseInt).toArray(Integer[]::new);
                    int attack = Integer.parseInt(minionElement.getAttributeValue("attack"));
                    int defence = Integer.parseInt(minionElement.getAttributeValue("defence"));
                    // Optional
                    String effectType = minionElement.getAttributeValue("effect");
                    String effectValueAttr = minionElement.getAttributeValue("effect-value");
                    int effectValue = effectValueAttr != null ? Integer.parseInt(effectValueAttr) : 0;
                    MinionEffect minionEffect;
                    if (effectType != null) {
                        minionEffect  = effectFactory.createEffect(effectType, effectValue);
                    } else {
                        minionEffect = null;
                    }
                    // Get the image for the type
                    MinionTypeImage minionImage = MinionTypeImage.valueOf(type.toUpperCase().replace("-","_"));
                    System.out.println(type.toUpperCase().replace("-","_"));
                    System.out.println(minionImage.getImagePath());
                    Image minionIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(minionImage.getImagePath())));
                    minionList.add(new Minion(type,name,cost,movement,range,attack,defence,minionEffect, minionIcon) );
                }
            }

            // Process <powers>
            Element powersElement = root.getChild("powers");
            if (powersElement != null) {
                for (Element powerElement : powersElement.getChildren()) {
                    // Process powers (TODO)
                }
            }

            // Process <field>
            Element fieldElement = root.getChild("field");
            if (fieldElement != null) {
                for (Element tileElement : fieldElement.getChildren()) {
                    String tileType = tileElement.getName();
                    int x = Integer.parseInt(tileElement.getAttributeValue("x"));
                    int y = Integer.parseInt(tileElement.getAttributeValue("y"));
                    // Optional: Handle "homebase" attribute
                    String homebaseAttr = tileElement.getAttributeValue("homebase");
                    int homebase = (homebaseAttr != null) ? Integer.parseInt(homebaseAttr) : 0;

                    // Create tile
                    Tile tile = tileFactory.createTile(tileType, x, y, homebase);
                    tiles.add(tile);
                }
            }

// Process <effects>
            Element effectsElement = root.getChild("effects");
            if (effectsElement != null) {
                for (Element effectElement : effectsElement.getChildren()) {
                    // Process effects (TODO)
                }
            }

        } catch (Exception e ) {
            throw new IOException("Config file not found: " + filename);
        }
    }


    public List<Minion> getMinions() {
        return minionList;
    }

    public List<Tile> getTiles() {
        return tiles;
    }


}

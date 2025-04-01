package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.effects.EffectFactory;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.minions.MinionTypeImage;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.powers.PowerFactory;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.TileFactory;
import javafx.scene.image.Image;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JDOMReader {
    private final String filename;
    private final List<Minion> minionList;
    private final List<Tile> tiles;
    private final List<MinionEffect> effectList;
    private final EffectFactory effectFactory;
    private final List<Power> powers;

    public JDOMReader(String filename) throws IOException {
        this.filename = filename;
        tiles = new ArrayList<>();
        TileFactory tileFactory = new TileFactory();
        effectFactory = new EffectFactory();
        PowerFactory powerFactory = new PowerFactory();
        minionList = new ArrayList<>();
        effectList = new ArrayList<>();
        powers = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document;

            File file = new File(filename);

            if (file.exists()) {
                document = saxBuilder.build(file);
            } else {
                System.out.println("Config file not found on disk: " + filename);
                System.out.println("Searching classpath instead");

                InputStream inputStream = getClass().getResourceAsStream(filename);
                if (inputStream == null) {
                    throw new FileNotFoundException("Config file not found in classpath: " + filename);
                }
                System.out.println("Loading config from classpath...");
                document = saxBuilder.build(inputStream);

            }

            // Get root element configuration>
            Element root = document.getRootElement();

            // Process <effects>
            Element effectsElement = root.getChild("effects");
            if (effectsElement != null) {
                for (Element effectElement : effectsElement.getChildren()) {
                    String effectType = effectElement.getName();
                    String effectName = effectElement.getAttributeValue("name");
                    int baseDuration = Integer.parseInt(effectElement.getAttributeValue("duration"));
                    String effectValueAttr = effectElement.getAttributeValue("value");
                    int effectValue = (effectValueAttr != null) ? Integer.parseInt(effectValueAttr) : 0;

                    MinionEffect effect = effectFactory.createEffect(effectType, effectName,baseDuration, effectValue);
                    effectList.add(effect);
                }
            }


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

                    // Optional effect
                    String effectType = minionElement.getAttributeValue("effect");
                    MinionEffect minionEffect = null;
                    if (effectType != null) {
                        String effectValueAttr = minionElement.getAttributeValue("effect-value");
                        minionEffect = effectList.stream()
                                .filter(e -> e.getClass().getSimpleName().equalsIgnoreCase(effectType + "Effect"))
                                .findFirst()
                                .map(e -> {
                                    int valueToUse = effectValueAttr != null
                                            ? Integer.parseInt(effectValueAttr)
                                            : e.getValue(); // use default value from the effect instance
                                    return effectFactory.createEffect(effectType, e.getName(), e.getDuration(), valueToUse);
                                })
                                .orElse(null);
                    }


                    // Get the image for the type
                    MinionTypeImage minionImage = MinionTypeImage.valueOf(type.toUpperCase().replace("-", "_"));
                    Image minionIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(minionImage.getImagePath())));
                    minionList.add(new Minion(type, name, cost, movement, range, attack, defence, minionEffect, minionIcon));
                }
            }

            // Process <powers>
            Element powersElement = root.getChild("powers");
            if (powersElement != null) {
                for (Element powerElement : powersElement.getChildren()) {
                    String powerType = powerElement.getName();
                    String name = powerElement.getAttributeValue("name");
                    int radius = Integer.parseInt(powerElement.getAttributeValue("radius"));
                    int value = Integer.parseInt(powerElement.getAttributeValue("value"));
                    String effectType = powerElement.getAttributeValue("effect");
                    MinionEffect minionEffect = null;
                    if (effectType != null) {
                        String effectValueAttr = powersElement.getAttributeValue("effect-value");
                        minionEffect = effectList.stream()
                                .filter(e -> e.getClass().getSimpleName().equalsIgnoreCase(effectType + "Effect"))
                                .findFirst()
                                .map(e -> {
                                    int valueToUse = effectValueAttr != null
                                            ? Integer.parseInt(effectValueAttr)
                                            : e.getValue(); // use default value from the effect instance
                                    return effectFactory.createEffect(effectType, e.getName(), e.getDuration(), valueToUse);
                                })
                                .orElse(null);
                    }

                    powers.add(powerFactory.createPower(powerType, name,radius, value, minionEffect));
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


        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());

        }
    }

    public List<MinionEffect> getEffectList() {
        return effectList;
    }

    public String getFilename() {
        return filename;
    }

    public List<Minion> getMinions() {
        return minionList;
    }

    public List<Power> getPowers() {
        return powers;
    }

    public List<Tile> getTiles() {
        return tiles;
    }


}

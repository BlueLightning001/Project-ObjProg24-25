package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.powers.Power;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.tiles.TileFactory;
import javafx.scene.effect.Effect;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JDOMReader {
    private static String FILENAME = "/be/ugent/objprog/minionwars/configs/game-big.xml";
    private List<Minion> minionList;
    private List<Power> powers;
    private List<Tile> tiles;
    private List<Effect> effectList;
    private TileFactory tileFactory;

    public JDOMReader() {
        tiles = new ArrayList<>();
        tileFactory = new TileFactory();
        // Step 3: Read and parse the XML file using SAXBuilder
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document = saxBuilder.build(new File(Objects.requireNonNull(getClass().getResource(FILENAME)).getFile()));
            // Get root element (configuration)
            Element root = document.getRootElement();

            // Process <minions>
            Element minionsElement = root.getChild("minions");
            if (minionsElement != null) {
                for (Element minionElement : minionsElement.getChildren()) {
                    // Process minions (TODO)
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
                    String tileType = tileElement.getName(); // Element name (e.g., "dirt", "forest")
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

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JDOMReader jdomReader = new JDOMReader();
        System.out.println(jdomReader.getTiles());

    }

    public List<Tile> getTiles() {
        return tiles;
    }


}

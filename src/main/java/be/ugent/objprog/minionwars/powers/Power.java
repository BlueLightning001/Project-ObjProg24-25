package be.ugent.objprog.minionwars.powers;

import be.ugent.objprog.minionwars.effects.EffectFactory;
import be.ugent.objprog.minionwars.effects.MinionEffect;
import be.ugent.objprog.minionwars.minions.Minion;
import be.ugent.objprog.minionwars.models.Player;
import be.ugent.objprog.minionwars.tiles.Tile;
import be.ugent.objprog.minionwars.views.HexTile;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public abstract class Power {
    protected final String name;
    protected final int radius;
    protected final int value;
    protected final MinionEffect effect;
    protected final Image offensiveImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/attack-D60000.png")));
    protected final Image healthImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/health-D60000.png")));
    protected Image image = null;
    protected final EffectFactory effectFactory = new EffectFactory();
    protected boolean offensive;

    public Power(String name,int radius, int value, MinionEffect effect) {
        this.name = name;
        this.radius = radius;
        this.value = value;
        this.effect = effect;
        this.offensive = false;
    }

    public void apply(HexTile center, Player caster) {
        List<Tile> affectedTiles = center.getTilesInRadius(radius);
        for (Tile tile : affectedTiles) {
            Minion minion = tile.getOccupant();
            if (minion != null) {

                String effectType = null;
                if (effect != null) {
                    effectType = effect.getClass().getSimpleName().toLowerCase().replace("effect", "");
                }
                if (offensive && !minion.getOwner().equals(caster)) {
                    minion.decreaseDefence(value);
                    if (effect != null) {
                        MinionEffect effectClone = effectFactory.createEffect(effectType,
                                effect.getName(),
                                effect.getDuration(), effect.getValue());
                        minion.addStatusAilment(effectClone);
                    }
                } else if (!offensive && minion.getOwner().equals(caster)) {
                    minion.heal(value);
                    if (effect != null) {
                        MinionEffect effectClone = effectFactory.createEffect(effectType,
                                effect.getName(),
                                effect.getDuration(), effect.getValue());
                        minion.addStatusAilment(effectClone);
                    }
                }

            }
        }
    }

    public String getDescription(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("be.ugent.objprog.minionwars.lang.messages", locale);
        return bundle.getString("power.description." + this.getClass().getSimpleName());
    }

    public MinionEffect getEffect() {
        return effect;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getRadius() {
        return radius;
    }

    public int getValue() {
        return value;
    }

    public Image getValueImage() {
        if (offensive) {
            return offensiveImage;
        } else {
            return healthImage;
        }
    }

    public boolean hasEffect() {
        return effect != null;
    }

}

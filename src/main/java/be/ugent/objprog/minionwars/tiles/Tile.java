package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.minions.Minion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Tile {
    private final int xCoord;
    private final int yCoord;
    private final SimpleIntegerProperty homebase;
    private final String imagePath;
    private final boolean traversable;
    private final boolean canAttack;
    private final boolean canBeAttacked;
    private final ObjectProperty<Minion> occupant = new SimpleObjectProperty<>(this, "occupant", null);
    private int traversalCost;


    public Tile(int x, int y, int homebase, String imagePath, boolean traversable,
                int traversalCost, boolean canAttack, boolean canBeAttacked) {
        this.xCoord = x;
        this.yCoord = y;
        this.homebase = new SimpleIntegerProperty(homebase);
        this.imagePath = imagePath;
        this.traversable = traversable;
        this.traversalCost = traversalCost;
        this.canAttack = canAttack;
        this.canBeAttacked = canBeAttacked;
    }

    public int getHomebase() {
        return homebase.get();
    }

    public void setHomebase(int homebase) {
        this.homebase.set(homebase);
    }

    public String getImagePath() {
        return imagePath;
    }

    public Minion getOccupant() {
        return occupant.get();
    }

    public void setOccupant(Minion occupant) {
        this.occupant.set(occupant);
    }

    public int getTraversalCost() {
        return traversalCost;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public SimpleIntegerProperty homebaseProperty() {
        return homebase;
    }

    public boolean isHomeBase() {
        return homebase.get() != 0;
    }

    public boolean isOccupied() {
        return occupant.get() != null;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public ObjectProperty<Minion> occupantProperty() {
        return occupant;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + xCoord + ", " + yCoord + ") ( Occupant: " + occupant.get() + ") ( homebase: " + homebase.get() + ")";
    }

}

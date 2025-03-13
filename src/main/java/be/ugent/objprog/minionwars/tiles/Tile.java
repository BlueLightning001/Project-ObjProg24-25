package be.ugent.objprog.minionwars.tiles;

import be.ugent.objprog.minionwars.minions.Minion;

public abstract class Tile {
    private final int xCoord;
    private final int yCoord;
    private final String imagePath;
    private final boolean traversable;
    private int traversalCost;
    private final boolean canAttack;
    private final boolean canBeAttacked;
    private Minion occupant = null ;


    public Tile(int x, int y, String imagePath,boolean traversable,int traversalCost , boolean canAttack, boolean canBeAttacked) {
        this.xCoord = x;
        this.yCoord = y;
        this.imagePath = imagePath;
        this.traversable = traversable;
        this.traversalCost = traversalCost;
        this.canAttack = canAttack;
        this.canBeAttacked = canBeAttacked;
    }

    public Minion getOccupant() {
        return occupant;
    }

    public int getXCoord() { return xCoord; }
    public int getYCoord() { return yCoord; }
    public String getImagePath() { return imagePath; }

    public void setOccupant(Minion occupant) {
        this.occupant = occupant;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + xCoord + ", " + yCoord + "Occupant: " + occupant  +")";
    }
}

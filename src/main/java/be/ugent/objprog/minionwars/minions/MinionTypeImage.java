package be.ugent.objprog.minionwars.minions;

public enum MinionTypeImage {
    MILITIA("/be/ugent/objprog/minionwars/images/minions/militia.png"),
    SPEAR("/be/ugent/objprog/minionwars/images/minions/spear.png"),
    SWORD("/be/ugent/objprog/minionwars/images/minions/sword.png"),
    AXE("/be/ugent/objprog/minionwars/images/minions/axe.png"),
    ARCHER("/be/ugent/objprog/minionwars/images/minions/archer.png"),
    SCOUT("/be/ugent/objprog/minionwars/images/minions/scout.png"),
    CAVALRY("/be/ugent/objprog/minionwars/images/minions/cavalry.png"),
    MOUNTED_ARCHER("/be/ugent/objprog/minionwars/images/minions/mounted-archer.png"),
    HEAVY_CAVALRY("/be/ugent/objprog/minionwars/images/minions/heavy-cavalry.png"),
    CATAPULT("/be/ugent/objprog/minionwars/images/minions/catapult.png"),
    TREBUCHET("/be/ugent/objprog/minionwars/images/minions/trebuchet.png");

    private final String imagePath;

    MinionTypeImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}

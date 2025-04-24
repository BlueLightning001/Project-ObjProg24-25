package be.ugent.objprog.minionwars.minions;

public enum MinionTypeImage {
    MILITIA("militia.png"),
    SPEAR("spear.png"),
    SWORD("sword.png"),
    AXE("axe.png"),
    ARCHER("archer.png"),
    SCOUT("scout.png"),
    CAVALRY("cavalry.png"),
    MOUNTED_ARCHER("mounted-archer.png"),
    HEAVY_CAVALRY("heavy-cavalry.png"),
    CATAPULT("catapult.png"),
    TREBUCHET("trebuchet.png");

    private final String fileName;

    MinionTypeImage(String fileName) {
        this.fileName = fileName;
    }

    public String getImagePath() {
        return getImagePath(false);
    }

    public String getImagePath(boolean despicable) {
        String folder = despicable ? "minions-despicable" : "minions";
        return "/be/ugent/objprog/minionwars/images/" + folder + "/" + fileName;
    }
}


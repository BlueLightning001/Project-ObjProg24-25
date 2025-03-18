package be.ugent.objprog.minionwars.views;

import be.ugent.objprog.minionwars.models.MinionModel;
import be.ugent.objprog.minionwars.models.PlayerModel;
import javafx.scene.layout.HBox;

public class Part2MenuContainer extends HBox {
    private MinionModel minionModel;
    private PlayerModel playerModel;
    private TileGroupPane tileGroupPane;
    public Part2MenuContainer(PlayerModel playerModel, MinionModel minionModel, TileGroupPane tileGroupPane) {
        this.playerModel = playerModel;
        this.minionModel = minionModel;
        this.tileGroupPane = tileGroupPane;

    }
}

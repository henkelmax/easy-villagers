package de.maxhenkel.easyvillagers.config;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;

@Version(version = 1)
@Translation(prefix = "easy_villagers.config")
public class ModConfig extends Config {

    public ModConfig() {
        super(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "config"));
    }

    @Override
    public int defaultPermLevel() {
        return 2;
    }

    public ServerSection server = new ServerSection();
    public ClientSection client = new ClientSection();

    public static class ServerSection extends ConfigSection {
        public ValidatedInt breedingTime = new ValidatedInt(1200, Integer.MAX_VALUE, 20);
        public ValidatedInt convertingTime = new ValidatedInt(6000, Integer.MAX_VALUE, 20);
        public ValidatedInt farmSpeed = new ValidatedInt(10, Integer.MAX_VALUE, 1);
        public ValidatedList<String> farmCropsBlacklist = ValidatedList.ofString("#easy_villagers:invalid_farmer_crop");
        public ValidatedInt golemSpawnTime = new ValidatedInt(4800, Integer.MAX_VALUE, 201);

        public ValidatedInt traderMinRestockTime = new ValidatedInt(1200, Integer.MAX_VALUE, 1);
        public ValidatedInt traderMaxRestockTime = new ValidatedInt(3600, Integer.MAX_VALUE, 2);

        public ValidatedInt autoTraderMinRestockTime = new ValidatedInt(1200, Integer.MAX_VALUE, 1);
        public ValidatedInt autoTraderMaxRestockTime = new ValidatedInt(3600, Integer.MAX_VALUE, 2);
        public ValidatedInt autoTraderCooldown = new ValidatedInt(20, Integer.MAX_VALUE, 1);

        public ValidatedBoolean villagerInventorySounds = new ValidatedBoolean(true);
        public ValidatedInt villagerSoundAmount = new ValidatedInt(20, Integer.MAX_VALUE, 1);

        public ValidatedInt incubatorSpeed = new ValidatedInt(2, 1024, 1);
        public ValidatedBoolean tradeCycling = new ValidatedBoolean(true);
        public ValidatedBoolean universalReputation = new ValidatedBoolean(true);
    }

    public static class ClientSection extends ConfigSection {
        public ValidatedBoolean enableRightClickPickup = new ValidatedBoolean(true);
        public ValidatedDouble villagerVolume = new ValidatedDouble(1.0, 1.0, 0.0);
        public CycleTradesButtonLocation cycleTradesButtonLocation = CycleTradesButtonLocation.TOP_LEFT;
        public ValidatedBoolean renderBlockContents = new ValidatedBoolean(true);
        public ValidatedInt blockRenderDistance = new ValidatedInt(32, 256, 1);
        public ValidatedBoolean showFacesInConfigPanel = new ValidatedBoolean(true);
        public ValidatedBoolean showFacesInAllConfigPanels = new ValidatedBoolean(false);
    }

    public enum CycleTradesButtonLocation {
        TOP_LEFT, TOP_RIGHT, NONE
    }
}
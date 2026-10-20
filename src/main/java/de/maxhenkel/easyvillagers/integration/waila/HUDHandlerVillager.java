package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.impl.ui.ItemStackElement;

public class HUDHandlerVillager implements IBlockComponentProvider {

    public static final HUDHandlerVillager INSTANCE = new HUDHandlerVillager();

    private static final Identifier UID = Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "villager");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof VillagerTileentity blockEntity) {
            EasyVillagerEntity villager = blockEntity.getVillagerEntity();
            if (villager != null) {
                iTooltip.add(villager.getAdvancedName());
            }
        }
    }

    @Override
    public @Nullable Element getIcon(BlockAccessor accessor, IPluginConfig config, Element currentIcon) {
        if (accessor.getBlockEntity() instanceof FakeWorldTileentity blockEntity) {
            return ItemStackElement.of(blockEntity.getItemStack());
        }
        return currentIcon;
    }

    @Override
    public Identifier getUid() {
        return UID;
    }

}
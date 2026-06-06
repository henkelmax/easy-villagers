package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.VillagerArmorContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class InventoryViewerTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    public InventoryViewerTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.INVENTORY_VIEWER, ModBlocks.INVENTORY_VIEWER.defaultBlockState(), pos, state);
    }

    @Override
    public void tickServer() {
        if (hasVillager()) {
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);
        }
    }

    @Nullable
    public Container getVillagerInventory() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v == null) {
            return null;
        }
        return new net.minecraft.world.SimpleContainer(v.getInventory().getItems().toArray(new net.minecraft.world.item.ItemStack[0]));
    }

    @Nullable
    public Container getVillagerArmorInventory() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v == null) {
            return null;
        }
        return new VillagerArmorContainer(v, this::setChanged);
    }

    

    @Override
    public void setChanged() {
        super.setChanged();
        saveVillagerEntity();
        if (level != null) {
            
        }
    }
}

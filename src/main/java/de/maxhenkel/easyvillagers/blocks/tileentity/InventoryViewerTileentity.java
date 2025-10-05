package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.VillagerArmorContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

import javax.annotation.Nullable;

public class InventoryViewerTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    public InventoryViewerTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.INVENTORY_VIEWER.get(), ModBlocks.INVENTORY_VIEWER.get().defaultBlockState(), pos, state);
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
        return new ItemListInventory(v.getInventory().getItems(), this::setChanged);
    }

    @Nullable
    public Container getVillagerArmorInventory() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v == null) {
            return null;
        }
        return new VillagerArmorContainer(v, this::setChanged);
    }

    public ResourceHandler<ItemResource> getItemHandler() {
        Container inv = getVillagerInventory();
        if (inv == null) {
            return EmptyResourceHandler.instance();
        }
        return VanillaContainerWrapper.of(inv);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        saveVillagerEntity();
        if (level != null) {
            level.invalidateCapabilities(worldPosition);
        }
    }
}

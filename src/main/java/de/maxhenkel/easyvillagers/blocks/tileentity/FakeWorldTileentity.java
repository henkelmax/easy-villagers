package de.maxhenkel.easyvillagers.blocks.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;

import javax.annotation.Nullable;

public class FakeWorldTileentity extends SyncableTileentity {

    protected boolean fakeWorld;
    protected BlockState defaultState;
    @Nullable
    protected ItemStack itemStack;

    public FakeWorldTileentity(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.defaultState = defaultState;
    }

    public void setFakeWorld(Level w) {
        level = w;
        fakeWorld = true;
    }

    public boolean isFakeWorld() {
        return fakeWorld;
    }

    @Override
    public BlockState getBlockState() {
        if (fakeWorld) {
            return defaultState;
        }
        return super.getBlockState();
    }

    // Cached until new data is loaded, since every new stack creates a new villager when rendered
    public ItemStack getItemStack() {
        if (itemStack == null) {
            itemStack = new ItemStack(getBlockState().getBlock());
            itemStack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(getType(), saveWithoutMetadata(level.registryAccess())));
        }
        return itemStack;
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        itemStack = null;
    }

}

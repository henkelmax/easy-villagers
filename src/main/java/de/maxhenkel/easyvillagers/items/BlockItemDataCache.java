package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockItemDataCache {

    private static final CachedMap<TypedEntityData<BlockEntityType<?>>, FakeWorldTileentity> CACHE = new CachedMap<>(10_000L);

    @Nullable
    public static <T extends FakeWorldTileentity> T get(Level level, ItemStack stack, Class<T> beClass) {
        FakeWorldTileentity fakeWorldTileentity = get(level, stack);
        if (!beClass.isInstance(fakeWorldTileentity)) {
            return null;
        }
        return beClass.cast(fakeWorldTileentity);
    }

    @Nullable
    public static FakeWorldTileentity get(Level level, ItemStack stack) {
        TypedEntityData<BlockEntityType<?>> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return null;
        }
        return CACHE.get(data, () -> load(level, stack, data));
    }

    private static FakeWorldTileentity load(Level level, ItemStack stack, TypedEntityData<BlockEntityType<?>> data) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            throw new IllegalArgumentException("Item is not a block item");
        }

        if (!(blockItem.getBlock() instanceof VillagerBlockBase villagerBlockBase)) {
            throw new IllegalArgumentException("Item is not a villager block");
        }

        BlockState blockState = villagerBlockBase.defaultBlockState();
        BlockEntity blockEntity = villagerBlockBase.newBlockEntity(BlockPos.ZERO, blockState);

        if (!(blockEntity instanceof FakeWorldTileentity fakeWorldTileentity)) {
            throw new IllegalArgumentException("Item is no fake world block entity");
        }

        fakeWorldTileentity.setFakeWorld(level);
        if (data != null) {
            if (!fakeWorldTileentity.getType().isValid(blockState)) {
                throw new IllegalArgumentException("Block entity type is not valid for block state");
            }
            data.loadInto(fakeWorldTileentity, level.registryAccess());
        }

        return fakeWorldTileentity;
    }

}

package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemTileEntityCache {

    private static final CachedMap<ItemStack, FakeWorldTileentity> CACHE = new CachedMap<>(10_000L, ItemUtils.ITEM_COMPARATOR);
    private static final Minecraft MC = Minecraft.getInstance();

    public static <T extends FakeWorldTileentity> T getTileEntity(ItemStack stack, Supplier<T> tileentitySupplier) {
        return (T) CACHE.get(stack, () -> {
            T te = tileentitySupplier.get();
            CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");
            te.setFakeWorld(MC.level);
            if (blockEntityTag != null) {
                te.load(blockEntityTag);
            }
            return te;
        });
    }

}

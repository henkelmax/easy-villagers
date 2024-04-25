package de.maxhenkel.easyvillagers.datacomponents;

import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class VillagerBlockEntityData {

    public static final StreamCodec<RegistryFriendlyByteBuf, VillagerBlockEntityData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public VillagerBlockEntityData decode(RegistryFriendlyByteBuf buf) {
            return new VillagerBlockEntityData(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, VillagerBlockEntityData be) {
            buf.writeNbt(be.nbt);
        }
    };

    @Nullable
    private FakeWorldTileentity cache;
    private final CompoundTag nbt;

    private VillagerBlockEntityData(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public CompoundTag copy() {
        return nbt.copy();
    }

    public static VillagerBlockEntityData of(CompoundTag nbt) {
        return new VillagerBlockEntityData(nbt.copy());
    }

    @Nullable
    public static VillagerBlockEntityData get(ItemStack stack) {
        return stack.get(ModItems.BLOCK_ENTITY_DATA_COMPONENT);
    }

    public <T extends FakeWorldTileentity> T getBlockEntity(HolderLookup.Provider provider, @Nullable Level level, Supplier<T> blockEntitySupplier) {
        if (cache == null) {
            cache = blockEntitySupplier.get();
            cache.setFakeWorld(level);
            cache.loadCustomOnly(nbt, provider);
        }
        if (level != null && !cache.isFakeWorld()) {
            cache.setFakeWorld(level);
        }
        return (T) cache;
    }

    public static <T extends FakeWorldTileentity> T getAndStoreBlockEntity(ItemStack stack, HolderLookup.Provider provider, @Nullable Level level, Supplier<T> blockEntitySupplier) {
        VillagerBlockEntityData data = get(stack);
        if (data == null) {
            CustomData beData = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
            data = new VillagerBlockEntityData(beData.copyTag());
            stack.set(ModItems.BLOCK_ENTITY_DATA_COMPONENT, data);
        }
        return data.getBlockEntity(provider, level, blockEntitySupplier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VillagerBlockEntityData be = (VillagerBlockEntityData) o;
        return Objects.equals(nbt, be.nbt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nbt);
    }

}

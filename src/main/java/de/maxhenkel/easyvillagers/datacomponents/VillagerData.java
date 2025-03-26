package de.maxhenkel.easyvillagers.datacomponents;

import com.mojang.serialization.Codec;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;

public class VillagerData {

    public static final Codec<VillagerData> CODEC = CompoundTag.CODEC.xmap(VillagerData::of, villagerData -> villagerData.nbt);
    public static final StreamCodec<RegistryFriendlyByteBuf, VillagerData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public VillagerData decode(RegistryFriendlyByteBuf buf) {
            return new VillagerData(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, VillagerData villager) {
            buf.writeNbt(villager.nbt);
        }
    };

    private WeakReference<EasyVillagerEntity> villager = new WeakReference<>(null);
    private final CompoundTag nbt;

    private VillagerData(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static VillagerData of(CompoundTag nbt) {
        return new VillagerData(nbt.copy());
    }

    public static VillagerData of(Villager villager) {
        CompoundTag nbt = new CompoundTag();
        villager.addAdditionalSaveData(nbt);
        return new VillagerData(nbt);
    }

    @Nullable
    public static VillagerData get(ItemStack stack) {
        if (!(stack.getItem() instanceof VillagerItem)) {
            throw new IllegalArgumentException("Tried to set villager data to non-villager item (%s)".formatted(stack.getHoverName().getString()));
        }
        convert(stack);
        return stack.get(ModItems.VILLAGER_DATA_COMPONENT);
    }

    public static VillagerData getOrCreate(ItemStack stack) {
        VillagerData villagerData = get(stack);
        if (villagerData == null) {
            villagerData = setEmptyVillagerTag(stack);
        }
        return villagerData;
    }

    public EasyVillagerEntity getCacheVillager(Level level) {
        EasyVillagerEntity easyVillager = villager.get();
        if (easyVillager == null) {
            easyVillager = createEasyVillager(level, null);
            villager = new WeakReference<>(easyVillager);
        }
        return easyVillager;
    }

    public EasyVillagerEntity createEasyVillager(Level level, @Nullable ItemStack stack) {
        EasyVillagerEntity v = new EasyVillagerEntity(EntityType.VILLAGER, level);
        v.readAdditionalSaveData(nbt);
        if (stack != null) {
            Component customName = stack.get(DataComponents.CUSTOM_NAME);
            if (customName != null) {
                v.setCustomName(customName);
            }
        }
        v.hurtTime = 0;
        v.yHeadRot = 0F;
        v.yHeadRotO = 0F;
        return v;
    }

    public static EasyVillagerEntity createEasyVillager(ItemStack stack, Level level) {
        VillagerData villagerData = getOrCreate(stack);
        return villagerData.createEasyVillager(level, stack);
    }

    public static void applyToItem(ItemStack stack, Villager villager) {
        if (stack.isEmpty()) {
            return;
        }
        stack.set(ModItems.VILLAGER_DATA_COMPONENT, VillagerData.of(villager));
        if (villager.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, villager.getCustomName());
        }
    }

    public static EasyVillagerEntity getCacheVillager(ItemStack stack, Level level) {
        return getOrCreate(stack).getCacheVillager(level);
    }

    public static void convertInventory(CompoundTag tag, NonNullList<ItemStack> stacks, HolderLookup.Provider provider) {
        Optional<ListTag> optionalItems = tag.getList("Items");

        if (optionalItems.isEmpty()) {
            return;
        }

        ListTag listTag = optionalItems.get();

        for (int i = 0; i < listTag.size(); i++) {
            Optional<CompoundTag> tagOptional = listTag.getCompound(i);
            if (tagOptional.isEmpty()) {
                continue;
            }
            CompoundTag itemTag = tagOptional.get();
            Optional<Byte> optionalByte = itemTag.getByte("Slot");
            if (optionalByte.isEmpty()) {
                continue;
            }
            int pos = optionalByte.get() & 255;
            if (pos < stacks.size()) {
                ItemStack convert = convert(provider, itemTag);
                stacks.set(pos, convert);
            }
        }
    }

    public static ItemStack convert(HolderLookup.Provider provider, CompoundTag itemCompound) {
        ItemStack stack = ItemStack.parse(provider, itemCompound).orElse(ItemStack.EMPTY);
        if (stack.isEmpty()) {
            return stack;
        }
        if (!(stack.getItem() instanceof VillagerItem)) {
            return stack;
        }
        if (stack.has(ModItems.VILLAGER_DATA_COMPONENT)) {
            return stack;
        }
        Optional<CompoundTag> tagOptional = itemCompound.getCompound("tag");
        if (tagOptional.isEmpty()) {
            return stack;
        }
        CompoundTag tag = tagOptional.get();
        Optional<CompoundTag> optionalVillager = tag.getCompound("villager");
        if (optionalVillager.isEmpty()) {
            return stack;
        }
        CompoundTag villagerTag = optionalVillager.get();
        VillagerData villagerData = VillagerData.of(villagerTag);
        stack.set(ModItems.VILLAGER_DATA_COMPONENT, villagerData);
        return stack;
    }

    public static void convert(ItemStack stack) {
        if (!(stack.getItem() instanceof VillagerItem)) {
            return;
        }
        if (stack.has(ModItems.VILLAGER_DATA_COMPONENT)) {
            return;
        }
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            setEmptyVillagerTag(stack);
            return;
        }
        CompoundTag customTag = customData.copyTag();
        Optional<CompoundTag> villagerTag = customTag.getCompound("villager");
        if (villagerTag.isEmpty()) {
            setEmptyVillagerTag(stack);
            return;
        }
        customTag.remove("villager");
        if (customTag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(customTag));
        }
        VillagerData villagerData = VillagerData.of(villagerTag.get());
        stack.set(ModItems.VILLAGER_DATA_COMPONENT, villagerData);
    }

    private static VillagerData setEmptyVillagerTag(ItemStack stack) {
        VillagerData villagerData = new VillagerData(new CompoundTag());
        stack.set(ModItems.VILLAGER_DATA_COMPONENT, villagerData);
        return villagerData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VillagerData villager1 = (VillagerData) o;
        return Objects.equals(nbt, villager1.nbt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nbt);
    }
}

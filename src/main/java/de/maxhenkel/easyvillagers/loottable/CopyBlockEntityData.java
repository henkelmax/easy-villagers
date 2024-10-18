package de.maxhenkel.easyvillagers.loottable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class CopyBlockEntityData extends LootItemConditionalFunction {

    public static final MapCodec<CopyBlockEntityData> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, CopyBlockEntityData::new));

    protected CopyBlockEntityData(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        BlockEntity blockEntity = context.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity == null) {
            return stack;
        }
        CompoundTag compoundtag = blockEntity.saveCustomAndMetadata(context.getLevel().registryAccess());
        BlockItem.setBlockEntityData(stack, blockEntity.getType(), compoundtag);
        stack.applyComponents(blockEntity.collectComponents());
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootTables.COPY_BLOCK_ENTITY.get();
    }

}

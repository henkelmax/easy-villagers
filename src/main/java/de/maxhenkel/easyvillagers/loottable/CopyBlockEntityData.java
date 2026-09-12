package de.maxhenkel.easyvillagers.loottable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.maxhenkel.corelib.codec.ValueInputOutputUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class CopyBlockEntityData extends LootItemConditionalFunction {

    public static final MapCodec<CopyBlockEntityData> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, CopyBlockEntityData::new));

    protected CopyBlockEntityData(Optional<Holder<LootItemCondition>> conditions) {
        super(conditions);
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        BlockEntity blockEntity = context.getOptional(LootContextParams.BLOCK_ENTITY);
        if (blockEntity == null) {
            return stack;
        }
        TagValueOutput valueOutput = ValueInputOutputUtils.createValueOutput(blockEntity, context.getLevel().registryAccess());
        blockEntity.saveWithFullMetadata(valueOutput);
        BlockItem.setBlockEntityData(stack, blockEntity.getType(), valueOutput);
        stack.applyComponents(blockEntity.collectComponents());
        return stack;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

}

package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.corelib.client.CustomRendererItem;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.render.VillagerItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

import static de.maxhenkel.easyvillagers.datacomponents.VillagerData.getCacheVillager;

public class VillagerItem extends CustomRendererItem {

    public VillagerItem() {
        super(new Item.Properties().stacksTo(1));

        DispenserBlock.registerBehavior(this, (source, stack) -> {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos blockpos = source.pos().relative(direction);
            Level world = source.level();
            Villager villager = VillagerData.getOrCreate(stack).createEasyVillager(world, stack);
            villager.absMoveTo(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, direction.toYRot(), 0F);
            world.addFreshEntity(villager);
            stack.shrink(1);
            return stack;
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemRenderer createItemRenderer() {
        return new VillagerItemRenderer();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            if (!blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos = blockpos.relative(direction);
            }

            Villager villager = VillagerData.getOrCreate(itemstack).createEasyVillager(world, itemstack);

            villager.setPos(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5);

            if (world.addFreshEntity(villager)) {
                itemstack.shrink(1);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getName(ItemStack stack) {
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return super.getName(stack);
        } else {
            EasyVillagerEntity villager = getCacheVillager(stack, world);
            if (!villager.hasCustomName() && villager.isBaby()) {
                return Component.translatable("item.easy_villagers.baby_villager");
            }
            return villager.getDisplayName();
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!(entity instanceof Player) || world.isClientSide) {
            return;
        }
        if (!Main.SERVER_CONFIG.villagerInventorySounds.get()) {
            return;
        }
        VillagerBlockBase.playRandomVillagerSound((Player) entity, SoundEvents.VILLAGER_AMBIENT);
    }

    public static ItemStack createBabyVillager() {
        ItemStack babyVillager = new ItemStack(ModItems.VILLAGER.get());
        CompoundTag compound = new CompoundTag();
        compound.putInt("Age", -24000);
        VillagerData data = VillagerData.of(compound);
        babyVillager.set(ModItems.VILLAGER_DATA_COMPONENT, data);
        return babyVillager;
    }
}

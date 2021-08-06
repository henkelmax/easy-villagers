package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.client.CustomRendererItem;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.render.VillagerItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VillagerItem extends CustomRendererItem {

    private final CachedMap<ItemStack, EasyVillagerEntity> cachedVillagers;

    public VillagerItem() {
        super(new Item.Properties().stacksTo(1));
        cachedVillagers = new CachedMap<>(10_000, ItemUtils.ITEM_COMPARATOR);

        DispenserBlock.registerBehavior(this, (source, stack) -> {
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos blockpos = source.getPos().relative(direction);
            Level world = source.getLevel();
            EasyVillagerEntity villager = getVillager(world, stack);
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

            EasyVillagerEntity villager = getVillager(world, itemstack);

            villager.setPos(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5);

            if (world.addFreshEntity(villager)) {
                itemstack.shrink(1);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getName(ItemStack stack) {
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return super.getName(stack);
        } else {
            EasyVillagerEntity villager = getVillagerFast(world, stack);
            if (!villager.hasCustomName() && villager.isBaby()) {
                return new TranslatableComponent("item.easy_villagers.baby_villager");
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

    public void setVillager(ItemStack stack, Villager villager) {
        CompoundTag compound = stack.getOrCreateTagElement("villager");
        villager.addAdditionalSaveData(compound);
        if (villager.hasCustomName()) {
            stack.setHoverName(villager.getCustomName());
        }
    }

    public EasyVillagerEntity getVillager(Level world, ItemStack stack) {
        CompoundTag compound = stack.getTagElement("villager");
        if (compound == null) {
            compound = new CompoundTag();
        }

        EasyVillagerEntity villager = new EasyVillagerEntity(EntityType.VILLAGER, world);
        villager.readAdditionalSaveData(compound);

        if (stack.hasCustomHoverName()) {
            villager.setCustomName(stack.getHoverName());
        }

        villager.hurtTime = 0;
        villager.yHeadRot = 0F;
        villager.yHeadRotO = 0F;
        return villager;
    }

    public EasyVillagerEntity getVillagerFast(Level world, ItemStack stack) {
        return cachedVillagers.get(stack, () -> getVillager(world, stack));
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemStack getBabyVillager() {
        ItemStack babyVillager = new ItemStack(ModItems.VILLAGER);
        EasyVillagerEntity villager = new EasyVillagerEntity(EntityType.VILLAGER, Minecraft.getInstance().level);
        villager.setAge(-24000);
        ModItems.VILLAGER.setVillager(babyVillager, villager);
        return babyVillager;
    }
}

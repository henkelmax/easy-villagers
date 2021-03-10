package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.render.VillagerItemRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VillagerItem extends Item {

    private final CachedMap<ItemStack, EasyVillagerEntity> cachedVillagers;

    public VillagerItem() {
        super(new Item.Properties().stacksTo(1).setISTER(() -> VillagerItemRenderer::new));
        cachedVillagers = new CachedMap<>(10_000, ItemUtils.ITEM_COMPARATOR);

        DispenserBlock.registerBehavior(this, (source, stack) -> {
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos blockpos = source.getPos().relative(direction);
            World world = source.getLevel();
            EasyVillagerEntity villager = getVillager(world, stack);
            villager.absMoveTo(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, direction.toYRot(), 0F);
            world.addFreshEntity(villager);
            stack.shrink(1);
            return stack;
        });
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
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

            return ActionResultType.CONSUME;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getName(ItemStack stack) {
        World world = Minecraft.getInstance().level;
        if (world == null) {
            return super.getName(stack);
        } else {
            EasyVillagerEntity villager = getVillagerFast(world, stack);
            if (!villager.hasCustomName() && villager.isBaby()) {
                return new TranslationTextComponent("item.easy_villagers.baby_villager");
            }
            return villager.getDisplayName();
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!(entity instanceof PlayerEntity) || world.isClientSide) {
            return;
        }
        if (!Main.SERVER_CONFIG.villagerInventorySounds.get()) {
            return;
        }
        VillagerBlockBase.playRandomVillagerSound((PlayerEntity) entity, SoundEvents.VILLAGER_AMBIENT);
    }

    public void setVillager(ItemStack stack, VillagerEntity villager) {
        CompoundNBT compound = stack.getOrCreateTagElement("villager");
        villager.addAdditionalSaveData(compound);
        if (villager.hasCustomName()) {
            stack.setHoverName(villager.getCustomName());
        }
    }

    public EasyVillagerEntity getVillager(World world, ItemStack stack) {
        CompoundNBT compound = stack.getTagElement("villager");
        if (compound == null) {
            compound = new CompoundNBT();
        }

        EasyVillagerEntity villager = new EasyVillagerEntity(EntityType.VILLAGER, world);
        villager.readAdditionalSaveData(compound);

        if (stack.hasCustomHoverName()) {
            villager.setCustomName(stack.getDisplayName());
        }

        villager.hurtTime = 0;
        villager.yHeadRot = 0F;
        villager.yHeadRotO = 0F;
        return villager;
    }

    public EasyVillagerEntity getVillagerFast(World world, ItemStack stack) {
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

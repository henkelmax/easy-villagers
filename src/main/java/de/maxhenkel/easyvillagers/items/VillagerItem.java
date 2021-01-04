package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.Main;
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
import net.minecraft.util.SoundCategory;
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

    private CachedMap<ItemStack, EasyVillagerEntity> cachedVillagers;

    public VillagerItem() {
        super(new Item.Properties().maxStackSize(1).setISTER(() -> VillagerItemRenderer::new));
        cachedVillagers = new CachedMap<>(10_000);

        DispenserBlock.registerDispenseBehavior(this, (source, stack) -> {
            Direction direction = source.getBlockState().get(DispenserBlock.FACING);
            BlockPos blockpos = source.getBlockPos().offset(direction);
            World world = source.getWorld();
            EasyVillagerEntity villager = getVillager(world, stack);
            villager.setPositionAndRotation(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, direction.getHorizontalAngle(), 0F);
            world.addEntity(villager);
            stack.shrink(1);
            return stack;
        });
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemstack = context.getItem();
            BlockPos blockpos = context.getPos();
            Direction direction = context.getFace();
            BlockState blockstate = world.getBlockState(blockpos);

            if (!blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos = blockpos.offset(direction);
            }

            EasyVillagerEntity villager = getVillager(world, itemstack);

            villager.setPosition(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5);

            if (world.addEntity(villager)) {
                itemstack.shrink(1);
            }

            return ActionResultType.CONSUME;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        World world = Minecraft.getInstance().world;
        if (world == null) {
            return super.getDisplayName(stack);
        } else {
            EasyVillagerEntity villager = getVillagerFast(world, stack);
            if (!villager.hasCustomName() && villager.isChild()) {
                return new TranslationTextComponent("item.easy_villagers.baby_villager");
            }
            return villager.getDisplayName();
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!(entity instanceof PlayerEntity) || world.isRemote) {
            return;
        }
        if (!Main.SERVER_CONFIG.villagerInventorySounds.get()) {
            return;
        }
        if (world.getGameTime() % 20 != 0) {
            return;
        }
        if (world.rand.nextInt(20) == 0) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            playerEntity.playSound(SoundEvents.ENTITY_VILLAGER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F);
        }
    }

    public void setVillager(ItemStack stack, VillagerEntity villager) {
        CompoundNBT compound = stack.getOrCreateChildTag("villager");
        villager.writeAdditional(compound);
        if (villager.hasCustomName()) {
            stack.setDisplayName(villager.getCustomName());
        }
    }

    public EasyVillagerEntity getVillager(World world, ItemStack stack) {
        CompoundNBT compound = stack.getChildTag("villager");
        if (compound == null) {
            compound = new CompoundNBT();
        }

        EasyVillagerEntity villager = new EasyVillagerEntity(EntityType.VILLAGER, world);
        villager.readAdditional(compound);

        if (stack.hasDisplayName()) {
            villager.setCustomName(stack.getDisplayName());
        }

        villager.hurtTime = 0;
        villager.rotationYawHead = 0F;
        villager.prevRotationYawHead = 0F;
        return villager;
    }

    public EasyVillagerEntity getVillagerFast(World world, ItemStack stack) {
        return cachedVillagers.get(stack, () -> getVillager(world, stack));
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemStack getBabyVillager() {
        ItemStack babyVillager = new ItemStack(ModItems.VILLAGER);
        EasyVillagerEntity villager = new EasyVillagerEntity(EntityType.VILLAGER, Minecraft.getInstance().world) {
            @Override
            public int getGrowingAge() {
                return -24000;
            }
        };
        ModItems.VILLAGER.setVillager(babyVillager, villager);
        return babyVillager;
    }

}

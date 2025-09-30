package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

public class VillagerItem extends Item {

    public VillagerItem(Item.Properties properties) {
        super(properties.stacksTo(1));

        DispenserBlock.registerBehavior(this, (source, stack) -> {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos blockpos = source.pos().relative(direction);
            Level world = source.level();
            Villager villager = VillagerData.getOrCreate(stack).createEasyVillager(world, stack);
            villager.snapTo(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, direction.toYRot(), 0F);
            world.addFreshEntity(villager);
            stack.shrink(1);
            return stack;
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide()) {
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
    public Component getName(ItemStack stack) {
        if (FMLEnvironment.getDist().isClient()) {
            Component clientName = ClientVillagerItemUtils.getClientName(stack);
            if (clientName != null) {
                return clientName;
            }
        }
        return super.getName(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        super.inventoryTick(stack, level, entity, equipmentSlot);
        if (!(entity instanceof Player player)) {
            return;
        }
        if (!EasyVillagersMod.SERVER_CONFIG.villagerInventorySounds.get()) {
            return;
        }
        VillagerBlockBase.playRandomVillagerSound(player, SoundEvents.VILLAGER_AMBIENT);
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

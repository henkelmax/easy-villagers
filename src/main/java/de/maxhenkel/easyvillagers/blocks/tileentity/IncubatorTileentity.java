package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.gui.VillagerIncubateSlot;
import de.maxhenkel.easyvillagers.inventory.InputOnlyResourceHandler;
import de.maxhenkel.easyvillagers.inventory.ListAccessItemStacksResourceHandler;
import de.maxhenkel.easyvillagers.inventory.OutputOnlyResourceHandler;
import de.maxhenkel.easyvillagers.inventory.ValidateResourceHandler;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class IncubatorTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    protected ValidateResourceHandler inputInventory;
    protected ListAccessItemStacksResourceHandler outputInventory;

    protected CombinedResourceHandler<ItemResource> itemHandler;

    public IncubatorTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.INCUBATOR.get(), ModBlocks.INCUBATOR.get().defaultBlockState(), pos, state);
        inputInventory = new ValidateResourceHandler(4, VillagerIncubateSlot::isValid);
        outputInventory = new ListAccessItemStacksResourceHandler(4);
        itemHandler = new CombinedResourceHandler<>(new InputOnlyResourceHandler(inputInventory), new OutputOnlyResourceHandler(outputInventory));
    }

    @Override
    public void tickServer() {
        if (!hasVillager()) {
            try (Transaction transaction = Transaction.open(null)) {
                for (int i = 0; i < inputInventory.size(); i++) {
                    ItemResource resource = inputInventory.getResource(i);
                    if (resource.getItem() instanceof VillagerItem) {
                        inputInventory.extract(resource, 1, transaction);
                        setVillager(resource.toStack());
                        transaction.commit();
                        sync();
                        break;
                    }
                }
            }
        }
        if (hasVillager()) {
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);

            Villager villagerEntity = getVillagerEntity();

            if (villagerEntity.isBaby()) {
                if (advanceAge(Math.min(EasyVillagersMod.SERVER_CONFIG.incubatorSpeed.get(), Math.abs(villagerEntity.getAge())))) {
                    sync();
                }
            } else {
                advanceAge(1);
            }

            if (villagerEntity.getAge() > 20) {
                ItemStack villagerItem = getVillager();
                try (Transaction transaction = Transaction.open(null)) {
                    if (outputInventory.insert(ItemResource.of(villagerItem), 1, transaction) > 0) {
                        removeVillager();
                        transaction.commit();
                        sync();
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ItemUtils.saveInventory(valueOutput.child("InputInventory"), "Items", inputInventory.getRaw());
        ItemUtils.saveInventory(valueOutput.child("OutputInventory"), "Items", outputInventory.getRaw());
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        ItemUtils.readInventory(valueInput.childOrEmpty("InputInventory"), "Items", inputInventory.getRaw());
        ItemUtils.readInventory(valueInput.childOrEmpty("OutputInventory"), "Items", outputInventory.getRaw());

        super.loadAdditional(valueInput);
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory.getRaw(), this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory.getRaw(), this::setChanged);
    }

    public ResourceHandler<ItemResource> getItemHandler() {
        return itemHandler;
    }

}

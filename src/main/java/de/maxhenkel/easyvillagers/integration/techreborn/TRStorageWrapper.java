package de.maxhenkel.easyvillagers.integration.techreborn;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TRStorageWrapper implements Storage<ItemVariant> {

    private final Container inputInventory;
    private final Container outputInventory;
    private final Storage<ItemVariant> inputStorage;
    private final Storage<ItemVariant> outputStorage;
    private final TRSlotConfiguration config;
    private final Direction side;
    private final int inputSlotCount;
    private final int outputSlotCount;

    public static boolean hasConfiguredSide(de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration config, int totalSlots, net.minecraft.core.Direction side) {
        if (config == null) return false;
        for (int i = 0; i < totalSlots; i++) {
            if (config.getConfig(i, side) != de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration.ExtractConfig.NONE) {
                return true;
            }
        }
        return false;
    }

    public TRStorageWrapper(Container inputInventory, Container outputInventory, TRSlotConfiguration config, Direction side) {
        this.inputInventory = inputInventory;
        this.outputInventory = outputInventory;
        this.inputStorage = inputInventory != null ? ContainerStorage.of(inputInventory, side) : null;
        this.outputStorage = outputInventory != null ? ContainerStorage.of(outputInventory, side) : null;
        this.config = config;
        this.side = side;
        this.inputSlotCount = inputInventory != null ? inputInventory.getContainerSize() : 0;
        this.outputSlotCount = outputInventory != null ? outputInventory.getContainerSize() : 0;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        if (inputInventory == null || maxAmount == 0) return 0;
        
        long inserted = 0;
        ContainerStorage containerStorage = ContainerStorage.of(inputInventory, side);
        List<SingleSlotStorage<ItemVariant>> slots = containerStorage.getSlots();
        
        for (int i = 0; i < inputSlotCount && i < slots.size(); i++) {
            if (config.getConfig(i, side) == TRSlotConfiguration.ExtractConfig.INPUT) {
                inserted += slots.get(i).insert(resource, maxAmount - inserted, transaction);
                if (inserted >= maxAmount) {
                    break;
                }
            }
        }
        
        return inserted;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        if (outputInventory == null || maxAmount == 0) return 0;
        
        long extracted = 0;
        ContainerStorage containerStorage = ContainerStorage.of(outputInventory, side);
        List<SingleSlotStorage<ItemVariant>> slots = containerStorage.getSlots();
        
        for (int i = 0; i < outputSlotCount && i < slots.size(); i++) {
            if (config.getConfig(inputSlotCount + i, side) == TRSlotConfiguration.ExtractConfig.OUTPUT) {
                extracted += slots.get(i).extract(resource, maxAmount - extracted, transaction);
                if (extracted >= maxAmount) {
                    break;
                }
            }
        }
        
        return extracted;
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        List<StorageView<ItemVariant>> views = new ArrayList<>();
        
        if (inputInventory != null) {
            ContainerStorage containerStorage = ContainerStorage.of(inputInventory, side);
            List<SingleSlotStorage<ItemVariant>> slots = containerStorage.getSlots();
            for (int i = 0; i < inputSlotCount && i < slots.size(); i++) {
                if (config.getConfig(i, side) == TRSlotConfiguration.ExtractConfig.INPUT) {
                    StorageView<ItemVariant> originalView = slots.get(i);
                    views.add(new StorageView<ItemVariant>() {
                        @Override
                        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
                            return 0; // Prevent extraction from INPUT slots
                        }

                        @Override
                        public boolean isResourceBlank() {
                            return originalView.isResourceBlank();
                        }

                        @Override
                        public ItemVariant getResource() {
                            return originalView.getResource();
                        }

                        @Override
                        public long getAmount() {
                            return originalView.getAmount();
                        }

                        @Override
                        public long getCapacity() {
                            return originalView.getCapacity();
                        }
                    });
                }
            }
        }
        
        if (outputInventory != null) {
            ContainerStorage containerStorage = ContainerStorage.of(outputInventory, side);
            List<SingleSlotStorage<ItemVariant>> slots = containerStorage.getSlots();
            for (int i = 0; i < outputSlotCount && i < slots.size(); i++) {
                if (config.getConfig(inputSlotCount + i, side) == TRSlotConfiguration.ExtractConfig.OUTPUT) {
                    views.add(slots.get(i));
                }
            }
        }
        
        return views.iterator();
    }
}
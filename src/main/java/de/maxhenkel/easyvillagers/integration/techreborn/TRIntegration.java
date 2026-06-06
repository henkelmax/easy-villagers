package de.maxhenkel.easyvillagers.integration.techreborn;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TRIntegration {

    public static Object createSlotConfiguration(BlockEntity blockEntity) {
        try {
            // Uses reflection to avoid hard dependency at compile time
            Class<?> clazz = Class.forName("reborncore.common.blockentity.SlotConfiguration");
            // SlotConfiguration requires a RebornInventory, but if we don't have one, we can instantiate it with empty data
            // However, it's easier to just create an empty instance or not use it if we can't
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}

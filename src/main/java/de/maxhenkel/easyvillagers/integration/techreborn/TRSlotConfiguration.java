package de.maxhenkel.easyvillagers.integration.techreborn;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class TRSlotConfiguration {

    public enum ExtractConfig {
        NONE, INPUT, OUTPUT
    }

    // Map of slot index -> Map of Direction -> ExtractConfig
    private final Map<Integer, Map<Direction, ExtractConfig>> config = new HashMap<>();

    public TRSlotConfiguration() {
    }

    public ExtractConfig getConfig(int slot, Direction dir) {
        if (!config.containsKey(slot)) {
            return ExtractConfig.NONE;
        }
        return config.get(slot).getOrDefault(dir, ExtractConfig.NONE);
    }

    public void setConfig(int slot, Direction dir, ExtractConfig extractConfig) {
        config.computeIfAbsent(slot, k -> new HashMap<>()).put(dir, extractConfig);
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<Integer, Map<Direction, ExtractConfig>> entry : config.entrySet()) {
            CompoundTag slotTag = new CompoundTag();
            for (Map.Entry<Direction, ExtractConfig> dirEntry : entry.getValue().entrySet()) {
                slotTag.putInt(dirEntry.getKey().getName(), dirEntry.getValue().ordinal());
            }
            tag.put(String.valueOf(entry.getKey()), slotTag);
        }
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        config.clear();
        for (String slotKey : tag.keySet()) {
            try {
                int slot = Integer.parseInt(slotKey);
                CompoundTag slotTag = tag.getCompound(slotKey).orElse(new CompoundTag());
                for (Direction dir : Direction.values()) {
                    if (slotTag.contains(dir.getName())) {
                        setConfig(slot, dir, ExtractConfig.values()[slotTag.getInt(dir.getName()).orElse(0)]);
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
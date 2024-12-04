package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class InventoryViewerContainer extends VillagerContainerBase {

    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    public static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET
    };

    protected InventoryViewerTileentity inventoryViewer;
    protected EasyVillagerEntity villager;

    public InventoryViewerContainer(int id, Container playerInventory, InventoryViewerTileentity inventoryViewer, ContainerLevelAccess access) {
        super(Containers.INVENTORY_VIEWER_CONTAINER.get(), id, playerInventory, null, access);
        this.inventoryViewer = inventoryViewer;
        villager = inventoryViewer.getVillagerEntity();

        Container villagerInventory = inventoryViewer.getVillagerInventory();
        Container armorInventory = inventoryViewer.getVillagerArmorInventory();

        for (int i = 0; i < villagerInventory.getContainerSize() && i < 4; i++) {
            addSlot(new VillagerInventorySlot(villagerInventory, i, 52 + i * 18, 20, inventoryViewer));
        }
        for (int i = 4; i < villagerInventory.getContainerSize() && i < 8; i++) {
            addSlot(new VillagerInventorySlot(villagerInventory, i, 52 + (i - 4) * 18, 38, inventoryViewer));
        }

        for (int i = 0; i < 4; i++) {
            EquipmentSlot equipmentslot = SLOT_IDS[i];
            addSlot(new Slot(armorInventory, 3 - i, 52 + i * 18, 69) { //51

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return villager.getEquipmentSlotForItem(stack) == equipmentslot;
                }

                @Override
                public boolean mayPickup(Player player) {
                    ItemStack itemstack = getItem();
                    return !itemstack.isEmpty() && !player.isCreative() && EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) ? false : super.mayPickup(player);
                }

                @Override
                public ResourceLocation getNoItemIcon() {
                    return TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()];
                }
            });
        }

        addPlayerInventorySlots();
    }

    public InventoryViewerContainer(int id, Inventory playerInventory, BlockPos pos) {
        this(id, playerInventory, get(pos));
    }

    public InventoryViewerContainer(int id, Inventory playerInventory, InventoryViewerTileentity te) {
        this(id, playerInventory, te, ContainerLevelAccess.create(te.getLevel(), te.getBlockPos()));
    }

    @OnlyIn(Dist.CLIENT)
    private static InventoryViewerTileentity get(BlockPos pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            throw new IllegalStateException("No client level present");
        }
        if (!(level.getBlockEntity(pos) instanceof InventoryViewerTileentity iv)) {
            throw new IllegalStateException("Could not find block entity");
        }
        return iv;
    }

    @Override
    public Block getBlock() {
        return ModBlocks.INVENTORY_VIEWER.get();
    }

    @Override
    public int getInvOffset() {
        return -2 + 18;
    }

    @Override
    public int getInventorySize() {
        return 12;
    }

    @Override
    public boolean stillValid(Player player) {
        return super.stillValid(player) && villager == inventoryViewer.getVillagerEntity();
    }
}

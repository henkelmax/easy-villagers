package de.maxhenkel.easyvillagers.integration.techreborn;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.SlotConfigGui;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.slot.BaseSlot;
import reborncore.common.util.RebornInventory;

public class TRDummyIntegration {

    public static int lastMouseX;
    public static int lastMouseY;

    public static Object createSlotConfigGui(de.maxhenkel.easyvillagers.gui.ScreenBase<?> evScreen, net.minecraft.world.level.block.entity.BlockEntityType<?> type, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, Object trConfigObj) {
        MachineBaseBlockEntity dummyMachine = new DummyMachineBlockEntity(type, pos, state, trConfigObj);
        DummyBuiltScreenHandler dummyContainer = new DummyBuiltScreenHandler(evScreen, dummyMachine);
        GuiBase<?> dummyGui = new DummyGuiBase(dummyContainer, evScreen);
        return new SlotConfigGui(dummyGui);
    }

        public static void open(Object gui) {
        ((SlotConfigGui) gui).open();
    }

    public static void draw(Object gui, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int leftPos, int topPos) {
        reborncore.client.gui.config.SlotConfigGui slotGui = (reborncore.client.gui.config.SlotConfigGui) gui;
        int tabX = leftPos - 24;
        int tabY = topPos + 6;
        net.minecraft.world.item.ItemStack tabStack = net.minecraft.world.item.ItemStack.EMPTY; try { tabStack = slotGui.stack(); } catch (Exception e) { tabStack = new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.HOPPER); }
        slotGui.gui().builder.drawSlotTab(guiGraphics, slotGui.gui(), tabX, tabY, tabStack);
        
        if (mouseX >= tabX && mouseX < tabX + 24 && mouseY >= tabY && mouseY < tabY + 24) {
            guiGraphics.setComponentTooltipForNextFrame(net.minecraft.client.Minecraft.getInstance().font, java.util.Collections.singletonList(net.minecraft.network.chat.Component.translatable(slotGui.name())), mouseX, mouseY);
        }

        DummyGuiBase dummyGui = (DummyGuiBase) slotGui.gui();
        if (dummyGui.dummySelectedTab == slotGui) {
            guiGraphics.pose().translate(leftPos, topPos);
            slotGui.draw(guiGraphics, mouseX, mouseY);
            guiGraphics.pose().translate(-leftPos, -topPos);
        }
    }

    public static boolean click(Object gui, double mouseX, double mouseY, int button, int leftPos, int topPos) {
        reborncore.client.gui.config.SlotConfigGui slotGui = (reborncore.client.gui.config.SlotConfigGui) gui;
        int tabX = leftPos - 24;
        int tabY = topPos + 6;
        
        DummyGuiBase dummyGui = (DummyGuiBase) slotGui.gui();

        if (mouseX >= tabX && mouseX < tabX + 24 && mouseY >= tabY && mouseY < tabY + 24) {
            if (dummyGui.dummySelectedTab == slotGui) {
                dummyGui.dummySelectedTab = null;
            } else {
                dummyGui.dummySelectedTab = slotGui;
                slotGui.open();
            }
            return true;
        }
        if (dummyGui.dummySelectedTab == slotGui) {
            boolean clicked = slotGui.click(mouseX, mouseY, button);
            if (isMouseOverPopup(gui, mouseX, mouseY)) {
                return true;
            }
            return clicked;
        }
        return false;
    }

    public static boolean isMouseOverPopup(Object gui, double mouseX, double mouseY) {
        reborncore.client.gui.config.SlotConfigGui slotGui = (reborncore.client.gui.config.SlotConfigGui) gui;
        DummyGuiBase dummyGui = (DummyGuiBase) slotGui.gui();
        if (dummyGui.dummySelectedTab != slotGui || slotGui.getSelectedSlot() == null) {
            return false;
        }
        int popupX = slotGui.getSelectedSlot().getX() - 8 + dummyGui.getGuiLeft() + dummyGui.getGuiLeft();
        int popupY = slotGui.getSelectedSlot().getY() - 7 + dummyGui.getGuiTop() + dummyGui.getGuiTop();
        int width = 84;
        int height = 79;
        return mouseX >= popupX && mouseX < popupX + width && mouseY >= popupY && mouseY < popupY + height;
    }

    public static void mouseReleased(Object gui, double mouseX, double mouseY, int button) {
        reborncore.client.gui.config.SlotConfigGui slotGui = (reborncore.client.gui.config.SlotConfigGui) gui;
        DummyGuiBase dummyGui = (DummyGuiBase) slotGui.gui();
        if (dummyGui.dummySelectedTab == slotGui) {
            slotGui.mouseReleased(mouseX, mouseY, button);
        }
    }

        public static class DummyGuiBase extends GuiBase<DummyBuiltScreenHandler> {
        private final de.maxhenkel.easyvillagers.gui.ScreenBase<?> evScreen;
        public reborncore.client.gui.config.GuiTab dummySelectedTab;

        public DummyGuiBase(DummyBuiltScreenHandler container, de.maxhenkel.easyvillagers.gui.ScreenBase<?> evScreen) {
            super(net.minecraft.client.Minecraft.getInstance().player, container.getBlockEntity(), container);
            this.evScreen = evScreen;
        }

        @Override
        public reborncore.client.gui.config.GuiTab getSelectedTab() {
            return dummySelectedTab;
        }

        @Override
        public void closeSelectedTab() {
            if (dummySelectedTab != null) {
                dummySelectedTab.close();
            }
            dummySelectedTab = null;
        }

        @Override
        public int getGuiLeft() {
            return evScreen.getLeftPos();
        }

                @Override
        public int getGuiTop() {
            return evScreen.getTopPos();
        }

        @Override
        public boolean isPointInRect(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
            return pointX >= rectX + getGuiLeft() && pointX < rectX + getGuiLeft() + rectWidth && pointY >= rectY + getGuiTop() && pointY < rectY + getGuiTop() + rectHeight;
        }

        @Override
        public MachineBaseBlockEntity getMachine() {
            return (MachineBaseBlockEntity) be;
        }

        @Override
        protected void drawTitle(net.minecraft.client.gui.GuiGraphicsExtractor drawContext) {
            drawCentredText(drawContext, evScreen.getTitle(), 6, 4210752, Layer.FOREGROUND);
        }

        @Override
        public boolean isConfigEnabled() {
            return true;
        }
    }

    public static class DummyBuiltScreenHandler extends BuiltScreenHandler {
        @SuppressWarnings("this-escape")
        public DummyBuiltScreenHandler(de.maxhenkel.easyvillagers.gui.ScreenBase<?> evScreen, MachineBaseBlockEntity machine) {
            super(0, "Dummy", p -> true, java.util.Collections.emptyList(), java.util.Collections.emptyList(), machine);
            int mappedIndex = 0;
            for (net.minecraft.world.inventory.Slot slot : evScreen.getMenu().slots) {
                if (slot.container instanceof net.minecraft.world.entity.player.Inventory) continue;
                if (evScreen.getMenu() instanceof de.maxhenkel.easyvillagers.gui.AutoTraderContainer autoTrader) {
                    if (slot.container == autoTrader.getTrader().getTradeGuiInv()) continue;
                }
                // Detect output-only slots: LockedSlot with canPut=false won't allow placing items
                boolean isOutputOnly = !slot.mayPlace(new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.STONE));
                reborncore.common.screen.slot.BaseSlot baseSlot;
                if (isOutputOnly) {
                    baseSlot = new OutputBaseSlot(machine, mappedIndex, slot.x, slot.y);
                } else {
                    baseSlot = new reborncore.common.screen.slot.BaseSlot(machine, mappedIndex, slot.x, slot.y);
                }
                this.addSlot(baseSlot);
                mappedIndex++;
            }
        }

        @Override
        public boolean canTakeItemForPickAll(net.minecraft.world.item.ItemStack stack, Slot slot) {
            return false;
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }

    // Output-only BaseSlot: canWorldBlockInsert() returns false, so Tech Reborn's
    // SlotConfigPopupElement restricts it to OUTPUT/NONE only (no INPUT option).
    public static class OutputBaseSlot extends reborncore.common.screen.slot.BaseSlot {
        public OutputBaseSlot(net.minecraft.world.Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean canWorldBlockInsert() {
            return false;
        }
    }

        public static boolean isTabOpen(Object gui) {
        reborncore.client.gui.config.SlotConfigGui slotGui = (reborncore.client.gui.config.SlotConfigGui) gui;
        DummyGuiBase dummyGui = (DummyGuiBase) slotGui.gui();
        return dummyGui.dummySelectedTab == slotGui;
    }

    public static class DummyMachineBlockEntity extends MachineBaseBlockEntity implements reborncore.api.blockentity.InventoryProvider {
        private final reborncore.common.util.RebornInventory<?> dummyInv; private final reborncore.common.blockentity.SlotConfiguration dummyConfig; @Override public reborncore.common.util.RebornInventory<?> getInventory() { return dummyInv; }
        
        @SuppressWarnings("this-escape")
                public DummyMachineBlockEntity(net.minecraft.world.level.block.entity.BlockEntityType<?> type, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, Object trConfigObj) {
            super(type, pos, state); 
            this.dummyInv = new reborncore.common.util.RebornInventory<>(10, "Dummy", 64, this);
            this.dummyConfig = new reborncore.common.blockentity.SlotConfiguration(this.dummyInv);

            if (trConfigObj instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) {
                for (int slotId = 0; slotId < 10; slotId++) {
                    reborncore.common.blockentity.SlotConfiguration.SlotConfigHolder holder = this.dummyConfig.getSlotDetails(slotId);
                    for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
                        de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration.ExtractConfig ec = trConfig.getConfig(slotId, dir);
                        reborncore.common.blockentity.SlotConfiguration.ExtractConfig trEc = reborncore.common.blockentity.SlotConfiguration.ExtractConfig.values()[ec.ordinal()];
                        holder.updateSlotConfig(new reborncore.common.blockentity.SlotConfiguration.SlotConfig(dir, new reborncore.common.blockentity.SlotConfiguration.SlotIO(trEc), slotId));
                    }
                }
            }
        }

        @Override
        public net.minecraft.core.Direction getFacingEnum() {
            net.minecraft.world.level.block.state.BlockState blockState = getBlockState();
            if (blockState != null && blockState.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING)) {
                return blockState.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING);
            }
            return net.minecraft.core.Direction.NORTH;
        }

        @Override
        public net.minecraft.core.Direction getFacing() {
            return getFacingEnum();
        }

        @Override
        public net.minecraft.world.level.block.Block getBlockType() {
            return getBlockState() != null ? getBlockState().getBlock() : net.minecraft.world.level.block.Blocks.AIR;
        }

        @Override
        public reborncore.common.blockentity.SlotConfiguration getSlotConfiguration() {
            return dummyConfig;
        }

        @Override
        public boolean hasSlotConfig() {
            return true;
        }

        @Override
        public int getContainerSize() { return 10; }
        @Override
        public boolean isEmpty() { return true; }
        @Override
        public net.minecraft.world.item.ItemStack getItem(int slot) { return net.minecraft.world.item.ItemStack.EMPTY; }
        @Override
        public net.minecraft.world.item.ItemStack removeItem(int slot, int amount) { return net.minecraft.world.item.ItemStack.EMPTY; }
        @Override
        public net.minecraft.world.item.ItemStack removeItemNoUpdate(int slot) { return net.minecraft.world.item.ItemStack.EMPTY; }
        @Override
        public void setItem(int slot, net.minecraft.world.item.ItemStack stack) {}
        @Override
        public boolean stillValid(Player player) { return true; }
        @Override
        public void clearContent() {}
    }


}

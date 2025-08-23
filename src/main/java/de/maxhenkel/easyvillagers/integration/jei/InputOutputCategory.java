package de.maxhenkel.easyvillagers.integration.jei;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class InputOutputCategory implements IRecipeCategory<ItemStack> {

    protected static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "textures/gui/container/jei_input_output.png");

    protected final IGuiHelper helper;
    protected final IDrawableStatic background;

    public InputOutputCategory(IGuiHelper helper) {
        this.helper = helper;
        background = helper.createDrawable(BACKGROUND, 0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(ItemStack recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public int getWidth() {
        return 72;
    }

    @Override
    public int getHeight() {
        return 49;
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon());
    }

    public abstract ItemStack icon();

    @Override
    public abstract void setRecipe(IRecipeLayoutBuilder builder, ItemStack recipe, IFocusGroup focuses);

    @Override
    public abstract Component getTitle();

    @Override
    public abstract IRecipeType<ItemStack> getRecipeType();

}

package de.maxhenkel.easyvillagers.integration.jei.converter;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.items.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ConverterCategory implements IRecipeCategory<ItemStack> {

    private IGuiHelper helper;

    public ConverterCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(Main.MODID, "textures/gui/container/jei_input_output.png"), 0, 0, 72, 49);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.CONVERTER));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemStack recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredient(VanillaTypes.ITEM, recipe);
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addIngredient(VanillaTypes.ITEM, new ItemStack(Items.GOLDEN_APPLE));
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 1).addIngredient(VanillaTypes.ITEM, new ItemStack(ModItems.VILLAGER));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 32).addIngredient(VanillaTypes.ITEM, new ItemStack(ModItems.VILLAGER));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 19, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 32);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.easy_villagers.converting");
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "converting");
    }

    @Override
    public Class<? extends ItemStack> getRecipeClass() {
        return ItemStack.class;
    }

}
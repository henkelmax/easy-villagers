package de.maxhenkel.easyvillagers.integration.jei.incubator;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class IncubatorCategory implements IRecipeCategory<ItemStack> {

    private IGuiHelper helper;

    public IncubatorCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(Main.MODID, "textures/gui/container/jei_input_output.png"), 0, 0, 72, 49);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.INCUBATOR.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemStack recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredient(VanillaTypes.ITEM, recipe);
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 32).addIngredient(VanillaTypes.ITEM, getOutput(recipe));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 19, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 32);
    }

    private ItemStack getOutput(ItemStack input) {
        ItemStack out = input.copy();
        EasyVillagerEntity villager = ModItems.VILLAGER.get().getVillager(Minecraft.getInstance().level, out);
        villager.setAge(0);
        ModItems.VILLAGER.get().setVillager(out, villager);
        return out;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.easy_villagers.incubating");
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "incubating");
    }

    @Override
    public Class<? extends ItemStack> getRecipeClass() {
        return ItemStack.class;
    }

}
package de.maxhenkel.easyvillagers.integration.jei.incubator;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.integration.jei.InputOutputCategory;
import de.maxhenkel.easyvillagers.integration.jei.JEIPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class IncubatorCategory extends InputOutputCategory {

    public IncubatorCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(ModBlocks.INCUBATOR.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemStack recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).add(VanillaTypes.ITEM_STACK, recipe);
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 32).add(VanillaTypes.ITEM_STACK, getOutput(recipe));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 19, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 32);
    }

    private ItemStack getOutput(ItemStack input) {
        ItemStack out = input.copy();
        EasyVillagerEntity villager = VillagerData.createEasyVillager(out, Minecraft.getInstance().level);
        villager.setAge(0);
        VillagerData.applyToItem(out, villager);
        return out;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.easy_villagers.incubating");
    }

    @Override
    public IRecipeType<ItemStack> getRecipeType() {
        return JEIPlugin.CATEGORY_INCUBATING;
    }

}
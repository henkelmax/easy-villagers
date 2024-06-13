package de.maxhenkel.easyvillagers.integration.jei;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.integration.jei.breeder.BreederCategory;
import de.maxhenkel.easyvillagers.integration.jei.converter.ConverterCategory;
import de.maxhenkel.easyvillagers.integration.jei.incubator.IncubatorCategory;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final RecipeType<ItemStack> CATEGORY_BREEDING = RecipeType.create(Main.MODID, "breeding", ItemStack.class);
    public static final RecipeType<ItemStack> CATEGORY_CONVERTING = RecipeType.create(Main.MODID, "converting", ItemStack.class);
    public static final RecipeType<ItemStack> CATEGORY_INCUBATING = RecipeType.create(Main.MODID, "incubating", ItemStack.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Main.MODID, "easy_villagers");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BREEDER.get()), CATEGORY_BREEDING);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CONVERTER.get()), CATEGORY_CONVERTING);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.INCUBATOR.get()), CATEGORY_INCUBATING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BreederCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ConverterCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new IncubatorCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ItemStack> foods = Villager.FOOD_POINTS.entrySet().stream().map(itemIntegerEntry -> new ItemStack(itemIntegerEntry.getKey(), (int) Math.ceil(24D / (double) itemIntegerEntry.getValue()))).toList();
        registration.addRecipes(CATEGORY_BREEDING, foods);

        List<ItemStack> potions = new ArrayList<>();
        potions.add(PotionContents.createItemStack(Items.POTION, Potions.WEAKNESS));
        potions.add(PotionContents.createItemStack(Items.POTION, Potions.LONG_WEAKNESS));
        potions.add(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.WEAKNESS));
        potions.add(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_WEAKNESS));
        potions.add(PotionContents.createItemStack(Items.LINGERING_POTION, Potions.WEAKNESS));
        potions.add(PotionContents.createItemStack(Items.LINGERING_POTION, Potions.LONG_WEAKNESS));
        registration.addRecipes(CATEGORY_CONVERTING, potions);

        registration.addRecipes(CATEGORY_INCUBATING, Collections.singletonList(VillagerItem.createBabyVillager()));
    }

}

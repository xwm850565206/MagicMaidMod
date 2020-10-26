package com.xwm.magicmaid.registry;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.tileentity.Formula;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagicFormulaRegistry
{
    private static HashMap<ResourceLocation, Formula> FORMULA;
    private static HashMap<ResourceLocation, List<ItemStack>> RESULT;
    private static HashMap<ResourceLocation, Integer> COOKTIME;

    public static void registerFormula(ResourceLocation keyItem, Formula formula, List<ItemStack> result, int cookTime)
    {
        FORMULA.put(keyItem, formula);
        RESULT.put(keyItem, result);
        COOKTIME.put(keyItem, cookTime);
    }

    public static void registerAllFormula()
    {
        FORMULA = new HashMap<>();
        RESULT = new HashMap<>();
        COOKTIME = new HashMap<>();

        registerFormula(Items.COMPASS.getRegistryName(), Formula.create(Items.COMPASS, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION),
                Lists.newArrayList(new ItemStack(ItemInit.ITEM_ABSORB_SOUL_COMPASS)), 200);

        registerFormula(Items.IRON_SWORD.getRegistryName(), Formula.create(Items.IRON_SWORD, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, Items.DIAMOND),
                Lists.newArrayList(new ItemStack(ItemInit.ITEM_OBSESSION_SWORD)), 200);

        registerFormula(ItemInit.ITEM_OBSESSION_SWORD.getRegistryName(), Formula.create(ItemInit.ITEM_OBSESSION_SWORD, ItemInit.ITEM_GHOST, ItemInit.ITEM_GHOST, ItemInit.ITEM_GHOST, ItemInit.ITEM_GHOST),
                Lists.newArrayList(new ItemStack(ItemInit.ITEM_GHOST_OBSESSION_SWORD)), 200);

        registerFormula(ItemInit.ITEM_ELIMINATE_SOUL_NAIL.getRegistryName(),Formula.create(ItemInit.ITEM_ELIMINATE_SOUL_NAIL, ItemInit.ITEM_ABSORB_SOUL_COMPASS),
                Lists.newArrayList(new ItemStack(ItemInit.ITEM_NO_SOUL_GHOST), new ItemStack(Items.COMPASS)), 200);

        registerFormula(ItemInit.ITEM_LOST_KEY.getRegistryName(), Formula.create(ItemInit.ITEM_LOST_KEY, ItemInit.ITEM_NO_SOUL_GHOST, ItemInit.ITEM_NO_SOUL_GHOST),
                Lists.newArrayList(new ItemStack(ItemInit.ITEM_JUSTICE_SOUL_PIECE), new ItemStack(ItemInit.ITEM_EVIL_SOUL_PIECE)), 200);
    }

    public static Formula getFormula(ItemStack keyItem)
    {
        return FORMULA.getOrDefault(keyItem.getItem().getRegistryName(), Formula.EMPTY);
    }

    public static List<ItemStack> getResult(ItemStack keyItem)
    {
        return RESULT.getOrDefault(keyItem.getItem().getRegistryName(), Lists.newArrayList(ItemStack.EMPTY));
    }

    public static int getCookTime(ItemStack keyItem)
    {
        return COOKTIME.getOrDefault(keyItem.getItem().getRegistryName(), 0);
    }
}

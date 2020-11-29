package com.xwm.magicmaid.registry;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.tileentity.*;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class MagicFormulaRegistry
{
    private static HashMap<ResourceLocation, Formula> FORMULA;
    private static HashMap<ResourceLocation, Result> RESULT;
    private static HashMap<ResourceLocation, Integer> COOKTIME;

    public static void registerFormula(ResourceLocation keyItem, Formula formula, Result result, int cookTime)
    {
        FORMULA.put(keyItem, formula);
        RESULT.put(keyItem, result);
        COOKTIME.put(keyItem, cookTime);
    }

    private static void registerUpLevelDamageFormula(Item keyItem, Item rawItem)
    {
        ItemStack rawItemStack = new ItemStack(rawItem);
        ItemStack keyItemStack = new ItemStack(keyItem);

        registerFormula(keyItem.getRegistryName(), FormulaUpLevelDamage.create(keyItemStack, rawItemStack, rawItemStack),
                ResultDamage.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1), 50);
    }

    public static void registerAllFormula()
    {
        FORMULA = new HashMap<>();
        RESULT = new HashMap<>();
        COOKTIME = new HashMap<>();

        registerFormula(Items.COMPASS.getRegistryName(), Formula.create(Items.COMPASS, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION),
                Result.create(Lists.newArrayList(new ItemStack(ItemInit.ITEM_ABSORB_SOUL_COMPASS))), 200);

        registerFormula(Items.IRON_SWORD.getRegistryName(), Formula.create(Items.IRON_SWORD, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, ItemInit.ITEM_OBSESSION, Items.DIAMOND),
                Result.create(Lists.newArrayList(new ItemStack(ItemInit.ITEM_OBSESSION_SWORD))), 200);

        registerFormula(ItemInit.ITEM_OBSESSION_SWORD.getRegistryName(), Formula.create(ItemInit.ITEM_OBSESSION_SWORD, ItemInit.ITEM_GHOST, ItemInit.ITEM_GHOST, ItemInit.ITEM_GHOST, ItemInit.ITEM_GHOST),
                Result.create(Lists.newArrayList(new ItemStack(ItemInit.ITEM_GHOST_OBSESSION_SWORD))), 200);

        registerFormula(ItemInit.ITEM_ELIMINATE_SOUL_NAIL.getRegistryName(),Formula.create(ItemInit.ITEM_ELIMINATE_SOUL_NAIL, ItemInit.ITEM_ABSORB_SOUL_COMPASS),
                Result.create(Lists.newArrayList(new ItemStack(ItemInit.ITEM_NO_SOUL_GHOST), new ItemStack(Items.COMPASS))), 200);

        registerFormula(ItemInit.ITEM_LOST_KEY.getRegistryName(), Formula.create(ItemInit.ITEM_LOST_KEY, ItemInit.ITEM_NO_SOUL_GHOST, ItemInit.ITEM_NO_SOUL_GHOST),
                Result.create(Lists.newArrayList(new ItemStack(ItemInit.ITEM_JUSTICE_SOUL_PIECE), new ItemStack(ItemInit.ITEM_EVIL_SOUL_PIECE))), 200);

        registerFormula(Item.getItemFromBlock(Blocks.COBBLESTONE).getRegistryName(), Formula.create(Item.getItemFromBlock(Blocks.COBBLESTONE), Items.STICK, Items.STICK, ItemInit.ITEM_OBSESSION),
                Result.create(Lists.newArrayList(new ItemStack(Item.getItemFromBlock(BlockInit.CHURCH_PORTAL_FRAME)))), 200);

        registerFormula(ItemInit.ITEM_THE_GOSPELS.getRegistryName(), Formula.create(ItemInit.ITEM_THE_GOSPELS, ItemInit.ITEM_THE_GOSPELS, ItemInit.ITEM_THE_GOSPELS, ItemInit.ITEM_THE_GOSPELS, ItemInit.ITEM_THE_GOSPELS),
                Result.create(Lists.newArrayList(new ItemStack(Items.EMERALD), new ItemStack(Items.PAPER), new ItemStack(Items.PAPER))), 200);

        registerFormula(ItemInit.ITEM_HOLY_STONE.getRegistryName(), FormulaUpLevelDamage.create(new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE)),
                ResultDamage.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1), 50);

        String levelKey = Reference.MODID + "_level";
        registerFormula(ItemInit.ITEM_REPANTENCE.getRegistryName(), FormulaUpLevelComponent.create(new ItemStack(ItemInit.ITEM_REPANTENCE), levelKey, new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE)),
                ResultNBTTagCompound.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1, levelKey), 50);

        registerFormula(ItemInit.ITEM_CONVICTION.getRegistryName(), FormulaUpLevelComponent.create(new ItemStack(ItemInit.ITEM_CONVICTION), levelKey, new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE)),
                ResultNBTTagCompound.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1, levelKey), 50);

        registerFormula(ItemInit.ITEM_PANDORA.getRegistryName(), FormulaUpLevelComponent.create(new ItemStack(ItemInit.ITEM_PANDORA), levelKey, new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE)),
                ResultNBTTagCompound.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1, levelKey), 50);

        registerFormula(ItemInit.ITEM_WHISPER.getRegistryName(), FormulaUpLevelComponent.create(new ItemStack(ItemInit.ITEM_WHISPER), levelKey, new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE)),
                ResultNBTTagCompound.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1, levelKey), 50);

        registerFormula(ItemInit.ITEM_DEMON_KILLER_SWORD.getRegistryName(), FormulaUpLevelComponent.create(new ItemStack(ItemInit.ITEM_DEMON_KILLER_SWORD), levelKey, new ItemStack(ItemInit.ITEM_HOLY_STONE), new ItemStack(ItemInit.ITEM_HOLY_STONE)),
                ResultNBTTagCompound.create(new ArrayList<>(), Result.MetadataTypes.INCREASE, 1, levelKey), 50);
//        ItemStack stone0 = new ItemStack(ItemInit.ITEM_HOLY_STONE);
//        stone0.setItemDamage(0);
//        registerFormula(ItemInit.ITEM_HOLY_STONE.getRegistryName(), FormulaDamageCareless.create(stone0.copy(), stone0.copy(), stone0.copy(), stone0.copy(), stone0.copy()),
//                ResultDamage.create(Lists.newArrayList(), 1), 50);


    }

    public static Formula getFormula(ItemStack keyItem)
    {
        return FORMULA.getOrDefault(keyItem.getItem().getRegistryName(), Formula.EMPTY);
    }

    public static Result getResult(ItemStack keyItem)
    {
        return RESULT.getOrDefault(keyItem.getItem().getRegistryName(),  Result.create(Lists.newArrayList(ItemStack.EMPTY)));
    }

    public static int getCookTime(ItemStack keyItem)
    {
        return COOKTIME.getOrDefault(keyItem.getItem().getRegistryName(), 0);
    }

    public static HashMap<ResourceLocation, Formula> getFormulaMap()
    {
        return FORMULA;
    }
}

package com.xwm.magicmaid.object.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//raw 物品必须全部一样
public class FormulaUpLevelComponent extends FormulaUpLevelDamage
{
    protected String componentKey;

    protected FormulaUpLevelComponent(int rawSize, String componentKey) {
        super(rawSize);
        this.componentKey = componentKey;
    }

    protected FormulaUpLevelComponent(ItemStack keyItem, String componentKey, ItemStack... stacks) {
        super(keyItem, stacks);
        this.componentKey = componentKey;
    }

    protected FormulaUpLevelComponent(ItemStack keyItem, String componentKey, List<ItemStack> stacks) {
        super(keyItem, stacks);
        this.componentKey = componentKey;
    }

    protected FormulaUpLevelComponent(ItemStack keyItem, boolean ordered, ItemStack... stacks) {
        super(keyItem, ordered, stacks);
        this.componentKey = "";
    }

    protected FormulaUpLevelComponent(ItemStack keyItem, boolean ordered, List<ItemStack> stacks) {
        super(keyItem, ordered, stacks);
        this.componentKey = "";
    }

    public static FormulaUpLevelComponent create(ItemStack keyItem, List<ItemStack> itemStacks)
    {
        return new FormulaUpLevelComponent(keyItem, "", itemStacks);
    }

    public static FormulaUpLevelComponent create(Item keyItem, Item ...items)
    {
        List<Item> list = Arrays.asList(items);
        List<ItemStack> list1 = new ArrayList<>();
        for (Item item : list) {
            list1.add(new ItemStack(item));
        }
        return new FormulaUpLevelComponent(new ItemStack(keyItem), "", list1);
    }

    public static FormulaUpLevelComponent create(ItemStack keyItem, String key, ItemStack ...stacks) {
        return new FormulaUpLevelComponent(keyItem, key, stacks);
    }

    public static FormulaUpLevelComponent empty(int rawSize) {
        return new FormulaUpLevelComponent(rawSize, "");
    }

    @Override
    protected int getMaxLevel() {
        return 7;
    }

    @Override
    protected int getItemDamage(Formula f){
        NBTTagCompound compound = f.keyItem.getTagCompound();
        if (compound == null || !compound.hasKey(this.componentKey))
            return 0;
        return compound.getInteger(this.componentKey);
    }
}

package com.xwm.magicmaid.object.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//raw 物品必须全部一样
public class FormulaUpLevelDamage extends FormulaDamageCareless
{

    protected FormulaUpLevelDamage(int rawSize) {
        super(rawSize);
    }

    protected FormulaUpLevelDamage(ItemStack keyItem, ItemStack... stacks) {
        super(keyItem, stacks);
    }

    protected FormulaUpLevelDamage(ItemStack keyItem, List<ItemStack> stacks) {
        super(keyItem, stacks);
    }

    protected FormulaUpLevelDamage(ItemStack keyItem, boolean ordered, ItemStack... stacks) {
        super(keyItem, ordered, stacks);
    }

    protected FormulaUpLevelDamage(ItemStack keyItem, boolean ordered, List<ItemStack> stacks) {
        super(keyItem, ordered, stacks);
    }

    public static FormulaUpLevelDamage create(ItemStack keyItem, List<ItemStack> itemStacks)
    {
        return new FormulaUpLevelDamage(keyItem, itemStacks);
    }

    public static FormulaUpLevelDamage create(Item keyItem, Item ...items)
    {
        List<Item> list = Arrays.asList(items);
        List<ItemStack> list1 = new ArrayList<>();
        for (Item item : list) {
            list1.add(new ItemStack(item));
        }
        return new FormulaUpLevelDamage(new ItemStack(keyItem), list1);
    }

    public static FormulaUpLevelDamage create(ItemStack keyItem, ItemStack ...stacks) {
        return new FormulaUpLevelDamage(keyItem, stacks);
    }

    public static FormulaUpLevelDamage empty(int rawSize) {
        return new FormulaUpLevelDamage(rawSize);
    }

    protected int getMaxLevel() {
        return 6;
    }

    protected int getItemDamage(){
        return this.keyItem.getItemDamage();
    }

    @Override
    public boolean equals(Formula f)
    {
        if (f.allItems.size() != this.allItems.size())
            return false;

        //所有rawitem的damage必须一样
        int damage = this.getItemDamage();
        if (damage > getMaxLevel()) return false; // 等级上限
        for (int i = 0; i < f.rawItems.size(); i++) {
            if (!f.rawItems.get(i).isEmpty() && damage != f.rawItems.get(i).getItemDamage()) return false;
        }

        if (ordered)
        {
            for (int i = 0; i < f.allItems.size(); i++) {
                ItemStack a = this.allItems.get(i);
                ItemStack b = f.allItems.get(i);
                if (!isItemEqual(a, b))
                    return false;
            }
            return true;
        }
        else
        {
            //todo 这里的比较效率可以用更低复杂度的
            boolean[] mark = new boolean[this.allItems.size()];
            Arrays.fill(mark, false);
            for (int i = 0; i < this.allItems.size(); i++)
            {
                boolean flag = false;
                ItemStack a = this.allItems.get(i);
                for (int j = 0; j < f.allItems.size(); j++)
                {
                    ItemStack b = f.allItems.get(j);
                    if ((a.isEmpty() && b.isEmpty()) || isItemEqual(a, b)) {
                        if (!mark[j]) {
                            flag = true;
                            mark[j] = true;
                            break;
                        }
                    }
                }
                if (!flag)
                    return false;
            }
            return true;
        }
    }

}

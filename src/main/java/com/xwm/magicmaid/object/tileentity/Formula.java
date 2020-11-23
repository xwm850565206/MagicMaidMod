package com.xwm.magicmaid.object.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Formula
{
    public static Formula EMPTY = Formula.empty(0);

    protected List<ItemStack> rawItems;
    protected ItemStack keyItem;
    protected List<ItemStack> allItems;
    protected boolean ordered;
    protected int cookTime;

    protected Formula(int rawSize) {
        this.rawItems = NonNullList.withSize(rawSize, ItemStack.EMPTY);
        this.keyItem = ItemStack.EMPTY;
        this.allItems = NonNullList.withSize(rawSize + 1, ItemStack.EMPTY);
        this.ordered = false;
    }

    public List<ItemStack> getRawItems() {
        return rawItems;
    }

    public ItemStack getKeyItem() {
        return keyItem;
    }

    public List<ItemStack> getAllItems() {
        return allItems;
    }

    protected Formula(ItemStack keyItem, ItemStack ...stacks)
    {
       this(keyItem, Arrays.asList(stacks));
    }

    protected Formula(ItemStack keyItem, List<ItemStack> stacks)
    {
        this(keyItem, false, stacks);
    }

    protected Formula(ItemStack keyItem, boolean ordered, ItemStack ...stacks)
    {
       this(keyItem, ordered, Arrays.asList(stacks));
    }

    protected Formula(ItemStack keyItem, boolean ordered, List<ItemStack> stacks)
    {
        this.rawItems = Lists.newArrayList(stacks);
        while (this.rawItems.size() < 4)
            this.rawItems.add(ItemStack.EMPTY);
        this.keyItem = keyItem;
        this.allItems = Lists.newArrayList(this.rawItems.iterator());
        this.allItems.add(keyItem);
        this.ordered = ordered;
    }

    public boolean equals(Formula f)
    {
        if  (f.allItems.size() != this.allItems.size())
            return false;

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

    public static Formula create(ItemStack keyItem, List<ItemStack> itemStacks)
    {
        return new Formula(keyItem, itemStacks);
    }

    public static Formula create(Item keyItem, Item ...items)
    {
        List<Item> list = Arrays.asList(items);
        List<ItemStack> list1 = new ArrayList<>();
        for (Item item : list) {
            list1.add(new ItemStack(item));
        }
        return new Formula(new ItemStack(keyItem), list1);
    }

    public static Formula create(ItemStack keyItem, ItemStack ...stacks) {
        return new Formula(keyItem, stacks);
    }

    public static Formula empty(int rawSize) {
        return new Formula(rawSize);
    }

    protected boolean isItemEqual(ItemStack a, ItemStack b)
    {
        return !b.isEmpty() && a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }
}

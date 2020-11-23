package com.xwm.magicmaid.object.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormulaDamageCareless extends Formula
{

    protected FormulaDamageCareless(int rawSize) {
        super(rawSize);
    }

    protected FormulaDamageCareless(ItemStack keyItem, ItemStack... stacks) {
        super(keyItem, stacks);
    }

    protected FormulaDamageCareless(ItemStack keyItem, List<ItemStack> stacks) {
        super(keyItem, stacks);
    }

    protected FormulaDamageCareless(ItemStack keyItem, boolean ordered, ItemStack... stacks) {
        super(keyItem, ordered, stacks);
    }

    protected FormulaDamageCareless(ItemStack keyItem, boolean ordered, List<ItemStack> stacks) {
        super(keyItem, ordered, stacks);
    }

    public static FormulaDamageCareless create(ItemStack keyItem, List<ItemStack> itemStacks)
    {
        return new FormulaDamageCareless(keyItem, itemStacks);
    }

    public static FormulaDamageCareless create(Item keyItem, Item ...items)
    {
        List<Item> list = Arrays.asList(items);
        List<ItemStack> list1 = new ArrayList<>();
        for (Item item : list) {
            list1.add(new ItemStack(item));
        }
        return new FormulaDamageCareless(new ItemStack(keyItem), list1);
    }

    public static FormulaDamageCareless create(ItemStack keyItem, ItemStack ...stacks) {
        return new FormulaDamageCareless(keyItem, stacks);
    }

    public static FormulaDamageCareless empty(int rawSize) {
        return new FormulaDamageCareless(rawSize);
    }


    @Override
    protected boolean isItemEqual(ItemStack a, ItemStack b)
    {
        return !b.isEmpty() && a.getItem() == b.getItem();
    }
}

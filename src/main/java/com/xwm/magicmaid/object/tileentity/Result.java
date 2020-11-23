package com.xwm.magicmaid.object.tileentity;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class Result
{
    protected List<ItemStack> stacks;
    protected int metadata;
    protected MetadataTypes type;

    protected Result(List<ItemStack> stacks, MetadataTypes type, int metadata)
    {
        this.stacks = stacks;
        this.metadata = metadata;
        this.type = type;
    }

    public static Result create(List<ItemStack> stacks, int metadata)
    {
        return new Result(stacks, MetadataTypes.SET, metadata);
    }

    public static Result create(List<ItemStack> stacks)
    {
        return new Result(stacks, MetadataTypes.SET, 0);
    }

    public List<ItemStack> getResult(@Nullable ItemStack keyItem)
    {
        return stacks;
    }


    public List<ItemStack> getStacks() {
        return stacks;
    }

    public void setStacks(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public int getMetadata() {
        return metadata;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    public MetadataTypes getType() {
        return type;
    }

    public void setType(MetadataTypes type) {
        this.type = type;
    }

    public enum MetadataTypes{
        INCREASE,
        DECREASE,
        SET
    }
}

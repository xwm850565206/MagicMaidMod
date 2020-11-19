package com.xwm.magicmaid.util.instruction;

import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ItemElement extends ContentElement
{
    public ResourceLocation resourceLocation;

    public ItemElement(EnumInstructElement type, ITextComponent nameCached, List<ITextComponent> description, ResourceLocation resourceLocation) {
        super(type, nameCached, description);
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public String toString() {
        return "ItemElement{" +
                "resourceLocation=" + resourceLocation +
                ", description=" + description +
                ", type=" + type +
                ", nameCached=" + nameCached +
                '}';
    }
}

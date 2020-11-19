package com.xwm.magicmaid.util.instruction;

import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class EntityElement extends ItemElement
{
    public EntityElement(EnumInstructElement type, ITextComponent nameCached, List<ITextComponent> description, ResourceLocation resourceLocation) {
        super(type, nameCached, description, resourceLocation);
    }
}

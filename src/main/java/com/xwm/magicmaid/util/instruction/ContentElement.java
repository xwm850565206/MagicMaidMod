package com.xwm.magicmaid.util.instruction;

import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ContentElement extends InstructElement
{
    public List<ITextComponent> description;

    public ContentElement(EnumInstructElement type, ITextComponent nameCached, List<ITextComponent> description) {
        super(type, nameCached);
        this.description = description;
    }

    public List<ITextComponent> getDescription() {
        return description;
    }

    public void setDescription(List<ITextComponent> description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ContentElement{" +
                "description=" + description +
                ", type=" + type +
                ", nameCached=" + nameCached +
                '}';
    }
}

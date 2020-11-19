package com.xwm.magicmaid.util.instruction;

import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ImageElement extends ContentElement
{
    public ResourceLocation imageLocation;

    public ImageElement(EnumInstructElement type, ITextComponent nameCached, List<ITextComponent> description, ResourceLocation imageLocation) {
        super(type, nameCached, description);
        this.imageLocation = imageLocation;
    }

    public ResourceLocation getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(ResourceLocation imageLocation) {
        this.imageLocation = imageLocation;
    }

    @Override
    public String toString() {
        return "ImageElement{" +
                "imageLocation=" + imageLocation +
                ", description=" + description +
                ", type=" + type +
                ", nameCached=" + nameCached +
                '}';
    }
}

package com.xwm.magicmaid.util.instruction;

import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class InstructElement {

    public static InstructElement EMPTY = new InstructElement(EnumInstructElement.NONE, new TextComponentString("empty"));

    public EnumInstructElement type;
    public ITextComponent nameCached;

    public InstructElement(EnumInstructElement type, ITextComponent nameCached) {
        this.type = type;
        this.nameCached = nameCached;
    }

    public EnumInstructElement getType() {
        return type;
    }

    public void setType(EnumInstructElement type) {
        this.type = type;
    }

    public ITextComponent getNameCached() {
        return nameCached;
    }

    public void setNameCached(ITextComponent nameCached) {
        this.nameCached = nameCached;
    }


    @Override
    public String toString() {
        return "InstructElement{" +
                "type=" + type +
                ", nameCached=" + nameCached +
                '}';
    }

    /**
     * 返回说明元素，不包括formula 和 recipe
     * @param rawInstructElement
     * @param maxLenth
     * @param fontRenderer
     * @return
     */
    public static InstructElement create(RawInstructElement rawInstructElement, int maxLenth, FontRenderer fontRenderer)
    {
        EnumInstructElement enumInstructElement = EnumInstructElement.parse(rawInstructElement.type);
        switch (enumInstructElement)
        {
            case NONE:
                return EMPTY;
            case ITEM:
                return new ItemElement(enumInstructElement, new TextComponentString(rawInstructElement.name),
                         GuiUtilRenderComponents.splitText(new TextComponentString(rawInstructElement.description), maxLenth, fontRenderer, true, true),
                        new ResourceLocation(rawInstructElement.resourceLocation));
            case TITLE:
                return new TitleElement(enumInstructElement, new TextComponentString(rawInstructElement.name));
            case CONTENT:
                return new ContentElement(enumInstructElement, new TextComponentString(rawInstructElement.name),
                        GuiUtilRenderComponents.splitText(new TextComponentString(rawInstructElement.description), maxLenth, fontRenderer, true, true));
            case ENTITY:
                return new EntityElement(enumInstructElement, new TextComponentString(rawInstructElement.name),
                        GuiUtilRenderComponents.splitText(new TextComponentString(rawInstructElement.description), maxLenth, fontRenderer, true, true),
                        new ResourceLocation(rawInstructElement.resourceLocation));
            case IMAGE:
                return new ImageElement(enumInstructElement, new TextComponentString(rawInstructElement.name),
                        GuiUtilRenderComponents.splitText(new TextComponentString(rawInstructElement.description == null? "" : rawInstructElement.description), maxLenth, fontRenderer, true, true),
                        new ResourceLocation(rawInstructElement.resourceLocation));
            default:
                return EMPTY;
        }
    }
}

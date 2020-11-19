package com.xwm.magicmaid.util.instruction;

import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import com.xwm.magicmaid.object.tileentity.Formula;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class FormulaElement extends InstructElement
{
    public List<ItemStack> formula;

    public FormulaElement(EnumInstructElement type, ITextComponent nameCached, List<ItemStack> formula) {
        super(type, nameCached);
        this.formula = formula;
    }

    public List<ItemStack> getFormula() {
        return formula;
    }
}

package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.manager.ISkillManagerImpl;
import com.xwm.magicmaid.object.item.interfaces.ICanGetSkillPoint;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSkillBook extends ItemBase implements ICanGetSkillPoint
{
    public ItemSkillBook(String name) {
        super(name);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab))
            return;

        for (Class<? extends ISkill> skillClazz : MagicSkillRegistry.SKILL_MAP.values()) {
            try {
                ISkill iSkill = skillClazz.newInstance();
                String skillName = iSkill.getName();
                String[] describeElements = skillName.split("\\.");
                if (describeElements.length < 3)
                    continue;
                if (describeElements[0].equals("perform") && !describeElements[1].equals("unreachable"))
                {
                    ItemStack stack = new ItemStack(this);
                    ItemSkillBook.setSkill(stack, skillName);
                    items.add(stack);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.RED + "一本遗失的技能书");
        ISkill skill = ItemSkillBook.getSkill(stack);
        if (skill == null)
        {
            tooltip.add("书页太过残破，无法从中学习到技能");
        }
        else {
            tooltip.add("右键学习: " + TextFormatting.LIGHT_PURPLE + skill.getDescription());
            tooltip.add("");
            tooltip.add("已学会的技能无法重复学习");
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (handIn == EnumHand.MAIN_HAND && !worldIn.isRemote)
        {
            if (playerIn.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
            {
                ISkill bookSkill = ItemSkillBook.getSkill(playerIn.getHeldItem(handIn));
                if (bookSkill == null) {
                    return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
                }
                ISkillCapability skillCapability = (ISkillCapability) playerIn.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                if (skillCapability.getPerformSkill(bookSkill.getName()) != null)
                {
                    return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
                }
                else {
                    if (bookSkill.getName().startsWith("perform"))
                    {
                        skillCapability.setPerformSkill(bookSkill.getName(), (IPerformSkill) bookSkill);
                    }
                    else if (bookSkill.getName().startsWith("attribute"))
                    {
                        skillCapability.setAttributeSkill(bookSkill.getName(), (IAttributeSkill) bookSkill);
                    }
                    else if (bookSkill.getName().startsWith("passive"))
                    {
                        skillCapability.setPassiveSkill(bookSkill.getName(), (IPassiveSkill) bookSkill);
                    }
                    ISkillManagerImpl.getInstance().updateToClient(playerIn); // 更新到客户端

                    playerIn.getHeldItem(handIn).shrink(1); // 没有生成新对象 可能会有问题
                    return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
                }
            }
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));

        }
        else
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));

    }

    public static ISkill getSkill(ItemStack stack)
    {
        NBTTagCompound data = stack.getOrCreateSubCompound(Reference.MODID);
        if (data.hasKey("skill"))
        {
            return MagicSkillRegistry.getSkill(data.getString("skill"));
        }

        return null;
    }

    public static void setSkill(ItemStack stack, String name)
    {
        NBTTagCompound data = stack.getOrCreateSubCompound(Reference.MODID);
        if (!data.hasKey("skill"))
        {
            data.setString("skill", name);
        }
    }

    public static void setSkill(ItemStack stack, ISkill skill)
    {
        setSkill(stack, skill.getName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getSkillPoint(ItemStack stack, EntityPlayer player) {
        return 5000;
    }
}

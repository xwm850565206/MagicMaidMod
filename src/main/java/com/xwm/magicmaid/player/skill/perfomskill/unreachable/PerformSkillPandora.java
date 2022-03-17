package com.xwm.magicmaid.player.skill.perfomskill.unreachable;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class PerformSkillPandora extends PerformSkillUnreachableBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon_2.png");

    @Override
    public int getPerformEnergy() {
        return 0;
    }

    @Override
    public int getColdTime() {
        return 0;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        ItemStack stack = playerIn.getHeldItemMainhand();
        if (stack.getItem() != ItemInit.ITEM_PANDORA) {
            playerIn.sendMessage(new TextComponentString("必须手持[潘多拉魔盒]才可以释放"));
            return;
        }

        ItemEquipment equipment = (ItemEquipment) stack.getItem();
        stack.damageItem(1, playerIn);
        int level = equipment.getLevel(stack);

        List<EntityLivingBase> entityLivingBases = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, playerIn.getEntityBoundingBox().grow(level*2, level, level*2));
        Vec3d vec3d = playerIn.getLookVec();
        vec3d = vec3d.scale(level*2);
        BlockPos pos = playerIn.getPosition().add(new Vec3i(vec3d.x, vec3d.y, vec3d.z));
        for (EntityLivingBase entityLivingBase : entityLivingBases)
        {
            if(MagicEquipmentUtils.checkEnemy(playerIn, entityLivingBase))
                entityLivingBase.setPosition(pos.getX(), pos.getY()+2, pos.getZ());
        }


        curColdTime = getColdTime();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getRequirePoint() {
        return 0;
    }

    @Override
    public String getName() {
        return super.getName() + ".pandora";
    }

    @Override
    public String getDescription() {
        return "武器专精:潘多拉魔盒";
    }


    @Override
    public String getDetailDescription() {
        return "手持潘多拉魔盒，将周围的怪物聚集在指针处\n范围随着武器等级提升而提升";
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 40.0;
        double scaley = 46.0 / 40.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 80, 40, 40);
        GlStateManager.popMatrix();
    }
}

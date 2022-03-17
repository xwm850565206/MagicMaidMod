package com.xwm.magicmaid.player.skill.perfomskill.unreachable;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.IProcessManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketParticle;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.process.ProcessTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class PerformSkillDemonKiller extends PerformSkillUnreachableBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon_2.png");

    @Override
    public int getPerformEnergy() {
        return 200;
    }

    @Override
    public int getColdTime() {
        return 200;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        ItemStack stack = playerIn.getHeldItemMainhand();
        if (stack.getItem() != ItemInit.ITEM_DEMON_KILLER_SWORD) {
            playerIn.sendMessage(new TextComponentString("必须手持[灭魔大剑]才可以释放"));
            return;
        }

        IProcessManagerImpl.getInstance().addTask(new ProcessSkillTaskDemonKiller(1, playerIn, 110));
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
        return super.getName() + ".demon_killer";
    }

    @Override
    public String getDescription() {
        return "武器专精:灭魔大剑";
    }


    @Override
    public String getDetailDescription() {
        return "手持灭魔大剑，奋力跃起，随后猛砸地面\n伤害和范围随着武器等级提升而提升";
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

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 40, 40);
        GlStateManager.popMatrix();
    }

    private static class ProcessSkillTaskDemonKiller extends ProcessTask
    {
        private World world;
        private EntityLivingBase player;

        public ProcessSkillTaskDemonKiller(int priority, EntityLivingBase taskOwner, int maxAge) {
            super(priority, taskOwner, maxAge);
            this.world = taskOwner.getEntityWorld();
            this.player = taskOwner;
        }

        @Override
        public void update() {

            try {
                ItemStack stack = player.getHeldItemMainhand();
                ItemEquipment equipment = (ItemEquipment) stack.getItem();
                stack.damageItem(1, player);
                int level = equipment.getLevel(stack);

                taskOwner.motionX = 0;
                taskOwner.motionZ = 0;

                if (age == 0)
                    taskOwner.motionY += 1.5 + level * 0.15;

                if (age >= 40 && age <= 100)
                    taskOwner.motionY = 0;

                if (age == 100) {
                    for (int i = 0; i < 256; i++) {
                        BlockPos pos = taskOwner.getPosition().down(i);
                        if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
                            taskOwner.setPosition(taskOwner.posX, pos.getY() + 1, taskOwner.posZ);

                            if (!world.isRemote) {
                                int n = level * 12;
                                int r = level * 4;
                                double perAngle = 360.0 / n;
                                for (int t = 1; t <= r; t++) {
                                    for (int j = 0; j < n; j++) {
                                        for (int k = 0; k < 4; k++) {
                                            SPacketParticle packet = new SPacketParticle(taskOwner.posX + t * Math.sin(perAngle * j), taskOwner.posY + 0.25 * k, taskOwner.posZ + t * Math.cos(perAngle * j), EnumParticleTypes.CLOUD);
                                            NetworkLoader.instance.sendToAllAround(packet, new NetworkRegistry.TargetPoint(taskOwner.dimension, taskOwner.posX, taskOwner.posY, taskOwner.posZ, r * 4));
                                        }
                                    }
                                }

                                List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class, taskOwner.getEntityBoundingBox().grow(r, 1, r));
                                for (EntityLivingBase entityLivingBase : entityLivingBases) {
                                    if (MagicEquipmentUtils.checkEnemy(player, entityLivingBase)) {
                                        IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLivingBase, DamageSource.causeExplosionDamage(player), (level + 1) * 10);
                                    }
                                }
                            }

                            break;
                        }
                    }
                }

                age++;
            } catch (Exception e) {
                this.setAge(this.getMaxAge());
            }
        }
    }
}

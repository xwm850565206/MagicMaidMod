package com.xwm.magicmaid.player.skill.perfomskill.unreachable;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.IProcessManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.process.ProcessTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PerformSkillWhisper extends PerformSkillUnreachableBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon_2.png");
    private Random random = new Random();

    @Override
    public int getPerformEnergy() {
        return 200;
    }

    @Override
    public int getColdTime() {
        return 400;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        ItemStack stack = playerIn.getHeldItemMainhand();
        if (stack.getItem() != ItemInit.ITEM_WHISPER) {
            playerIn.sendMessage(new TextComponentString("必须手持[低语]才可以释放"));
            return;
        }

        ItemEquipment equipment = (ItemEquipment) stack.getItem();
        int level = equipment.getLevel(stack);

        IProcessManagerImpl.getInstance().addTask(new ProcessSkillTaskWhisper(1, playerIn, 125, level));


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
        return super.getName() + ".whisper";
    }

    @Override
    public String getDescription() {
        return "武器专精:低语";
    }


    @Override
    public String getDetailDescription() {
        return "手持低语，跃向空中，随后创造多个六芒星阵进行大范围的轰击\n轰击范围和伤害随着武器等级提升而提升";
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 164, 46, 46);
        GlStateManager.popMatrix();
    }

    private static class ProcessSkillTaskWhisper extends ProcessTask
    {
        private World world;
        private EntityLivingBase player;
        private int level;
        private Random random = new Random();
        private List<AxisAlignedBB> cbbs = new ArrayList<>();
        private static final int[] hexameron = new int[]{0, 60, 120, 180, 240, 300};

        public ProcessSkillTaskWhisper(int priority, EntityLivingBase taskOwner, int maxAge, int level) {
            super(priority, taskOwner, maxAge);
            this.world = taskOwner.getEntityWorld();
            this.player = taskOwner;
            this.level = level;
        }

        @Override
        public void update() {

            try {
                ItemStack stack = player.getHeldItemMainhand();
                taskOwner.motionX = 0;
                taskOwner.motionZ = 0;

                if (age == 0) {
                    for (int i = 0; i < 6; i++)
                    {
                        cbbs.add(taskOwner.getEntityBoundingBox().offset((level+1)*3*Math.cos(60*i), 0, (level+1)*3*Math.sin(60*i)));
                    }
                    taskOwner.motionY += 1.0 + level * 0.1;

                }

                if (age >= 20 && age <= 120) {
                    taskOwner.motionY = 0;
                    onUsing(stack, player, maxAge - age - 20);
                }

                if (age == 120) {
                    for (int i = 0; i < 256; i++) {
                        BlockPos pos = taskOwner.getPosition().down(i);
                        if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
                            taskOwner.setPosition(taskOwner.posX, pos.getY() + 1, taskOwner.posZ);
                            break;
                        }
                    }
                }
                age++;

            } catch (Exception e) {
                this.setAge(this.getMaxAge());
            }
        }

        public void onUse(World worldIn, EntityLivingBase playerIn, @Nullable List<EntityLivingBase> entityLivingBases, AxisAlignedBB cbb) {
            for (EntityLivingBase entityLiving1 : entityLivingBases)
            {
                try{
                    if (playerIn instanceof EntityPlayer && !MagicEquipmentUtils.checkEnemy((EntityPlayer) playerIn, entityLiving1))
                        continue;
                    // 造成大量伤害伤害 和 10点真实伤害
                    if (playerIn instanceof EntityPlayer)
                        IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLiving1, DamageSource.causePlayerDamage((EntityPlayer) playerIn), level*4);
                    else
                        IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLiving1, DamageSource.causeMobDamage(playerIn), level*4);

                    entityLiving1.getCombatTracker().trackDamage(DamageSource.LIGHTNING_BOLT, entityLiving1.getHealth(), 0);
                    IMagicCreatureManagerImpl.getInstance().setHealth(entityLiving1, entityLiving1.getHealth() - 10);
                    playerIn.getEntityWorld().playEvent(3000, entityLiving1.getPosition(), 10);
                    float radius = level*1.5f;
                    EntityLightningBolt bolt = new EntityLightningBolt(playerIn.getEntityWorld(), entityLiving1.posX + random.nextInt(2* (int) radius) - radius, cbb.maxY, entityLiving1.posZ + random.nextInt(2 * (int) radius) - radius, true);
                    playerIn.getEntityWorld().addWeatherEffect(bolt);
                    playerIn.getEntityWorld().spawnEntity(bolt);

                }catch (Exception e){
                    ;
                }
            }
        }

        public void onUsing(ItemStack stack, EntityLivingBase player, int count) {


            for (AxisAlignedBB cbb : cbbs) {
                float radius = (level + 1) * 2;
                float perAngle = 10;
                if (count != 0 && cbb != null) {
                    //画圆
                    float d0 = (float) ((cbb.minX + cbb.maxX) / 2.0);
                    float d1 = (float) cbb.minY;
                    float d2 = (float) ((cbb.minZ + cbb.maxZ) / 2.0);
                    for (int i = 0; i < 36; i++) {
                        ParticleSpawner.spawnParticle(EnumCustomParticles.WHISPER,
                                d0 + radius * Math.sin(Math.toRadians(i * perAngle)),
                                d1,
                                d2 + radius * Math.cos(Math.toRadians(i * perAngle)),
                                0, 0, 0);
                    }
                    //六芒星
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 3; j++) {
                            int k0 = i + j * 2 % 6;
                            int k1 = i + (j + 1) * 2 % 6;
                            float t0 = (float) (d0 + radius * Math.sin(Math.toRadians(hexameron[k0])));
                            float t1 = d1;
                            float t2 = (float) (d2 + radius * Math.cos(Math.toRadians(hexameron[k0])));

                            float t3 = (float) (d0 + radius * Math.sin(Math.toRadians(hexameron[k1])));
                            float t4 = d1;
                            float t5 = (float) (d2 + radius * Math.cos(Math.toRadians(hexameron[k1])));

                            playLinearParticle(t0, t1, t2, t3, t4, t5, player.getEntityWorld());
                        }
                    }
                }
                if (count == 1 && cbb != null) {
                    List<EntityLivingBase> entityLivingBaseList = player.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, cbb.grow((level+1)*2, 2, (level+1)*2));
                    this.onUse(player.getEntityWorld(), player, entityLivingBaseList, cbb);
                }
            }
        }

        private void playLinearParticle(float t0, float t1, float t2, float t3, float t4, float t5, World world)
        {
            float gap = 10;
            float d0 = (t3 - t0) / gap;
            float d1 = (t1 + t4) / 2.0f;
            float d2 = (t5 - t2) / gap;

            for (int i = 0; i <= 10; i++)
            {
                ParticleSpawner.spawnParticle(EnumCustomParticles.WHISPER,
                        t0 + d0 * i,
                        d1,
                        t2 + d2 * i,
                        0, 0, 0
                );
            }
        }
    }

}

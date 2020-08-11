package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.selina.EntityAIPandora;
import com.xwm.magicmaid.entity.ai.selina.EntityAIWhisper;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityMagicMaidSelina extends EntityMagicMaid
{
    public EntityMagicMaidSelina(World worldIn) {
        super(worldIn);
        weaponStandbyPos = new BlockPos(1, 1, 0);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();

        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, true));


//        this.tasks.addTask(2, new EntityAIMaidAttackMelee(this, 1.3D, false));
        this.tasks.addTask(3, new EntityAIPandora(this));
        this.tasks.addTask(3, new EntityAIWhisper(this));


    }

    @Override
    public int getAttackDamage(EnumAttackType type){

        switch(type){
            case NORMAL: return 50;
            case PANDORA: return 10 + 10 * getRank();
            case WHISPER: return 100 + 100 * getRank();
            default: return super.getAttackDamage(type);
        }
    }

    @Override
    public int getAttackColdTime(EnumAttackType type){
        switch(type){
            case NORMAL: return 20;
            case PANDORA: return 60 - 5 * this.getRank();
            case WHISPER: return 100 - 10 * this.getRank();
            default: return super.getAttackColdTime(type);
        }
    }
    @Override
    public void onUpdate() {

        if (!world.isRemote)
        {
            EnumSelineState state = EnumSelineState.valueOf(this.getState());
            EnumMode mode = EnumMode.valueOf(this.getMode());
            if (mode == EnumMode.SITTING && state != EnumSelineState.SITTING)
                this.setState(EnumSelineState.toInt(EnumSelineState.SITTING));
            else if (mode == EnumMode.SERVE && state != EnumSelineState.SERVE)
                this.setState(EnumSelineState.toInt(EnumSelineState.SERVE));
            else if (mode== EnumMode.BOSS && (state == EnumSelineState.SITTING || state == EnumSelineState.SERVE))
                this.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        }

        super.onUpdate();
    }

    public void getEquipment(ItemEquipment equipment){
        if (this.world.isRemote)
            return;

        EnumEquipment equipment1 = equipment.enumEquipment;
        switch (equipment1){
            case PANDORA:
                EntityMaidWeaponPandorasBox pandorasBox = new EntityMaidWeaponPandorasBox(world);
                pandorasBox.setMaid(this);
                pandorasBox.setPosition(posX, posY + height + 1, posZ);
                world.spawnEntity(pandorasBox);
                this.setWeaponID(pandorasBox.getUniqueID());
                this.setWeaponType(EnumEquipment.toInt(equipment1));
                this.setHasWeapon(true);
                break;
            case WHISPER:
                EntityMaidWeaponWhisper whisper = new EntityMaidWeaponWhisper(world);
                whisper.setMaid(this);
                whisper.setPosition(posX, posY + height + 1, posZ);
                world.spawnEntity(whisper);
                this.setWeaponID(whisper.getUniqueID());
                this.setWeaponType(EnumEquipment.toInt(equipment1));
                this.setHasWeapon(true);
                break;
            case WISE:
                this.setHasArmor(true);
                this.setMaxHealthbarnum(200);
                this.setHealthbarnum(200);
                break;
        }


    }

    public void loseEquipment(ItemEquipment equipment){
        if (equipment instanceof  ItemWeapon){
            try {
                EntityMaidWeapon.getWeaponFromUUID(world, this.getWeaponID()).setDead();
            }catch (Exception e){
                ;
            }
            this.setWeaponType(EnumEquipment.toInt(EnumEquipment.NONE));
            this.setWeaponID(null);
            this.setHasWeapon(false);
        }
        else {
            this.setHasArmor(false);
            this.setMaxHealthbarnum(10);
        }
    }


    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if(this.getRank() >= 2 && hasArmor()){ //等级2时候不会受到过高伤害的攻击 这里还不严谨 很容易绕过
            if (amount > 5) {
                try {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) source.getTrueSource();
                    if (entityLivingBase instanceof EntityPlayer && isEnemy(entityLivingBase)){
                        entityLivingBase.sendMessage(new TextComponentString("你的物品 全都消失！"));
                        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this, "clear " + entityLivingBase.getName());
                        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this, "kick " + entityLivingBase.getName());
                    }
                    amount = 1;
                } catch (Exception e){
                    ;
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }
}

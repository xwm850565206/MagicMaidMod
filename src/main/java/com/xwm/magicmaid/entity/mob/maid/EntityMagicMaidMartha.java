package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.martha.EntityAIConviction;
import com.xwm.magicmaid.entity.ai.martha.EntityAIRepantence;
import com.xwm.magicmaid.entity.ai.martha.EntityAIMarthaServe;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityMagicMaidMartha extends EntityMagicMaid
{
    //state: 0-草莓标准站立 1-草莓服侍站立 2-草莓待命站立 3-草莓攻击

    public EntityMagicMaidMartha(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit(){
        super.entityInit();

    }

    public void initEntityAI(){
        super.initEntityAI();
        this.targetTasks.addTask(3, new EntityAIMarthaServe(this));
        this.targetTasks.addTask(3, new EntityAIRepantence(this));
        this.targetTasks.addTask(3, new EntityAIConviction(this));
    }

    @Override
    public int getAttackDamage(EnumAttackType type){

        switch(type){
            case NORMAL: return 10;
            case REPANTENCE: return this.getRank() > 0 ? 50 : 200;
            case CONVICTION: return this.getRank() > 0 ? 0 : 1;
            default: return super.getAttackDamage(type);
        }
    }

    @Override
    public int getAttackColdTime(EnumAttackType type){
        switch(type){
            case NORMAL: return 20;
            case REPANTENCE: return 100 - 30 * this.getRank();
            case CONVICTION: return 100 - 10 * this.getRank();
            default: return super.getAttackColdTime(type);
        }
    }

    @Override
    public void onUpdate()
    {
        if (!world.isRemote){
            EnumMode mode = EnumMode.valueOf(this.getMode());
            if (mode == EnumMode.SERVE && this.getState() != 1)
                this.setState(1);
            else if (mode == EnumMode.SITTING && this.getState() != 2)
                this.setState(2);
        }

        super.onUpdate();
    }

    public void getEquipment(ItemEquipment equipment){

        if (this.world.isRemote)
            return;

        EnumEquipment equipment1 = equipment.enumEquipment;
        switch (equipment1){
            case NONE: return;
            case REPATENCE:
                EntityMaidWeapon weapon1 = EnumEquipment.toEntityMaidWeapon(equipment1, world);
                weapon1.setMaid(this);
                weapon1.setPosition(this.posX, this.posY + height + 1, this.posZ);
                this.setWeaponID(weapon1.getUniqueID());
                this.setWeaponType(EnumEquipment.toInt(equipment1));
                this.setHasWeapon(true);
                this.world.spawnEntity(weapon1);
                break;
            case CONVICTION:
                EntityMaidWeapon weapon2 = EnumEquipment.toEntityMaidWeapon(equipment1, world);
                weapon2.setMaid(this);
                weapon2.setPosition(this.posX, this.posY + height + 1, this.posZ);
                this.setWeaponID(weapon2.getUniqueID());
                this.setWeaponType(EnumEquipment.toInt(equipment1));
                this.setHasWeapon(true);
                this.world.spawnEntity(weapon2);
                break;
            case PROTECTOR:
                this.setHasArmor(true);
                this.setMaxHealthbarnum(200); //提高血量上限
                this.setHealthbarnum(200);
                break;
        }

    }

    public void loseEquipment(ItemEquipment equipment){

        if (this.world.isRemote)
            return;

        if (equipment instanceof ItemWeapon){
            try {
                EntityMaidWeapon weapon1 = EntityMaidWeapon.getWeaponFromUUID(world, getWeaponID());
                weapon1.setDead();
                setWeaponID(null);
                setWeaponType(0);
                setHasWeapon(false);
            } catch (Exception e){
                ; //可能武器被其他模组杀死了
            }
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
        if (this.getRank() >= 1 && hasArmor()) //回敬伤害 要注意有可能产生死循环，所以这里要判断
        {
            try{
                if (!(source.getTrueSource() instanceof EntityMagicMaid)
                        && this.isEnemy((EntityLivingBase) source.getTrueSource()))
                    source.getTrueSource().attackEntityFrom(source, amount);

                if (!(source.getImmediateSource() instanceof EntityMagicMaid)
                        && this.isEnemy((EntityLivingBase) source.getImmediateSource()))
                    source.getImmediateSource().attackEntityFrom(source, amount);
            }catch (Exception e){
                ;//有可能会有空指针
            }
        }

        if(this.getRank() >= 2 && hasArmor()){ //等级2时候不会受到过高伤害的攻击 这里还不严谨 很容易绕过
            if (amount > 100) {
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

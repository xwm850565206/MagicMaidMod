package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import net.minecraft.entity.SharedMonsterAttributes;

import java.lang.reflect.Field;

public class MagicCreatureUtils
{
    public static void setCreatureMaxHealthLimit(long limit)
    {
        Class attributes = null;
        Field f = null;
        try {
            System.out.println("try to find maximunValue by name maximumValue...");
            attributes = Class.forName("net.minecraft.entity.ai.attributes.RangedAttribute");
            f = attributes.getDeclaredField("maximumValue");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            System.out.println("failed.");
            System.out.println("try to find maximunValue by name field_111267_a...");
            try {
                f = attributes.getDeclaredField("field_111267_a");
            } catch (NoSuchFieldException ex) {
                System.out.println("failed.");
                System.out.println("try to find maximunValue by look up highest value...");
                try {
                    Field[] fields = attributes.getDeclaredFields();
                    double max = -1;
                    int index = 0;
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        System.out.println("test: " + field);
                        if (field.getType() != double.class)
                            continue;
                        field.setAccessible(true);
                        double tmp = field.getDouble(SharedMonsterAttributes.MAX_HEALTH);
                        System.out.println("look up: " + field + " value is: " + tmp);
                        if (max < tmp) {
                            max = tmp;
                            index = i;
                        }
                        field.setAccessible(false);
                    }
                    f = fields[index];
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }

        }
        try {
            f.setAccessible(true);
            f.set(SharedMonsterAttributes.MAX_HEALTH, limit);
            System.out.println("find RangedAttribute maximumValue: " + f);
            f.setAccessible(false);
        } catch (IllegalAccessException e) {
            System.out.println("find maximumValue failed");
        } catch (NullPointerException e) {
            System.out.println("find maximumValue failed");
        }
    }

    public static void setCreatureMaxHealth(AbstractEntityMagicCreature creature, int maxHealth) {
        creature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
//        if (maxHealth < 1024) {
//        creature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
//        }
//        else {
//            Class attributes = null;
//            Field f = null;
//            try {
//                attributes = Class.forName("net.minecraft.entity.ai.attributes.RangedAttribute");
//                f = attributes.getDeclaredField("maximumValue");
//                f.setAccessible(true);
//                f.set(SharedMonsterAttributes.MAX_HEALTH, 1024 << 8);
//                creature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
//        f.set(SharedMonsterAttributes.MAX_HEALTH, 1024);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } finally {
//                f.setAccessible(false);
//            }
//        }
//    }
    }
}

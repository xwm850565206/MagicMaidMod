package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.gui.player.GuiPlayerMenuAttribute;
import com.xwm.magicmaid.gui.player.GuiPlayerMenuSkill;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MagicMenuElementRegistry
{
    public static Map<Integer, String> MENU_INDEX = new HashMap<>();
    public static Map<Integer, Class<? extends GuiScreen>> MENU_SCREEN = new HashMap<>();
    public static Map<String, Integer> INDEX_MENU = new HashMap<>();

    public static void register(int index, String name, @Nullable GuiScreen menu)
    {
        if (MENU_INDEX.containsKey(index))
            Main.logger.warn("this index has been used, you overwrite the old one");

        MENU_INDEX.put(index, name);
        INDEX_MENU.put(name, index);
        MENU_SCREEN.put(index,menu != null ? menu.getClass() : null);
    }

    public static Integer getMenuIndex(String name)
    {
        return INDEX_MENU.get(name);
    }

    public static String getIndexMenu(int index)
    {
        return MENU_INDEX.get(index);
    }

    public static GuiScreen getMenuGui(String name)
    {
        try {
            return MENU_SCREEN.get(getMenuIndex(name)).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static GuiScreen getIndexGui(int index)
    {
        try {
            return MENU_SCREEN.get(index).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static void registerAll()
    {
        register(0, "主菜单", null); // container
        register(1, "属性", new GuiPlayerMenuAttribute());
        register(2, "技能", new GuiPlayerMenuSkill());
        register(3, "武器", null); // 武器还没实装
    }
}

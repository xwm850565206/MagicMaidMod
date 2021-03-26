package com.xwm.magicmaid.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyLoader
{
    public static KeyBinding OPEN_MENU;
    public static KeyBinding SKILL_1;
    public static KeyBinding SKILL_2;

    public KeyLoader()
    {
        KeyLoader.OPEN_MENU = new KeyBinding("key.magic_maid.open", Keyboard.KEY_O, "key.categories.magic_maid");
        KeyLoader.SKILL_1 = new KeyBinding("key.magic_maid.skill_1", Keyboard.KEY_R, "key.categories.magic_maid");
        KeyLoader.SKILL_2 = new KeyBinding("key.magic_maid.skill_2", Keyboard.KEY_T, "key.categories.magic_maid");
        ClientRegistry.registerKeyBinding(KeyLoader.OPEN_MENU);
        ClientRegistry.registerKeyBinding(KeyLoader.SKILL_1);
        ClientRegistry.registerKeyBinding(KeyLoader.SKILL_2);
    }
}

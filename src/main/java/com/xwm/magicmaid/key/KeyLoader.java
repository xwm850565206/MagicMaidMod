package com.xwm.magicmaid.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyLoader
{
    public static KeyBinding OPEN_MENU;

    public KeyLoader()
    {
        KeyLoader.OPEN_MENU = new KeyBinding("key.magic_maid.open", Keyboard.KEY_O, "key.categories.magic_maid");
        ClientRegistry.registerKeyBinding(KeyLoader.OPEN_MENU);
    }
}

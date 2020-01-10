/**
 * Copyright (C) 2016 Kirsty McNaught, SpecialEffect
 * www.specialeffect.org.uk
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package com.specialeffect.mods.misc;

import org.lwjgl.glfw.GLFW;

import com.specialeffect.mods.EyeGaze;
import com.specialeffect.utils.CommonStrings;
import com.specialeffect.utils.ModUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UseItem.MODID)
public class UseItem {
	public static final String MODID = "useitem";
	public static final String NAME = "UseItem";

	public UseItem() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	@SuppressWarnings("static-access")
	private void setup(final FMLCommonSetupEvent event) {

		MinecraftForge.EVENT_BUS.register(this);

		ModUtils.setupModInfo(event, this.MODID, this.NAME, "Add key binding to use item without mouse");
		ModUtils.setAsParent(event, EyeGaze.MODID);

		// Register key bindings
		mUseItemOnceKB = new KeyBinding("Use item", GLFW.GLFW_KEY_KP_0, CommonStrings.EYEGAZE_COMMON);
		ClientRegistry.registerKeyBinding(mUseItemOnceKB);

		mUseItemContinuouslyKB = new KeyBinding("Use item continuously", GLFW.GLFW_KEY_KP_1,
				CommonStrings.EYEGAZE_EXTRA);
		ClientRegistry.registerKeyBinding(mUseItemContinuouslyKB);

		mPrevItemKB = new KeyBinding("Select previous item", GLFW.GLFW_KEY_KP_4, CommonStrings.EYEGAZE_EXTRA);
		ClientRegistry.registerKeyBinding(mPrevItemKB);

		mNextItemKB = new KeyBinding("Select next item", GLFW.GLFW_KEY_KP_5, CommonStrings.EYEGAZE_EXTRA);
		ClientRegistry.registerKeyBinding(mNextItemKB);

	}

	private static KeyBinding mUseItemOnceKB;
	private static KeyBinding mUseItemContinuouslyKB;
	private static KeyBinding mPrevItemKB;
	private static KeyBinding mNextItemKB;

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		final KeyBinding useItemKeyBinding = Minecraft.getInstance().gameSettings.keyBindUseItem;
		PlayerEntity player = Minecraft.getInstance().player;
		
		if (mUseItemContinuouslyKB.isPressed()) {
			boolean useItemNewState = !useItemKeyBinding.isKeyDown();
			KeyBinding.setKeyBindState(useItemKeyBinding.getKey(), useItemNewState);
			final String message = "Using item: " + (useItemNewState ? "ON" : "OFF");
	        player.sendMessage(new StringTextComponent(message));
		} else if (mUseItemOnceKB.isPressed()) {
			KeyBinding.onTick(useItemKeyBinding.getKey());
		} else if (mPrevItemKB.isPressed()) {
			player.inventory.changeCurrentItem(1);
		} else if (mNextItemKB.isPressed()) {
			player.inventory.changeCurrentItem(-1);
		}
	}
}

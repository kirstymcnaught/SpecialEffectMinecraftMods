package com.specialeffect.eyegazemod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = SpecialEffectAutoDestroy.MODID, version = SpecialEffectAutoDestroy.VERSION, name = SpecialEffectAutoDestroy.NAME)
public class SpecialEffectAutoDestroy {
	public static final String MODID = "specialeffect.autodestroy";
	public static final String VERSION = "0.1";
	public static final String NAME = "SpecialEffectAutoDestroy";

	private boolean mDestroying = false;
	private BlockPos mBlockToDestroy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(this);

		ModUtils.setupModInfo(event, this.MODID, this.VERSION, this.NAME,
				"Add key binding to start/stop continuously attacking.");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Subscribe to event buses
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		
		// Register key bindings	
		mDestroyKB = new KeyBinding("Auto-Destroy", Keyboard.KEY_T, "SpecialEffect");
		ClientRegistry.registerKeyBinding(mDestroyKB);
	}
	
	private static KeyBinding mDestroyKB;
	
	@SubscribeEvent
	public void onLiving(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			
			if (mDestroying) {
				// Stop attacking if we're not pointing at the block any more
				// (which means either we've destroyed it, or moved away)
				MovingObjectPosition mov = Minecraft.getMinecraft().objectMouseOver;
				World world = Minecraft.getMinecraft().theWorld;
				
				boolean blockDestroyed = (world.getBlockState(mBlockToDestroy).getBlock() instanceof BlockAir);
				
				if (mov == null || blockDestroyed) {
					final KeyBinding attackBinding = 
							Minecraft.getMinecraft().gameSettings.keyBindAttack;
					KeyBinding.setKeyBindState(attackBinding.getKeyCode(), false);
					mDestroying = false;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(mDestroyKB.isPressed()) {
			 // only attack if there's something worth attacking
			MovingObjectPosition mov = Minecraft.getMinecraft().objectMouseOver;
			if (mov == null) {
				System.out.println("Nothing to attack");
				return;
			}

			mBlockToDestroy = mov.getBlockPos();
			mDestroying = true;

			// Start attacking
			final KeyBinding attackBinding = 
					Minecraft.getMinecraft().gameSettings.keyBindAttack;
			KeyBinding.setKeyBindState(attackBinding.getKeyCode(), true);
		}
	}
}

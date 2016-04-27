package com.specialeffect.utils;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModUtils {

	// This is where we specify the version that all our mods use
	public static final String VERSION = "0.2";

	// version of optikey to use
	public static final String OPTIKEY_VERSION = "2.2.4.1";

	// A helper function to replace mcmod.info. Must be called with 
	// a pre-init event.
	public static void setupModInfo(FMLPreInitializationEvent event,
									String modId, String modName,
									String modDescription) {

		// Adding info here avoids having to maintain a mcmod.info file.
		ModMetadata m = event.getModMetadata(); 		
		m.autogenerated = false;
		m.modId = modId;
		m.version = VERSION;
		m.name = modName;
		m.url = "https://github.com/kirstymcnaught/SpecialEffectMinecraftMods";
		m.description = modDescription;
		m.description += "\n\nFor eye control, use OptiKey version " + OPTIKEY_VERSION;
		m.authorList.add("Kirsty McNaught");
		m.credits = "Written in collaboration with SpecialEffect";
	}
	
	public static void setAsParent(FMLPreInitializationEvent event,
									String parentModID) {
		ModMetadata m = event.getModMetadata(); 		
		m.parent = parentModID;
    }
	
	// Get the x, y point corresponding to one of 8 compass points
	// 0 = N, 1 = NE, 2 = E, etc ...
	public static Point getCompassPoint(int i) {
		Point p = new Point(0,  0);	
		i = i % 8;
		switch (i) {
		case 0:
			p.setLocation(0, +1);
			break;
		case 1:
			p.setLocation(+1, +1);
			break;
		case 2:
			p.setLocation(+1, 0);
			break;
		case 3:
			p.setLocation(+1, -1);
			break;
		case 4:
			p.setLocation(0, -1);
			break;
		case 5:
			p.setLocation(-1, -1);
			break;
		case 6:
			p.setLocation(-1, 0);
			break;
		default:
			p.setLocation(-1, +1);
			break;
		}
		return p;
	}
	
	public static Point getScaledDisplaySize(Minecraft mc) {
		Point p = new Point(0,  0);	
		
		ScaledResolution res = new ScaledResolution( mc );
		p.setLocation(res.getScaledWidth(), res.getScaledHeight());
		
		return p;
		
	}

	public static void drawTexQuad(double x, double y, double width, double height) {

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		// Ugh, these methods get proper names in forge 1.9
		worldrenderer.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181707_g); // 2nd arg = DefaultVertexFormats.POSITION_TEX

		// = worldrenderer.pos( ... ).tex( ... ).endVertex() 
		worldrenderer.func_181662_b(x        , y + height, 0).func_181673_a(0.0, 1.0).func_181675_d();
		worldrenderer.func_181662_b(x + width, y + height, 0).func_181673_a(1.0, 1.0).func_181675_d();
		worldrenderer.func_181662_b(x + width, y         , 0).func_181673_a(1.0, 0.0).func_181675_d();
		worldrenderer.func_181662_b(x        , y         , 0).func_181673_a(0.0, 0.0).func_181675_d();

		tessellator.draw();

	}
}

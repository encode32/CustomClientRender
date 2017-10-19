package net.encode.customclientrender;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import com.wurmonline.client.renderer.gui.HeadsUpDisplay;

import javassist.ClassPool;
import javassist.CtClass;

public class CustomClientRenderMod implements WurmClientMod, Initable, PreInitable, Configurable {
	public static HeadsUpDisplay hud;
	
	public static Logger logger = Logger.getLogger("CustomClientRenderMod");
	
	private static boolean renderWeather = true;
	private static boolean renderSky = true;
	private static boolean renderTerrain = true;
	private static boolean renderCave = true;
	private static boolean renderWater = true;
	private static boolean renderParticles = true;
	private static boolean renderGrass = true;
	private static boolean renderTrees = true;
	
	public static boolean handleInput(final String cmd, final String[] data) {
		if (cmd.toLowerCase().equals("toggleweather")) {
			renderWeather = !renderWeather;
			hud.consoleOutput("[CustomClientRenderMod] renderWeather: " + Boolean.toString(renderWeather));
			return true;
		}
		if (cmd.toLowerCase().equals("togglesky")) {
			renderSky = !renderSky;
			hud.consoleOutput("[CustomClientRenderMod] renderSky: " + Boolean.toString(renderSky));
			return true;
		}
		if (cmd.toLowerCase().equals("toggleterrain")) {
			renderTerrain = !renderTerrain;
			hud.consoleOutput("[CustomClientRenderMod] renderTerrain: " + Boolean.toString(renderTerrain));
			return true;
		}
		if (cmd.toLowerCase().equals("togglecave")) {
			renderCave = !renderCave;
			hud.consoleOutput("[CustomClientRenderMod] renderCave: " + Boolean.toString(renderCave));
			return true;
		}
		if (cmd.toLowerCase().equals("togglewater")) {
			renderWater = !renderWater;
			hud.consoleOutput("[CustomClientRenderMod] renderWater: " + Boolean.toString(renderWater));
			return true;
		}
		if (cmd.toLowerCase().equals("toggleparticles")) {
			renderParticles = !renderParticles;
			hud.consoleOutput("[CustomClientRenderMod] renderParticles: " + Boolean.toString(renderParticles));
			return true;
		}
		if (cmd.toLowerCase().equals("togglegrass")) {
			renderGrass = !renderGrass;
			hud.consoleOutput("[CustomClientRenderMod] renderGrass: " + Boolean.toString(renderGrass));
			return true;
		}
		if (cmd.toLowerCase().equals("toggletrees")) {
			renderTrees = !renderTrees;
			hud.consoleOutput("[CustomClientRenderMod] renderTrees: " + Boolean.toString(renderTrees));
			return true;
		}
		return false;
	}
	
	@Override
	public void configure(Properties properties) {
		renderWeather = Boolean.valueOf(properties.getProperty("renderWeather", Boolean.toString(renderWeather)));
		renderSky = Boolean.valueOf(properties.getProperty("renderSky", Boolean.toString(renderSky)));
		renderTerrain = Boolean.valueOf(properties.getProperty("renderTerrain", Boolean.toString(renderTerrain)));
		renderCave = Boolean.valueOf(properties.getProperty("renderCave", Boolean.toString(renderCave)));
		renderWater = Boolean.valueOf(properties.getProperty("renderWater", Boolean.toString(renderWater)));
		renderParticles = Boolean.valueOf(properties.getProperty("renderParticles", Boolean.toString(renderParticles)));
		renderGrass = Boolean.valueOf(properties.getProperty("renderGrass", Boolean.toString(renderGrass)));
		renderTrees = Boolean.valueOf(properties.getProperty("renderTrees", Boolean.toString(renderTrees)));
	}
	
	@Override
	public void preInit() {
		
	}
	
	@Override
	public void init() {
		logger.log(Level.INFO, "Initializing CustomClientRenderMod");
		try {
			ClassPool classPool = HookManager.getInstance().getClassPool();

			CtClass ctWurmConsole = classPool.getCtClass("com.wurmonline.client.console.WurmConsole");
			ctWurmConsole.getMethod("handleDevInput", "(Ljava/lang/String;[Ljava/lang/String;)Z")
					.insertBefore("if (net.encode.customclientrender.CustomClientRenderMod.handleInput($1,$2)) return true;");
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.gui.HeadsUpDisplay", "init", "(II)V",
					() -> (proxy, method, args) -> {
						method.invoke(proxy, args);
						hud = (HeadsUpDisplay) proxy;
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.terrain.weather.Weather", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderWeather)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.terrain.sky.SkyRenderer", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderSky)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.terrain.TerrainRenderer", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderTerrain)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.cave.CaveRender", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderCave)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.terrain.WaterRenderer", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderWater)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.particles.ParticleRenderer", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderParticles)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.terrain.decorator.DecorationRenderer", "execute",
					"(Ljava/lang/Object;)V", () -> (proxy, method, args) -> {
						
						if(renderGrass)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
			
			HookManager.getInstance().registerHook("com.wurmonline.client.renderer.cell.SurfaceCell", "renderTrees",
					"(Lcom/wurmonline/client/renderer/backend/Queue;Lcom/wurmonline/client/renderer/Frustum;Z)V", () -> (proxy, method, args) -> {
						
						if(renderTrees)
						{
							method.invoke(proxy, args);
						}
						
						return null;
					});
		} catch (Throwable e) {
			logger.log(Level.SEVERE, "Error loading mod CustomClientRenderMod", e.getMessage());
		}
		
	}
}

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
	
	private static boolean renderGrass = true;
	
	private static boolean renderTrees = true;
	
	public static boolean handleInput(final String cmd, final String[] data) {
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

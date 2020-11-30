package me.arcanox.multipart;

import me.arcanox.multipart.client.renderers.MultipartBlockSelectionRenderer;
import me.arcanox.multipart.client.renderers.MultipartContainerTileRenderer;
import me.arcanox.multipart.common.blocks.Blocks;
import me.arcanox.multipart.common.capabilities.Capabilities;
import me.arcanox.multipart.common.capabilities.VanillaMultipartHandler;
import me.arcanox.multipart.common.events.MultipartBlockEventHandler;
import me.arcanox.multipart.common.tiles.TileEntities;
import me.arcanox.multipart.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod( MultipartMod.MOD_ID )
public class MultipartMod {
	public static final String MOD_ID   = "mcmultipart";
	public static final String MOD_NAME = "Minecraft Multipart";
	
	public MultipartMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		// Subscribe to common setup event
		eventBus.addListener( this::onCommonSetup );
		
		// Subscribe to client setup event
		DistExecutor.unsafeRunWhenOn( Dist.CLIENT, () -> () -> {
			ClientSetup clientSetup = new ClientSetup();
			
			eventBus.addListener( clientSetup::onClientSetup );
		} );
		
		Blocks.register( eventBus );
		TileEntities.register( eventBus );
	}
	
	private void onCommonSetup( final FMLCommonSetupEvent event ) {
		Log.info( "Beginning common setup for " + MOD_ID );
		
		Capabilities.register();
		MinecraftForge.EVENT_BUS.register( new VanillaMultipartHandler() );
		MinecraftForge.EVENT_BUS.register( new MultipartBlockEventHandler() );
		
		Log.info( "Common setup for " + MOD_ID + " is complete" );
	}
	
	static class ClientSetup {
		private void onClientSetup( final FMLClientSetupEvent event ) {
			Log.info( "Beginning client setup for " + MOD_ID );
			
			// Register TESRs
			ClientRegistry.bindTileEntityRenderer( TileEntities.MULTIPART_CONTAINER.get(), MultipartContainerTileRenderer::new );
			
			// Set up Multipart outline renderer
			MinecraftForge.EVENT_BUS.register( new MultipartBlockSelectionRenderer() );
			
			Log.info( "Client setup for " + MOD_ID + " is complete" );
		}
	}
}

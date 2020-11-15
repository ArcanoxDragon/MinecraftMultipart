package me.arcanox.multipart;

import me.arcanox.multipart.common.blocks.Blocks;
import me.arcanox.multipart.util.Log;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
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
		
		eventBus.addListener( this::onCommonInit );
		
		Blocks.register( eventBus );
	}
	
	private void onCommonInit( final FMLCommonSetupEvent event ) {
		Log.info( "Beginning common setup for " + MOD_ID );
		
		Log.info( "Common setup for " + MOD_ID + " is complete" );
	}
	
	@Mod.EventBusSubscriber( value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD )
	public static class ClientEventSubscriber {
		private void onClientInit( final FMLClientSetupEvent event ) {
			Log.info( "Beginning client setup for " + MOD_ID );
			
			Log.info( "Client setup for " + MOD_ID + " is complete" );
		}
	}
}

package me.arcanox.multipart.common.tiles;

import me.arcanox.multipart.MultipartMod;
import me.arcanox.multipart.common.blocks.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TileEntities {
	public static final TileEntityType<MultipartContainerTileEntity> MULTIPART_CONTAINER =
			TileEntityType.Builder.create( MultipartContainerTileEntity::new, Blocks.MULTIPART_CONTAINER.get() )
			                      .build( null );
	
	public static void registerTileEntities( RegistryEvent.Register<TileEntityType<?>> event ) {
		MULTIPART_CONTAINER.setRegistryName( MultipartMod.MOD_ID, "multipart_container" );
		
		event.getRegistry().register( MULTIPART_CONTAINER );
	}
}

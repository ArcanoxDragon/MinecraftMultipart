package me.arcanox.multipart.common.tiles;

import me.arcanox.multipart.MultipartMod;
import me.arcanox.multipart.api.Constants;
import me.arcanox.multipart.common.blocks.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public
class TileEntities {
	private static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create( ForgeRegistries.TILE_ENTITIES, MultipartMod.MOD_ID );
	
	public static final RegistryObject<TileEntityType<MultipartContainerTileEntity>> MULTIPART_CONTAINER = REGISTRY.register(
		Constants.Blocks.MULTIPART_CONTAINER,
		() -> TileEntityType.Builder.create( MultipartContainerTileEntity::new, Blocks.MULTIPART_CONTAINER.get() ).build( null )
	);
	
	public static void register( IEventBus eventBus ) {
		REGISTRY.register( eventBus );
	}
}

package me.arcanox.multipart.common.blocks;

import me.arcanox.multipart.MultipartMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create( ForgeRegistries.BLOCKS, MultipartMod.MOD_ID );
	
	public static final RegistryObject<MultipartContainerBlock> MULTIPART_CONTAINER = BLOCKS.register(
		"multipart_container",
		() -> new MultipartContainerBlock( Block.Properties.create( Material.MISCELLANEOUS ).noDrops().notSolid() ) );
	
	public static void register( IEventBus eventBus ) {
		BLOCKS.register( eventBus );
	}
}

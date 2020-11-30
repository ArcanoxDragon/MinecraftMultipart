package me.arcanox.multipart.common.capabilities;

import me.arcanox.multipart.MultipartMod;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VanillaMultipartProvider implements ICapabilityProvider {
	public static final ResourceLocation PROVIDER_KEY = new ResourceLocation( MultipartMod.MOD_ID, "vanilla_provider" );
	
	private final VanillaMultipartPlacer placer = new VanillaMultipartPlacer();
	
	private final LazyOptional<VanillaMultipartPlacer> placerValue = LazyOptional.of( () -> placer );
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {
		if ( cap == Capabilities.MULTIPART_PLACER ) {
			return placerValue.cast();
		}
		
		return LazyOptional.empty();
	}
}

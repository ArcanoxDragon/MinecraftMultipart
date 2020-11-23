package me.arcanox.multipart.common.capabilities;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VanillaMultipartProvider implements ICapabilityProvider {
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

package me.arcanox.multipart.api.util;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class ShapeHelper {
	public static boolean canShapesCoexist( VoxelShape first, VoxelShape... others ) {
		if ( first.isEmpty() )
			return true;
		
		VoxelShape combined = VoxelShapes.empty();
		
		for ( VoxelShape shape : others ) {
			combined = VoxelShapes.combineAndSimplify( combined, shape, IBooleanFunction.OR );
		}
		
		if ( combined.isEmpty() )
			return true;
		
		// Now get the intersection of the combined shape and the shape in question. If it has any volume, the shape won't fit.
		VoxelShape intersection = VoxelShapes.combineAndSimplify( first, combined, IBooleanFunction.AND );
		
		return intersection.isEmpty();
	}
}

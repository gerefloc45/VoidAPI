package com.gerefloc45.voidapi.api.goap.actions;

import com.gerefloc45.voidapi.api.BehaviorContext;
import com.gerefloc45.voidapi.api.goap.Action;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Built-in GOAP action to move to a target position.
 * 
 * <p>This action uses Minecraft's navigation system to pathfind
 * to a target location stored in the world state.
 * 
 * <p><b>Preconditions:</b>
 * <ul>
 *   <li>"targetPosition" (Vec3d or BlockPos) - Must exist in world state</li>
 * </ul>
 * 
 * <p><b>Effects:</b>
 * <ul>
 *   <li>"atTargetPosition" (Boolean) - Set to true when reached</li>
 * </ul>
 * 
 * @since 0.4.0
 */
public class MoveToPositionAction extends Action {
    private final String positionKey;
    private final double acceptableDistance;
    private Vec3d targetPos;
    
    /**
     * Creates a new move to position action.
     * 
     * @param positionKey The blackboard key for target position
     * @param acceptableDistance How close to get to target
     * @param cost The action cost
     */
    public MoveToPositionAction(String positionKey, double acceptableDistance, float cost) {
        super("MoveToPosition", cost);
        this.positionKey = positionKey;
        this.acceptableDistance = acceptableDistance;
        
        // Precondition: need target position
        preconditions.set(positionKey, true);
        
        // Effect: will be at target position
        effects.set("atTargetPosition", true);
    }
    
    /**
     * Creates a new move action with default parameters.
     * 
     * @param positionKey The position key
     */
    public MoveToPositionAction(String positionKey) {
        this(positionKey, 2.0, 1.0f);
    }
    
    @Override
    public boolean canRun(BehaviorContext context) {
        Object pos = context.getBlackboard().get(positionKey).orElse(null);
        return pos instanceof Vec3d || pos instanceof BlockPos;
    }
    
    @Override
    public void onStart(BehaviorContext context) {
        super.onStart(context);
        
        Object pos = context.getBlackboard().get(positionKey).orElse(null);
        if (pos instanceof Vec3d) {
            targetPos = (Vec3d) pos;
        } else if (pos instanceof BlockPos) {
            BlockPos blockPos = (BlockPos) pos;
            targetPos = new Vec3d(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        }
    }
    
    @Override
    public boolean execute(BehaviorContext context) {
        if (targetPos == null) {
            return false; // Failed
        }
        
        if (!(context.getEntity() instanceof MobEntity)) {
            return false; // Only works with MobEntity
        }
        
        MobEntity mob = (MobEntity) context.getEntity();
        Vec3d currentPos = mob.getPos();
        double distance = currentPos.distanceTo(targetPos);
        
        if (distance <= acceptableDistance) {
            // Reached target
            return true;
        }
        
        // Continue moving
        if (mob.getNavigation().isIdle()) {
            mob.getNavigation().startMovingTo(
                targetPos.x, targetPos.y, targetPos.z, 1.0
            );
        }
        
        return false; // Still moving
    }
    
    @Override
    public float getProceduralCost(BehaviorContext context, com.gerefloc45.voidapi.api.goap.WorldState currentState) {
        // Cost based on distance
        Object pos = context.getBlackboard().get(positionKey).orElse(null);
        if (pos instanceof Vec3d) {
            Vec3d target = (Vec3d) pos;
            double distance = context.getEntity().getPos().distanceTo(target);
            return getCost() * (float)(distance / 10.0); // Scale by distance
        }
        return getCost();
    }
}

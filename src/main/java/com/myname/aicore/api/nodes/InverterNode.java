package com.myname.aicore.api.nodes;

import com.myname.aicore.api.Behavior;
import com.myname.aicore.api.BehaviorContext;

/**
 * Inverter node - inverts the result of its child behavior.
 * SUCCESS becomes FAILURE and vice versa.
 * RUNNING remains RUNNING.
 * 
 * @author AI-Core Framework
 * @version 1.1.0
 */
public class InverterNode implements Behavior {
    private final Behavior child;

    /**
     * Creates a new inverter node.
     *
     * @param child The child behavior to invert
     */
    public InverterNode(Behavior child) {
        this.child = child;
    }

    @Override
    public Status execute(BehaviorContext context) {
        Status childStatus = child.execute(context);
        
        switch (childStatus) {
            case SUCCESS:
                return Status.FAILURE;
            case FAILURE:
                return Status.SUCCESS;
            case RUNNING:
                return Status.RUNNING;
            default:
                return Status.FAILURE;
        }
    }

    @Override
    public void onStart(BehaviorContext context) {
        child.onStart(context);
    }

    @Override
    public void onEnd(BehaviorContext context, Status status) {
        // Invert status for child callback
        Status invertedStatus = status == Status.SUCCESS ? Status.FAILURE : 
                               status == Status.FAILURE ? Status.SUCCESS : status;
        child.onEnd(context, invertedStatus);
    }
}

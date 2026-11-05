package dev.majorasblock.player;

public final class FlipMovementController {
    public enum Direction { FRONT, BACK, LEFT, RIGHT }
    public record Vector(double x, double z) {}
    private FlipMovementController() {}

    public static Vector direction(Direction direction, float yawDegrees) {
        if (direction == null || !Float.isFinite(yawDegrees)) {
            throw new IllegalArgumentException("A valid flip direction and yaw are required.");
        }
        double yaw = Math.toRadians(yawDegrees);
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);
        double rightX = Math.cos(yaw);
        double rightZ = Math.sin(yaw);
        return switch (direction) {
            case FRONT -> new Vector(forwardX, forwardZ);
            case BACK -> new Vector(-forwardX, -forwardZ);
            case LEFT -> new Vector(-rightX, -rightZ);
            case RIGHT -> new Vector(rightX, rightZ);
        };
    }
}

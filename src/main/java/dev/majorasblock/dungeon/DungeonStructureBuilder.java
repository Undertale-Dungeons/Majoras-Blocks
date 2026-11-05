package dev.majorasblock.dungeon;

import dev.majorasblock.content.DungeonCatalog;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Small procedural test-build for dungeon shells.
 * It only fills air, so it will not overwrite existing terrain or builds.
 */
public final class DungeonStructureBuilder {
    public record BuildResult(String dungeonName, BlockPos origin, int blocksPlaced) {}

    private record Palette(Block floor, Block wall, Block trim, Block light, Block bossMark) {}

    private DungeonStructureBuilder() {}

    public static BuildResult build(ServerLevel level, BlockPos requestedOrigin, String dungeonId) {
        DungeonCatalog.Dungeon dungeon = DungeonCatalog.byId(dungeonId);
        if (dungeon == null) {
            throw new IllegalArgumentException("Unknown dungeon id: " + dungeonId);
        }

        Palette palette = palette(dungeonId);
        BlockPos origin = requestedOrigin.offset(8, 0, 8);
        Counter counter = new Counter();

        // Three puzzle rooms and a larger boss chamber.
        room(level, origin, 0, 0, 11, palette, counter, true);
        room(level, origin, 16, 0, 11, palette, counter, true);
        room(level, origin, 0, 16, 11, palette, counter, true);
        room(level, origin, 16, 16, 17, palette, counter, false);

        corridor(level, origin, 10, 4, 16, 3, palette, counter, true);
        corridor(level, origin, 4, 10, 3, 6, palette, counter, false);
        corridor(level, origin, 20, 10, 3, 6, palette, counter, false);
        corridor(level, origin, 4, 26, 16, 3, palette, counter, true);

        // Puzzle pedestals in the three smaller chambers.
        pedestal(level, origin.offset(5, 0, 5), palette, counter);
        pedestal(level, origin.offset(21, 0, 5), palette, counter);
        pedestal(level, origin.offset(5, 0, 21), palette, counter);

        // A distinct boss dais and four corner lights.
        int bossCenterX = origin.getX() + 24;
        int bossCenterZ = origin.getZ() + 24;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (Math.abs(dx) == 3 || Math.abs(dz) == 3) {
                    place(level, new BlockPos(bossCenterX + dx, origin.getY() - 1,
                            bossCenterZ + dz), palette.bossMark().defaultBlockState(), counter);
                }
            }
        }
        place(level, new BlockPos(bossCenterX, origin.getY(), bossCenterZ),
                palette.trim().defaultBlockState(), counter);

        return new BuildResult(dungeon.name(), origin, counter.value);
    }

    private static void room(ServerLevel level, BlockPos origin, int offsetX, int offsetZ,
                             int size, Palette palette, Counter counter, boolean hasDoor) {
        int wallHeight = size > 12 ? 7 : 5;
        int minX = origin.getX() + offsetX;
        int minZ = origin.getZ() + offsetZ;

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                int worldX = minX + x;
                int worldZ = minZ + z;
                place(level, new BlockPos(worldX, origin.getY() - 1, worldZ),
                        palette.floor().defaultBlockState(), counter);

                boolean edge = x == 0 || z == 0 || x == size - 1 || z == size - 1;
                if (edge) {
                    boolean doorway = hasDoor && ((x == size / 2 && (z == 0 || z == size - 1))
                            || (z == size / 2 && (x == 0 || x == size - 1)));
                    if (!doorway) {
                        for (int y = 0; y < wallHeight; y++) {
                            Block block = y == wallHeight - 1 ? palette.trim() : palette.wall();
                            place(level, new BlockPos(worldX, origin.getY() + y, worldZ),
                                    block.defaultBlockState(), counter);
                        }
                    }
                }
            }
        }

        int roofY = origin.getY() + wallHeight;
        for (int x = 1; x < size - 1; x++) {
            for (int z = 1; z < size - 1; z++) {
                place(level, new BlockPos(minX + x, roofY, minZ + z),
                        palette.wall().defaultBlockState(), counter);
            }
        }

        int mid = size / 2;
        place(level, new BlockPos(minX + mid, origin.getY(), minZ + 1),
                palette.light().defaultBlockState(), counter);
        place(level, new BlockPos(minX + mid, origin.getY(), minZ + size - 2),
                palette.light().defaultBlockState(), counter);
    }

    private static void corridor(ServerLevel level, BlockPos origin, int offsetX, int offsetZ,
                                 int width, int length, Palette palette, Counter counter,
                                 boolean alongX) {
        for (int a = 0; a < length; a++) {
            for (int b = 0; b < width; b++) {
                int x = offsetX + (alongX ? a : b);
                int z = offsetZ + (alongX ? b : a);
                place(level, origin.offset(x, -1, z), palette.floor().defaultBlockState(), counter);
                if (b == 0 || b == width - 1) {
                    for (int y = 0; y < 4; y++) {
                        place(level, origin.offset(x, y, z), palette.wall().defaultBlockState(), counter);
                    }
                }
                place(level, origin.offset(x, 4, z), palette.trim().defaultBlockState(), counter);
            }
        }
    }

    private static void pedestal(ServerLevel level, BlockPos base, Palette palette, Counter counter) {
        place(level, base, palette.trim().defaultBlockState(), counter);
        place(level, base.above(), palette.bossMark().defaultBlockState(), counter);
        place(level, base.above(2), palette.light().defaultBlockState(), counter);
    }

    private static void place(ServerLevel level, BlockPos pos, BlockState state, Counter counter) {
        if (level.isEmptyBlock(pos) && level.setBlockAndUpdate(pos, state)) {
            counter.value++;
        }
    }

    private static Palette palette(String id) {
        return switch (id) {
            case "glassroot_catacombs" -> new Palette(Blocks.MOSSY_STONE_BRICKS,
                    Blocks.MOSSY_COBBLESTONE, Blocks.AMETHYST_BLOCK, Blocks.GLOWSTONE, Blocks.EMERALD_BLOCK);
            case "emberwake_foundry" -> new Palette(Blocks.POLISHED_BASALT,
                    Blocks.DEEPSLATE_BRICKS, Blocks.COPPER_BLOCK, Blocks.LANTERN, Blocks.MAGMA_BLOCK);
            case "tideglass_observatory" -> new Palette(Blocks.PRISMARINE_BRICKS,
                    Blocks.DARK_PRISMARINE, Blocks.SEA_LANTERN, Blocks.SEA_LANTERN, Blocks.DIAMOND_BLOCK);
            case "starfall_bastion" -> new Palette(Blocks.END_STONE_BRICKS,
                    Blocks.PURPUR_BLOCK, Blocks.AMETHYST_BLOCK, Blocks.END_ROD, Blocks.LAPIS_BLOCK);
            case "clockwork_reliquary" -> new Palette(Blocks.TUFF_BRICKS,
                    Blocks.POLISHED_TUFF, Blocks.COPPER_BLOCK, Blocks.SOUL_LANTERN, Blocks.COPPER_BLOCK);
            case "woodfall" -> new Palette(Blocks.MOSSY_STONE_BRICKS,
                    Blocks.MOSSY_COBBLESTONE, Blocks.MOSS_BLOCK, Blocks.GLOWSTONE, Blocks.EMERALD_BLOCK);
            case "snowhead" -> new Palette(Blocks.PACKED_ICE,
                    Blocks.BLUE_ICE, Blocks.IRON_BLOCK, Blocks.SEA_LANTERN, Blocks.DIAMOND_BLOCK);
            case "great_bay" -> new Palette(Blocks.PRISMARINE_BRICKS,
                    Blocks.PRISMARINE, Blocks.SEA_LANTERN, Blocks.SEA_LANTERN, Blocks.DIAMOND_BLOCK);
            case "stone_tower" -> new Palette(Blocks.DEEPSLATE_BRICKS,
                    Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRYING_OBSIDIAN, Blocks.SOUL_LANTERN, Blocks.GOLD_BLOCK);
            default -> throw new IllegalArgumentException("No palette for dungeon: " + id);
        };
    }

    private static final class Counter {
        private int value;
    }
}

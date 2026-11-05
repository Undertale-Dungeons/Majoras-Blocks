package dev.majorasblock.dungeon;

import dev.majorasblock.content.BossCatalog;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

/** Six buildable world landmarks and themed memorial arenas for the nine bosses. */
public final class LandmarkBuilder {
    public record BuildResult(String name, BlockPos origin, int blocksPlaced) {}
    private record Palette(Block floor, Block wall, Block trim, Block light) {}
    private static final Map<String,String> LANDMARKS=Map.of(
        "deity_fountain","Deity Fountain","moon_gate","Moon Gate","clock_tower","Clock Town Clock Tower",
        "peabert_grove","Peabert Grove","sponge_grotto","Sponge Grotto","starfall_observatory","Starfall Observatory"
    );
    private LandmarkBuilder() {}
    public static List<String> ids(){return LANDMARKS.keySet().stream().sorted().toList();}

    public static BuildResult build(ServerLevel level,BlockPos requested,String id){
        String name=LANDMARKS.get(id);
        if(name==null) throw new IllegalArgumentException("Unknown landmark: "+id);
        BlockPos o=requested.offset(10,0,10); Counter c=new Counter();
        switch(id){
            case "deity_fountain" -> fountain(level,o,c);
            case "moon_gate" -> moonGate(level,o,c);
            case "clock_tower" -> clockTower(level,o,c);
            case "peabert_grove" -> grove(level,o,c);
            case "sponge_grotto" -> grotto(level,o,c);
            case "starfall_observatory" -> observatory(level,o,c);
            default -> throw new IllegalStateException("Missing landmark builder: "+id);
        }
        return new BuildResult(name,o,c.n);
    }

    public static BuildResult buildBossMemorial(ServerLevel level,BlockPos requested,String bossId){
        BossCatalog.Boss boss=BossCatalog.byId(bossId)
                .orElseThrow(()->new IllegalArgumentException("Unknown boss: "+bossId));
        Palette p=palette(boss.dungeonId()); BlockPos o=requested.offset(10,0,10); Counter c=new Counter();
        disk(level,o,9,p.floor(),c);
        for(int[] d:new int[][]{{-7,0},{7,0},{0,-7},{0,7}})
            pillar(level,o.offset(d[0],0,d[1]),7,p.wall(),p.trim(),c);
        disk(level,o.above(),3,p.trim(),c);
        place(level,o.above(2),Blocks.AMETHYST_BLOCK.defaultBlockState(),c);
        place(level,o.above(3),p.light().defaultBlockState(),c);
        place(level,o.above(4),Blocks.GOLD_BLOCK.defaultBlockState(),c);
        return new BuildResult(boss.name()+" Memorial Arena",o,c.n);
    }

    private static void fountain(ServerLevel l,BlockPos o,Counter c){
        disk(l,o,8,Blocks.MOSSY_STONE_BRICKS,c);
        disk(l,o,4,Blocks.PRISMARINE_BRICKS,c);
        for(int x=-3;x<=3;x++)for(int z=-3;z<=3;z++)if(x*x+z*z<=9)
            place(l,o.offset(x,1,z),Blocks.WATER.defaultBlockState(),c);
        for(int i=0;i<8;i++){double a=i*Math.PI/4;int x=(int)Math.round(Math.cos(a)*6),z=(int)Math.round(Math.sin(a)*6);
            pillar(l,o.offset(x,0,z),3,Blocks.AMETHYST_BLOCK,Blocks.GLOWSTONE,c);}
        pillar(l,o,5,Blocks.PRISMARINE_BRICKS,Blocks.SEA_LANTERN,c);
    }
    private static void moonGate(ServerLevel l,BlockPos o,Counter c){
        disk(l,o,7,Blocks.DEEPSLATE_BRICKS,c);
        for(int x=-4;x<=4;x++){place(l,o.offset(x,1,0),Blocks.OBSIDIAN.defaultBlockState(),c);
            place(l,o.offset(x,10,0),Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(),c);}
        for(int y=1;y<=10;y++)for(int x:new int[]{-4,4})place(l,o.offset(x,y,0),Blocks.OBSIDIAN.defaultBlockState(),c);
        for(int x=-3;x<=3;x++)for(int y=2;y<=9;y++)place(l,o.offset(x,y,0),Blocks.GLASS.defaultBlockState(),c);
        place(l,o.offset(-5,1,0),Blocks.SOUL_LANTERN.defaultBlockState(),c);
        place(l,o.offset(5,1,0),Blocks.SOUL_LANTERN.defaultBlockState(),c);
    }
    private static void clockTower(ServerLevel l,BlockPos o,Counter c){
        disk(l,o,8,Blocks.COBBLED_DEEPSLATE,c);
        for(int y=0;y<17;y++)for(int x:new int[]{-5,5})for(int z:new int[]{-5,5})
            place(l,o.offset(x,y,z),Blocks.IRON_BLOCK.defaultBlockState(),c);
        for(int x=-5;x<=5;x++)for(int y=0;y<17;y++){
            place(l,o.offset(x,y,-5),Blocks.DEEPSLATE_BRICKS.defaultBlockState(),c);
            place(l,o.offset(x,y,5),Blocks.DEEPSLATE_BRICKS.defaultBlockState(),c);}
        for(int x=-4;x<=4;x++)for(int z=-4;z<=4;z++)if(Math.abs(x)==4||Math.abs(z)==4)
            place(l,o.offset(x,17,z),Blocks.GOLD_BLOCK.defaultBlockState(),c);
        for(int x=-2;x<=2;x++)for(int y=8;y<=12;y++)
            place(l,o.offset(x,y,-6),Blocks.GLASS.defaultBlockState(),c);
        place(l,o.offset(0,10,-7),Blocks.GOLD_BLOCK.defaultBlockState(),c);
        place(l,o.offset(0,18,0),Blocks.AMETHYST_BLOCK.defaultBlockState(),c);
    }
    private static void grove(ServerLevel l,BlockPos o,Counter c){
        disk(l,o,9,Blocks.MOSS_BLOCK,c);
        for(int i=0;i<7;i++){int x=(int)Math.round(Math.cos(i*Math.PI*2/7)*6),z=(int)Math.round(Math.sin(i*Math.PI*2/7)*6);
            for(int y=0;y<6;y++)place(l,o.offset(x,y,z),Blocks.OAK_LOG.defaultBlockState(),c);
            for(int dx=-2;dx<=2;dx++)for(int dz=-2;dz<=2;dz++)
                place(l,o.offset(x+dx,6,z+dz),Blocks.OAK_LEAVES.defaultBlockState(),c);}
        pillar(l,o,3,Blocks.MOSSY_COBBLESTONE,Blocks.GLOWSTONE,c);
        place(l,o.above(3),Blocks.PUMPKIN.defaultBlockState(),c);
    }
    private static void grotto(ServerLevel l,BlockPos o,Counter c){
        disk(l,o,9,Blocks.PRISMARINE_BRICKS,c);
        for(int x=-7;x<=7;x++)for(int z=-7;z<=7;z++)if(x*x+z*z<=49)
            place(l,o.offset(x,1,z),Blocks.WATER.defaultBlockState(),c);
        for(int i=0;i<8;i++){double a=i*Math.PI/4;int x=(int)Math.round(Math.cos(a)*7),z=(int)Math.round(Math.sin(a)*7);
            pillar(l,o.offset(x,0,z),6,Blocks.DARK_PRISMARINE,Blocks.SEA_LANTERN,c);}
        for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++)place(l,o.offset(x,2,z),Blocks.SPONGE.defaultBlockState(),c);
    }
    private static void observatory(ServerLevel l,BlockPos o,Counter c){
        disk(l,o,9,Blocks.END_STONE_BRICKS,c);
        for(int y=0;y<7;y++)for(int x=-7;x<=7;x++)for(int z=-7;z<=7;z++){
            int d=x*x+z*z;if(d>=38&&d<=50&&(y==0||y==6||(x%3==0&&z%3==0)))
                place(l,o.offset(x,y,z),Blocks.PURPUR_BLOCK.defaultBlockState(),c);}
        for(int[] d:new int[][]{{-6,0},{6,0},{0,-6},{0,6}})
            pillar(l,o.offset(d[0],0,d[1]),8,Blocks.PURPUR_PILLAR,Blocks.END_ROD,c);
        pillar(l,o,3,Blocks.AMETHYST_BLOCK,Blocks.SEA_LANTERN,c);
    }
    private static Palette palette(String id){
        return switch(id){
            case "woodfall","glassroot_catacombs"->new Palette(Blocks.MOSSY_STONE_BRICKS,Blocks.MOSSY_COBBLESTONE,Blocks.AMETHYST_BLOCK,Blocks.GLOWSTONE);
            case "snowhead","emberwake_foundry"->new Palette(Blocks.POLISHED_BASALT,Blocks.DEEPSLATE_BRICKS,Blocks.RAW_COPPER_BLOCK,Blocks.LANTERN);
            case "great_bay","tideglass_observatory"->new Palette(Blocks.PRISMARINE_BRICKS,Blocks.DARK_PRISMARINE,Blocks.SEA_LANTERN,Blocks.SEA_LANTERN);
            case "stone_tower","starfall_bastion"->new Palette(Blocks.END_STONE_BRICKS,Blocks.PURPUR_BLOCK,Blocks.AMETHYST_BLOCK,Blocks.END_ROD);
            default->new Palette(Blocks.TUFF_BRICKS,Blocks.POLISHED_TUFF,Blocks.RAW_COPPER_BLOCK,Blocks.SOUL_LANTERN);
        };
    }
    private static void disk(ServerLevel l,BlockPos o,int r,Block b,Counter c){
        for(int x=-r;x<=r;x++)for(int z=-r;z<=r;z++)if(x*x+z*z<=r*r)place(l,o.offset(x,0,z),b.defaultBlockState(),c);
    }
    private static void pillar(ServerLevel l,BlockPos o,int h,Block wall,Block cap,Counter c){
        for(int y=0;y<h;y++)place(l,o.above(y),(y==h-1?cap:wall).defaultBlockState(),c);
        place(l,o.above(h),Blocks.LANTERN.defaultBlockState(),c);
    }
    private static void place(ServerLevel l,BlockPos p,BlockState s,Counter c){if(l.isEmptyBlock(p)&&l.setBlockAndUpdate(p,s))c.n++;}
    private static final class Counter{int n;}
}

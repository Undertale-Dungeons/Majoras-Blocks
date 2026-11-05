package dev.majorasblock.content;
import java.util.List;
/** Protective and traversal relics in the adventure collection. */
public final class RelicCatalog {
 public record Relic(String id,String name,String icon,MaskCatalog.Power power,String description) {}
 private static final List<Relic> RELICS=List.of(
 new Relic("explorers_lens","Explorer's Lens","spyglass",MaskCatalog.Power.VISION,"Improves sight in dark places"),
 new Relic("tideglass_flippers","Tideglass Flippers","prismarine_shard",MaskCatalog.Power.AQUATIC,"Helps with underwater exploration"),
 new Relic("moonstone_pendant","Moonstone Pendant","amethyst_shard",MaskCatalog.Power.RESILIENT,"A quiet protective keepsake"),
 new Relic("clockmakers_compass","Clockmaker's Compass","compass",MaskCatalog.Power.TRAVERSAL,"Keeps the wearer moving along"),
 new Relic("bloom_satchel","Bloom Satchel","moss_block",MaskCatalog.Power.RECOVERY,"A small garden in a traveler's bag"),
 new Relic("frostwalk_cloak","Frostwalk Cloak","white_wool",MaskCatalog.Power.RESILIENT,"Made for cold and difficult routes"),
 new Relic("starfall_boots","Starfall Boots","iron_boots",MaskCatalog.Power.TRAVERSAL,"Softens long drops and speeds travel"),
 new Relic("lantern_satchel","Lantern Satchel","lantern",MaskCatalog.Power.VISION,"Keeps a little light close"),
 new Relic("springstep_boots","Springstep Boots","rabbit_foot",MaskCatalog.Power.AGILITY,"A nimble step for climbing paths"),
 new Relic("wayfarer_charm","Wayfarer Charm","emerald",MaskCatalog.Power.BALANCED,"A lucky token for long journeys"),
 new Relic("quiet_bell","Quiet Bell","bell",MaskCatalog.Power.QUIET,"Softens the wearer's presence"),
 new Relic("peaberts_keepsake","Peabert's Keepsake","cookie",MaskCatalog.Power.RECOVERY,"A secret token from an unlikely friend"),
 new Relic("sponge_satchel","Sponge Satchel","sponge",MaskCatalog.Power.AQUATIC,"A handy relic for damp places"),
 new Relic("moonlit_map","Moonlit Map","map",MaskCatalog.Power.ECHO,"Helps the wearer notice forgotten details"),
 new Relic("moonlit_buckler","Moonlit Buckler","shield",MaskCatalog.Power.RESILIENT,"A protective charm that steadies its bearer"),
 new Relic("reefguard_ward","Reefguard Ward","prismarine_crystals",MaskCatalog.Power.AQUATIC,"A coastal ward for underwater routes"),
 new Relic("rootbound_vest","Rootbound Vest","leather_chestplate",MaskCatalog.Power.RECOVERY,"A woven vest that restores strength"),
 new Relic("stargazer_cloak","Stargazer Cloak","chainmail_chestplate",MaskCatalog.Power.VISION,"A star-threaded cloak for dim places"),
 new Relic("quietstep_boots","Quietstep Boots","leather_boots",MaskCatalog.Power.QUIET,"Soft steps through forgotten halls"),
 new Relic("springheel_gloves","Springheel Gloves","rabbit_foot",MaskCatalog.Power.AGILITY,"Light gloves for nimble exploration")
 );
 private RelicCatalog() {}
 public static List<Relic> all(){return RELICS;}
}

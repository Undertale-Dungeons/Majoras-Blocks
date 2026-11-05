package dev.majorasblock.content;

import java.util.List;

/** Friendly ambient creatures used to populate landmarks and safe exploration spaces. */
public final class WanderingCreatureCatalog {
    public record Creature(String id,String name,String vanillaType,String region,String behavior) {}
    private static final List<Creature> CREATURES=List.of(
        new Creature("moon_moth","Moon Moth","allay","Moon Expanse","drifts around lanterns"),
        new Creature("glasswing_sprite","Glasswing Sprite","bat","Verdant Scar","circles crystal gardens"),
        new Creature("peabert_companion","Peabert Companion","fox","Clock Town","wanders near old trees"),
        new Creature("marsh_frog","Marsh Frog","frog","Woodfall","rests beside shallow water"),
        new Creature("tide_turtle","Tide Turtle","turtle","Shifting Coast","meanders through pools"),
        new Creature("starfall_kit","Starfall Kit","cat","Starfall Reach","wanders beneath open skies"),
        new Creature("snowcap_goat","Snowcap Goat","goat","Snowhead","climbs rocky slopes"),
        new Creature("lantern_firefly","Lantern Firefly","allay","Quiet Meridian","floats around lights"),
        new Creature("orchard_bird","Orchard Bird","parrot","Verdant Scar","perches in tree canopies"),
        new Creature("clockwork_chick","Clockwork Chick","chicken","Quiet Meridian","pecks around old mechanisms"),
        new Creature("reef_axolotl","Reef Axolotl","axolotl","Great Bay","swims in clear water"),
        new Creature("cloud_rabbit","Cloud Rabbit","rabbit","Starfall Reach","hops through open fields"),
        new Creature("mossy_sheep","Mossy Sheep","sheep","Verdant Scar","grazes in quiet clearings"),
        new Creature("moon_camel","Moon Camel","camel","Moon Expanse","rests beneath the moon"),
        new Creature("deepwood_panda","Deepwood Panda","panda","Verdant Scar","wanders among bamboo"),
        new Creature("star_sniffer","Star Sniffer","sniffer","Starfall Reach","searches ancient ground"),
        new Creature("ember_mooshroom","Ember Mooshroom","mooshroom","Cinder March","grazes near warm stone"),
        new Creature("stone_armadillo","Stone Armadillo","armadillo","Cinder March","hides in rocky cover"),
        new Creature("tide_dolphin","Tide Dolphin","dolphin","Great Bay","plays in coastal waters"),
        new Creature("clocktown_pig","Clock Town Pig","pig","Clock Town","snuffles around the market"),
        new Creature("quiet_cow","Quiet Cow","cow","Quiet Meridian","wanders through meadows"),
        new Creature("silver_horse","Silver Horse","horse","Starfall Reach","rests near the observatory"),
        new Creature("traveler_donkey","Traveler Donkey","donkey","Clock Town","keeps to the old roads"),
        new Creature("deepwater_squid","Deepwater Squid","squid","Great Bay","drifts through dark water")
    );
    private WanderingCreatureCatalog(){}
    public static List<Creature> all(){return CREATURES;}
    public static Creature byId(String id){return CREATURES.stream().filter(c->c.id().equals(id)).findFirst().orElse(null);}
}

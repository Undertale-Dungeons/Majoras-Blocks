package dev.majorasblock.content;

import java.util.List;

/** The complete first mask collection: 54 original masks, including the three form masks. */
public final class MaskCatalog {
    public enum Power {
        DEKU_FORM, GORON_FORM, ZORA_FORM, TRAVERSAL, AQUATIC, RESILIENT,
        VISION, RECOVERY, AGILITY, QUIET, ECHO, BALANCED
    }
    public record Mask(String id, String name, String icon, Power power, String description) {}
    private static final List<Mask> MASKS = List.of(
        new Mask("deku_form_mask","Deku Form Mask","turtle_scute",Power.DEKU_FORM,"A light form for springing across gaps and drifting safely down"),
        new Mask("goron_form_mask","Goron Form Mask","magma_cream",Power.GORON_FORM,"A sturdy form for rough terrain and hot places"),
        new Mask("zora_form_mask","Zora Form Mask","prismarine_shard",Power.ZORA_FORM,"A water-ready form for exploring submerged places"),
        new Mask("sponge_mask","Sponge Mask","wet_sponge",Power.AQUATIC,"A curious mask made for long underwater routes"),
        new Mask("peabert_mask","Peabert Mask","cookie",Power.RECOVERY,"A tiny secret with a surprisingly calming presence"),
        new Mask("moon_eye_mask","Moon Eye Mask","ender_eye",Power.VISION,"Reveals the shape of distant places in the dark"),
        new Mask("bloom_mask","Bloom Mask","spore_blossom",Power.RECOVERY,"Carries the quiet strength of an old garden"),
        new Mask("frost_mask","Frost Mask","packed_ice",Power.RESILIENT,"Keeps its wearer steady in harsh conditions"),
        new Mask("tide_mask","Tide Mask","heart_of_the_sea",Power.AQUATIC,"Follows the pull of hidden currents"),
        new Mask("ember_mask","Ember Mask","blaze_powder",Power.RESILIENT,"Holds a spark from the Cinder March"),
        new Mask("starfall_mask","Starfall Mask","amethyst_shard",Power.TRAVERSAL,"Moves lightly beneath the night sky"),
        new Mask("clockwork_mask","Clockwork Mask","clock",Power.BALANCED,"A measured rhythm helps the wearer keep pace"),
        new Mask("mirror_mask","Mirror Mask","glass",Power.ECHO,"Reflects faint traces left by nearby places"),
        new Mask("lantern_mask","Lantern Mask","lantern",Power.VISION,"A warm glow seems to follow the wearer"),
        new Mask("bell_mask","Bell Mask","bell",Power.ECHO,"Makes distant details easier to notice"),
        new Mask("echo_mask","Echo Mask","echo_shard",Power.ECHO,"Listens for the quiet spaces between sounds"),
        new Mask("sun_mask","Sun Mask","sunflower",Power.RESILIENT,"Carries a little warmth into cold places"),
        new Mask("wind_mask","Wind Mask","feather",Power.TRAVERSAL,"A gentle breeze lifts every step"),
        new Mask("stone_mask","Stone Mask","stone",Power.RESILIENT,"Helps the wearer stay grounded"),
        new Mask("glass_mask","Glass Mask","glass_pane",Power.VISION,"Sharpens sight in dim corridors"),
        new Mask("reef_mask","Reef Mask","prismarine_crystals",Power.AQUATIC,"Belongs among reefs and quiet pools"),
        new Mask("root_mask","Root Mask","moss_block",Power.RECOVERY,"Draws strength from old roots"),
        new Mask("hollow_mask","Hollow Mask","skeleton_skull",Power.QUIET,"Softens the wearer's presence"),
        new Mask("dusk_mask","Dusk Mask","purple_dye",Power.VISION,"Sees clearly when daylight fades"),
        new Mask("dawn_mask","Dawn Mask","orange_dye",Power.RECOVERY,"Begins each journey with renewed energy"),
        new Mask("pilgrim_mask","Pilgrim Mask","compass",Power.TRAVERSAL,"Made for long roads and unfamiliar paths"),
        new Mask("courier_mask","Courier Mask","rabbit_foot",Power.AGILITY,"Helps the wearer move quickly between places"),
        new Mask("gardener_mask","Gardener Mask","flower_pot",Power.RECOVERY,"Remembers the shape of every garden"),
        new Mask("astronomer_mask","Astronomer Mask","spyglass",Power.VISION,"Finds small details beneath distant stars"),
        new Mask("archivist_mask","Archivist Mask","book",Power.ECHO,"Keeps old stories close at hand"),
        new Mask("ferryman_mask","Ferryman Mask","oak_boat",Power.AQUATIC,"Knows the quiet routes across water"),
        new Mask("orchard_mask","Orchard Mask","apple",Power.RECOVERY,"A reminder of a peaceful harvest"),
        new Mask("snowcap_mask","Snowcap Mask","snowball",Power.RESILIENT,"Made for high passes and deep winter"),
        new Mask("raincaller_mask","Raincaller Mask","water_bucket",Power.AQUATIC,"Follows rain to hidden streams"),
        new Mask("cloudstep_mask","Cloudstep Mask","white_wool",Power.TRAVERSAL,"Makes high ledges feel a little closer"),
        new Mask("deepcurrent_mask","Deepcurrent Mask","nautilus_shell",Power.AQUATIC,"Follows the deepest ocean paths"),
        new Mask("forgeheart_mask","Forgeheart Mask","copper_ingot",Power.RESILIENT,"Remembers the heat of the old foundries"),
        new Mask("crystalkeeper_mask","Crystalkeeper Mask","amethyst_cluster",Power.VISION,"Finds the glimmer of buried crystals"),
        new Mask("moth_mask","Moth Mask","glow_ink_sac",Power.QUIET,"Moves softly around points of light"),
        new Mask("stag_mask","Stag Mask","leather",Power.AGILITY,"Carries the quickness of the forest"),
        new Mask("owl_mask","Owl Mask","feather",Power.VISION,"Sees clearly through the darkest hours"),
        new Mask("fox_mask","Fox Mask","sweet_berries",Power.QUIET,"Keeps a careful and quiet pace"),
        new Mask("rabbit_mask","Rabbit Mask","rabbit_hide",Power.AGILITY,"A nimble companion for open paths"),
        new Mask("turtle_mask","Turtle Mask","scute",Power.RESILIENT,"A steady presence on a long journey"),
        new Mask("mothwing_mask","Mothwing Mask","phantom_membrane",Power.TRAVERSAL,"Catches the air on a gentle descent"),
        new Mask("silent_mask","Silent Mask","black_dye",Power.QUIET,"Leaves little trace in forgotten halls"),
        new Mask("festival_mask","Festival Mask","firework_star",Power.BALANCED,"Remembers the lights of Clock Town"),
        new Mask("clockface_mask","Clockface Mask","clock",Power.ECHO,"Keeps time with the distant bells"),
        new Mask("starreader_mask","Starreader Mask","glowstone_dust",Power.VISION,"Reads patterns in the night sky"),
        new Mask("tidecaller_mask","Tidecaller Mask","prismarine_shard",Power.AQUATIC,"Feels the rhythm of the turning tide"),
        new Mask("lanternbearer_mask","Lanternbearer Mask","soul_lantern",Power.VISION,"Carries a reassuring light"),
        new Mask("wandering_mask","Wandering Mask","map",Power.TRAVERSAL,"Made for anyone who follows a new road"),
        new Mask("marshlight_mask","Marshlight Mask","ochre_froglight",Power.AQUATIC,"Finds safe paths through misty marshes"),
        new Mask("deepwood_mask","Deepwood Mask","dark_oak_sapling",Power.RECOVERY,"Feels at home beneath the forest canopy"),
        new Mask("pearl_mask","Pearl Mask","prismarine_crystals",Power.BALANCED,"A keepsake from the Shifting Coast"),
        new Mask("starlit_mask","Starlit Mask","nether_star",Power.VISION,"Holds a little of the night's sparkle")
    );
    private MaskCatalog() {}
    public static List<Mask> all() { return MASKS; }
    public static Mask byId(String id) {
        return MASKS.stream().filter(mask -> mask.id().equals(id)).findFirst().orElse(null);
    }
}

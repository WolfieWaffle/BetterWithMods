package betterwithmods.module.hardcore.creatures;

import betterwithmods.common.items.ItemArcaneScroll;
import betterwithmods.module.Feature;
import betterwithmods.util.WorldUtils;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.function.Predicate;

public class HCEnchanting extends Feature {
    private static boolean steelRequiresInfernal;


    public static boolean canEnchantSteel() {
        return !steelRequiresInfernal;
    }

    public static double dropChance;
    public static boolean fuckMending;
    @Override
    public void setupConfig() {
        steelRequiresInfernal = loadPropBool("Steel Requires Infernal Enchanter", "Soulforged Steel tools can only be enchanted with the Infernal Enchanter", true);
        dropChance = loadPropDouble("Arcane Scroll Drop Chance", "Percentage chance that an arcane scroll will drop, does not effect some scrolls.", 0.01);
        fuckMending = loadPropBool("Disable Mending", "Mending is a bad unbalanced pile of poo", true);
    }

    @Override
    public String getFeatureDescription() {
        return "Adds Arcane Scroll drops to specific mobs, used for enchanting with the Infernal Enchanter";
    }

    @FunctionalInterface
    public interface ScrollDrop {
        ItemStack getScroll(EntityLivingBase entity);

        default double getChance() {
            return dropChance;
        }
    }

    private static final HashMap<Class<? extends EntityLivingBase>, ScrollDrop> SCROLL_DROPS = Maps.newHashMap();

    public static void addScrollDrop(Class<? extends EntityLivingBase> clazz, Enchantment enchantment) {
        addScrollDrop(clazz, (entity) -> ItemArcaneScroll.getScrollWithEnchant(enchantment));
    }

    public static void addScrollDrop(Class<? extends EntityLivingBase> clazz, ItemStack scroll) {
        addScrollDrop(clazz, (entity) -> scroll);
    }

    public static void addScrollDrop(Class<? extends EntityLivingBase> clazz, ScrollDrop scroll) {
        SCROLL_DROPS.put(clazz, scroll);
    }


    @Override
    public void init(FMLInitializationEvent event) {
        addScrollDrop(EntitySlime.class, Enchantments.PROTECTION);
        addScrollDrop(EntityPigZombie.class, Enchantments.FIRE_PROTECTION);
        addScrollDrop(EntityBat.class, Enchantments.FEATHER_FALLING);
        addScrollDrop(EntityCreeper.class, Enchantments.BLAST_PROTECTION);
        addScrollDrop(AbstractSkeleton.class, (entity) -> {
            if (entity.world.provider.getDimensionType() == DimensionType.NETHER)
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.INFINITY);
            else
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.PROJECTILE_PROTECTION);
        });
        addScrollDrop(EntitySquid.class, Enchantments.RESPIRATION);
        addScrollDrop(EntityWitch.class, Enchantments.AQUA_AFFINITY);
        addScrollDrop(EntityZombie.class, Enchantments.SMITE);
        addScrollDrop(EntitySpider.class, Enchantments.BANE_OF_ARTHROPODS);
        addScrollDrop(EntityMagmaCube.class, Enchantments.FIRE_ASPECT);
        addScrollDrop(EntityEnderman.class, Enchantments.SILK_TOUCH);
        addScrollDrop(EntityGhast.class, Enchantments.PUNCH);
        addScrollDrop(EntityBlaze.class, Enchantments.FLAME);
        addScrollDrop(EntityPolarBear.class, Enchantments.FROST_WALKER);
        addScrollDrop(EntityGuardian.class, Enchantments.DEPTH_STRIDER);
        if(!fuckMending) {
            addScrollDrop(EntityShulker.class, new ScrollDrop() {
                @Override
                public ItemStack getScroll(EntityLivingBase entity) {
                    return ItemArcaneScroll.getScrollWithEnchant(Enchantments.MENDING);
                }

                @Override
                public double getChance() {
                    return 0.001;
                }
            });
        }
        addScrollDrop(EntityDragon.class, new ScrollDrop() {
            @Override
            public ItemStack getScroll(EntityLivingBase entity) {
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.SWEEPING);
            }

            //Always drops
            @Override
            public double getChance() {
                return 1;
            }
        });
        addScrollDrop(EntityWither.class, new ScrollDrop() {
            @Override
            public ItemStack getScroll(EntityLivingBase entity) {
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.KNOCKBACK);
            }

            //Always drops
            @Override
            public double getChance() {
                return 1;
            }
        });
        addScrollDrop(EntitySilverfish.class, entity -> {
            if (entity.world.provider.getDimensionType() == DimensionType.THE_END) {
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.EFFICIENCY);
            }
            return null;
        });

        //TODO
        // SHARPNESS -> Butcher Trade
        // LOOTING -> Farmer Trade
        // UNBREAKING -> Blacksmith Trade
        // FORTUNE ->  Priest Trade
        // POWER   -> Librarian Trade
        // LUCK_OF_THE_SEA, LURE -> Fisherman Trade
        /*
        BINDING_CURSE illager
        MENDING REMOVE
        VANISHING_CURSE illager
         */
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        for (Class<? extends EntityLivingBase> entity : SCROLL_DROPS.keySet()) {
            if (entity.isAssignableFrom(event.getEntityLiving().getClass())) {
                ScrollDrop drop = SCROLL_DROPS.get(entity);
                if (drop.getScroll(event.getEntityLiving()) != null) {
                    if (event.getEntityLiving().getRNG().nextDouble() <= drop.getChance()) {
                        WorldUtils.addDrop(event, drop.getScroll(event.getEntityLiving()));
                    }
                }
            }
        }
    }


    public enum InfernalEnchantmentType {
        ALL(item -> false),
        ARMOR(item -> item instanceof ItemArmor),
        ARMOR_FEET(item -> item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.FEET),
        ARMOR_LEGS(item -> item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.LEGS),
        ARMOR_CHEST(item -> item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.CHEST),
        ARMOR_HEAD(item -> item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.HEAD),
        WEAPON(item -> item instanceof ItemSword),
        TOOL(item -> item instanceof ItemTool),
        FISHING_ROD(item -> item instanceof ItemFishingRod),
        BREAKABLE(Item::isDamageable),
        BOW(item -> item instanceof ItemBow),
        WEARABLE(item -> item instanceof ItemArmor || item instanceof ItemElytra || item instanceof ItemSkull || (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockPumpkin));

        private Predicate<Item> delegate = null;

        InfernalEnchantmentType(Predicate<Item> delegate) {
            this.delegate = delegate;
        }

        public boolean canEnchantItem(Item item) {
            return this.delegate != null && this.delegate.test(item);
        }

        public static InfernalEnchantmentType[] VALUES = values();

        public static InfernalEnchantmentType fromEnchantment(Enchantment enchantment) {
            if (enchantment.type != null) {
                return VALUES[enchantment.type.ordinal()];
            }
            return ALL;
        }
    }
}

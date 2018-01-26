package betterwithmods.module.hardcore.needs;

import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.util.item.StackMap;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Calendar;

/**
 * Created by primetoxinz on 5/10/17.
 */
public class HCArmor extends Feature {
    public static final StackMap<Integer> weights = new StackMap<>(0);

    public static final EntityEquipmentSlot[] ARMOR_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    public static final ItemStack[] IRON_ARMOR = new ItemStack[]{new ItemStack(Items.IRON_HELMET), new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_BOOTS)};

    public static boolean changeArmorSpawns;

    public static boolean shieldRebalance;

    public static float getWeight(ItemStack stack) {
        if (!ModuleLoader.isFeatureEnabled(HCArmor.class))
            return 0;
        return weights.get(stack);
    }

    public static void initWeights() {
        weights.put(Items.CHAINMAIL_HELMET, OreDictionary.WILDCARD_VALUE, 3);
        weights.put(Items.CHAINMAIL_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 4);
        weights.put(Items.CHAINMAIL_LEGGINGS, OreDictionary.WILDCARD_VALUE, 4);
        weights.put(Items.CHAINMAIL_BOOTS, OreDictionary.WILDCARD_VALUE, 2);

        weights.put(Items.IRON_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.IRON_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.IRON_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.IRON_BOOTS, OreDictionary.WILDCARD_VALUE, 4);

        weights.put(Items.DIAMOND_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.DIAMOND_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.DIAMOND_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.DIAMOND_BOOTS, OreDictionary.WILDCARD_VALUE, 4);

        weights.put(Items.GOLDEN_HELMET, OreDictionary.WILDCARD_VALUE, 5);
        weights.put(Items.GOLDEN_CHESTPLATE, OreDictionary.WILDCARD_VALUE, 8);
        weights.put(Items.GOLDEN_LEGGINGS, OreDictionary.WILDCARD_VALUE, 7);
        weights.put(Items.GOLDEN_BOOTS, OreDictionary.WILDCARD_VALUE, 4);
    }

    public static void setSkeletonEquipment(AbstractSkeleton entity) {
        entity.setCombatTask();
        entity.setCanPickUpLoot(entity.getRNG().nextFloat() <= 0.25);
        entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, entity instanceof EntityWitherSkeleton ? ItemStack.EMPTY : new ItemStack(Items.BOW));
        halloween(entity);
    }

    public static void setZombieEquipment(EntityZombie entity) {
        entity.setCanPickUpLoot(entity.getRNG().nextFloat() <= 0.25);
        entity.setBreakDoorsAItask(true);

        //tool
        if (entity.getRNG().nextFloat() < 0.05F) {
            int i = entity.getRNG().nextInt(3);
            if (i == 0) {
                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }

        //armor
        if (entity.getRNG().nextFloat() < 0.018) {
            boolean flag = true;
            for (int s = 0; s < ARMOR_SLOTS.length; s++) {
                EntityEquipmentSlot slot = ARMOR_SLOTS[s];
                ItemStack current = entity.getItemStackFromSlot(slot);
                if (current.isEmpty()) {
                    if (!flag && entity.getRNG().nextFloat() < 0.1) {
                        break;
                    }
                    flag = false;
                    entity.setItemStackToSlot(slot, IRON_ARMOR[s]);
                }
            }
        }
        halloween(entity);
        entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", entity.getRNG().nextDouble() * 0.05000000074505806D, 0));
        double d0 = entity.getRNG().nextDouble() * 1.5D * 0.012;
        if (d0 > 1.0D) {
            entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (shieldRebalance) {
            addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.SHIELD),
                    "SWS", "WIW", " W ", 'S', "strapLeather", 'W', "sidingWood", 'I', "ingotIron"
            ).setRegistryName("minecraft:shield"));
        }
        initWeights();
    }

    @SubscribeEvent
    public void swimmingPenalty(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (PlayerHelper.isSurvival(player) && player.isInWater() && !PlayerHelper.canSwim(player) && !PlayerHelper.isNearBottom(player)) {
            player.motionY -= 0.02;
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    public static void halloween(EntityLiving entity) {
        if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = entity.world.getCurrentDate();
            if (calendar.get(Calendar.MONTH) + 1 == 10 && calendar.get(Calendar.DATE) == 31 && entity.getRNG().nextFloat() < 0.25F) {
                entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(entity.getRNG().nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                entity.setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
            }
        }
    }

    @Override
    public void setupConfig() {
        changeArmorSpawns = loadPropBool("Change Armor Spawns", "Changes Entity armor spawning: Zombies only spawn with Iron armor, Skeletons never wear armor.", false);

        shieldRebalance = loadPropBool("Shield Rebalance", "Experimental recipes for rebalacing shields", false);
    }

    @Override
    public String getFeatureDescription() {
        return "Gives Armor weight values that effect movement. Changes Entity armor spawning: Zombies only spawn with Iron armor, Skeletons never wear armor.";
    }

    @SubscribeEvent
    public void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (!changeArmorSpawns)
            return;
        if (event.getEntityLiving() instanceof EntityZombie) {
            event.setCanceled(true);
            EntityZombie entity = (EntityZombie) event.getEntityLiving();
            setZombieEquipment(entity);
        }
        if (event.getEntityLiving() instanceof AbstractSkeleton) {
            event.setCanceled(true);
            setSkeletonEquipment((AbstractSkeleton) event.getEntityLiving());
        }
    }
}

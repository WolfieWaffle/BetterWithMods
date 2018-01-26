package betterwithmods.util;


import com.google.common.collect.Maps;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.HashMap;
import java.util.List;

public class VillagerUtils {

    private static HashMap<VillagerRegistry.VillagerProfession, HashMap<String, VillagerRegistry.VillagerCareer>> professionCareerMap = Maps.newHashMap();

    public static void initVillagerInfo() {
        for (VillagerRegistry.VillagerProfession profession : ForgeRegistries.VILLAGER_PROFESSIONS.getValues()) {
            HashMap<String, VillagerRegistry.VillagerCareer> careers = Maps.newHashMap();
            for (VillagerRegistry.VillagerCareer career : VillagerUtils.getCareers(profession)) {
                careers.put(career.getName(), career);
            }
            professionCareerMap.put(profession, careers);
        }
    }

    public static List<VillagerRegistry.VillagerCareer> getCareers(VillagerRegistry.VillagerProfession profession) {
        return ReflectionHelper.getPrivateValue(VillagerRegistry.VillagerProfession.class, profession, "careers");
    }

    public static List<List<EntityVillager.ITradeList>> getTrades(VillagerRegistry.VillagerCareer career) {
        return ReflectionHelper.getPrivateValue(VillagerRegistry.VillagerCareer.class, career, "trades");
    }

    public static void clearTrades() {
        for (VillagerRegistry.VillagerProfession profession : ForgeRegistries.VILLAGER_PROFESSIONS.getValues()) {
            for (VillagerRegistry.VillagerCareer career : VillagerUtils.getCareers(profession)) {
                VillagerUtils.getTrades(career).clear();
            }
        }
    }

    public static VillagerRegistry.VillagerCareer getCareer(VillagerRegistry.VillagerProfession profession, String name) {
        HashMap<String, VillagerRegistry.VillagerCareer> c = professionCareerMap.get(profession);
        if (c != null) {
            return c.get(name);
        }
        return null;
    }

    public static void addTrades(String prof, String career, int level, List<EntityVillager.ITradeList> trades) {
        for(EntityVillager.ITradeList t: trades)
            addTrade(prof,career,level,t);
    }

    public static void addTrade(String prof, String career, int level, EntityVillager.ITradeList trades) {
        VillagerRegistry.VillagerProfession profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(prof));
        VillagerRegistry.VillagerCareer c = getCareer(profession, career);
        if (c != null)
            c.addTrade(level, trades);
    }

}

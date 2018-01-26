package betterwithmods.common.registry.heat;

import betterwithmods.common.registry.blockmeta.recipe.BlockMeta;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class BWMHeatRegistry {
    private static final List<HeatSource> HEAT_SOURCES = Lists.newArrayList();

    public static void addHeatSource(Block block, int heat) {
        addHeatSource(block, OreDictionary.WILDCARD_VALUE, heat);
    }

    public static void addHeatSource(Block block, int meta, int heat) {
        HEAT_SOURCES.add(new HeatSource(block, meta, heat));
    }

    public static void addHeatSource(ItemStack stack, int heat) {
        HEAT_SOURCES.add(new HeatSource(stack, heat));
    }

    public static int getHeat(IBlockState state) {
        HeatSource source = get(state);
        if (source != null)
            return source.getHeat();
        return 0;
    }

    public static HeatSource get(IBlockState state) {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        return get(block, meta);
    }

    public static HeatSource get(Block block, int meta) {
        return HEAT_SOURCES.stream().filter(bm -> bm.equals(block, meta)).findFirst().orElse(null);
    }

    public static class HeatSource extends BlockMeta {
        int heat;

        public HeatSource(Block block, int meta, int heat) {
            super(block, meta);
            this.heat = heat;
        }

        public HeatSource(ItemStack stack, int heat) {
            super(stack);
            this.heat = heat;
        }

        public int getHeat() {
            return heat;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ItemStack) {
                return super.equals(o);
            } else if (o instanceof HeatSource) {
                return super.equals(o) && heat == ((HeatSource) o).heat;
            }
            return false;
        }
    }
}
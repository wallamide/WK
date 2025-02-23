package cf.witcheskitchen.common.generator;


//this will all need to be reworked to better suit our needs

import cf.witcheskitchen.common.registry.WKConfiguredFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class SumacFoliagePlacer
        extends FoliagePlacer {
    public static final Codec<SumacFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> SumacFoliagePlacer.fillFoliagePlacerFields(instance).apply(instance, SumacFoliagePlacer::new));

    public SumacFoliagePlacer(IntProvider intProvider, IntProvider intProvider2) {
        super(intProvider, intProvider2);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return WKConfiguredFeatures.SUMAC_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, FoliagePlacer.TreeNode treeNode, int foliageHeight, int radius, int offset) {
        boolean bl = treeNode.isGiantTrunk();
        BlockPos blockPos = treeNode.getCenter().up(offset);
        this.generateSquare(world, replacer, random, config, blockPos, radius + treeNode.getFoliageRadius(), -1 - foliageHeight, bl);
        this.generateSquare(world, replacer, random, config, blockPos, radius - 1, -foliageHeight, bl);
        this.generateSquare(world, replacer, random, config, blockPos, radius + treeNode.getFoliageRadius() - 1, 0, bl);
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == 0) {
            return (dx > 1 || dz > 1) && dx != 0 && dz != 0;
        }
        return dx == radius && dz == radius && radius > 0;
    }
}
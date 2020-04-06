/*package com.minerarcana.floralchemy.village;

import com.minerarcana.floralchemy.Floralchemy;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

//TODO Biome based wood type
public class VillageHedgeHouse extends StructureVillagePieces.Village {
    private static final ResourceLocation ID = new ResourceLocation(Floralchemy.MOD_ID, "hedge_house");

    public VillageHedgeHouse() {
    }

    public VillageHedgeHouse(StructureVillagePieces.Start start, int type, StructureBoundingBox boundingBox,
            Direction facing) {
        super(start, type);
        this.boundingBox = boundingBox;
        setCoordBaseMode(facing);
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox boundingBox) {
        if(averageGroundLvl < 0) {
            averageGroundLvl = getAverageGroundLevel(world, boundingBox);
            if(averageGroundLvl < 0) {
                return true;
            }

            this.boundingBox.offset(0, averageGroundLvl - this.boundingBox.minY, 0);
        }
        BlockPos pos = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setReplacedBlock(Blocks.STRUCTURE_VOID)
                .setBoundingBox(boundingBox);
        Template template = templateManager.getTemplate(world.getMinecraftServer(), ID);
        template.addBlocksToWorldChunk(world, pos, settings);
        return true;
    }

    @Nullable
    public static StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece,
            StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y,
            int z, Direction facing, int type) {
        StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 6, 7,
                facing);
        if(canVillageGoDeeper(boundingBox) && findIntersecting(pieces, boundingBox) == null) {
            return new VillageHedgeHouse(startPiece, type, boundingBox, facing);
        }
        return null;
    }
}*/

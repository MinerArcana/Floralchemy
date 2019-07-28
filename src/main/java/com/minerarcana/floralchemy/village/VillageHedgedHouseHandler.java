package com.minerarcana.floralchemy.village;

import java.util.List;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.*;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageHedgedHouseHandler implements IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int size) {
        return new PieceWeight(getComponentClass(), 3, 2);
    }

    @Override
    public Class<VillageHedgeHouse> getComponentClass() {
        return VillageHedgeHouse.class;
    }

    @Override
    public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces,
            Random random, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
        return VillageHedgeHouse.buildComponent(villagePiece, startPiece, pieces, random, structureMinX, structureMinY,
                structureMinZ, facing, type);
    }

}

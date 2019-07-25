package com.minerarcana.floralchemy.village;

import java.util.List;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.*;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageHedgedFieldHandler implements IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int size) {
        return new PieceWeight(this.getComponentClass(), 10, 4);
    }

    @Override
    public Class<VillageHedgedField> getComponentClass() {
        return VillageHedgedField.class;
    }

    @Override
    public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces,
            Random random, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(structureMinX, structureMinY, structureMinZ, 0, 0, 0, 13, 4, 9, facing);
        return VillageHedgedField.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new VillageHedgedField(startPiece, type, random, structureboundingbox, facing) : null;
    }

}

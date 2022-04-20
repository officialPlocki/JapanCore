
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.inventory.types;

import org.japanbuild.japancore.inventory.selective.SelectiveInventory;

public class TypeInventory_Empty implements SelectiveInventory {

    private final boolean b;
    private final int r;

    public TypeInventory_Empty(boolean filler, int rows) {
        b = filler;
        r = rows;
    }

    @Override
    public boolean filler() {
        return b;
    }

    @Override
    public int rows() {
        return r;
    }

    @Override
    public int[] selectiveSlots() {
        return new int[0];
    }

}

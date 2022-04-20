
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.inventory.types;

import org.japanbuild.japancore.inventory.selective.SelectiveInventory;

public class TypeInventory_3x implements SelectiveInventory {

    @Override
    public boolean filler() {
        return true;
    }

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public int[] selectiveSlots() {
        int[] array = new int[3];
        array[0] = 1;
        array[1] = 2;
        array[2] = 3;
        return array;
    }

}

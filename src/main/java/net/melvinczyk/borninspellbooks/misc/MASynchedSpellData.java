package net.melvinczyk.borninspellbooks.misc;

import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;

public class MASynchedSpellData extends SyncedSpellData {
    public static final long PHANTOM_SPLIT = 4;


    public MASynchedSpellData(int serverPlayerId) {
        super(serverPlayerId);
    }
}

package org.example;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;

public class Parser {

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_512;
    private static final VectorMask<Byte> E = VectorMask.fromArray(SPECIES, makeEvens(), 0);
    private static final VectorMask<Byte> O = VectorMask.fromArray(SPECIES, makeOdds(), 0);
    private static boolean[] makeEvens() {
        boolean[] evens = new boolean[SPECIES.length()];
        for (int i = 0; i < evens.length; i++) {
            evens[i] = i % 2 == 0;
        }
        return evens;
    }
    private static boolean[] makeOdds() {
        boolean[] odds = new boolean[SPECIES.length()];
        for (int i = 0; i < odds.length; i++) {
            odds[i] = i % 2 == 1;
        }
        return odds;
    }

    private static final VectorShuffle<Byte> SHL = makeShl();// todo;

    private static VectorShuffle<Byte> makeShl() {
        int[] shl = new int[SPECIES.length()];
        for (int i = 1; i < shl.length; i++) {
            shl[i] = (byte) (i - 1);
        }
        shl[0] = SPECIES.length() - 1;
        return VectorShuffle.fromArray(SPECIES, shl, 0);
    }

    private static final VectorMask<Byte> CLEAR_FAR_RIGHT = makeClearFarRight();

    private static VectorMask<Byte> makeClearFarRight() {
        boolean[] clearFarRight = new boolean[SPECIES.length()];
        for (int i = 0; i < clearFarRight.length; i++) {
            clearFarRight[i] = i != SPECIES.length() - 1;
        }
        return VectorMask.fromArray(SPECIES, clearFarRight, 0);
    }

    private static VectorMask<Byte> shl(ByteVector byteVector) {
        return byteVector.rearrange(SHL).eq((byte)'\\').and(CLEAR_FAR_RIGHT);
    }

    public static void parse(byte[] json) {
        ByteVector byteVector = ByteVector.fromArray(SPECIES, json, 0);
        System.out.println(byteVector);
        var BB = byteVector.eq((byte)'\\').toVector();
        var B = byteVector.eq((byte)'\\');
        var E = Parser.E;
        var O = Parser.O;
        // identify 'starts' - backslashes characters not preceded by backslashes
        var S = B.andNot(shl(byteVector)); // todo: not as efficient as it could be (see shl)
        System.out.println(S);
        // detect ...
        var ES = S.and(E);

        // var S = B &~(B << 1);
        // B.;;

        //byteVector.eq();

        var Q = byteVector.eq((byte)'"');
    }
}

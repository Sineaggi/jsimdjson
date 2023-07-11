package org.example;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;

public class Parser {

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_512;
    private static final ByteVector E = ByteVector.fromBooleanArray();
    private static final VectorShuffle SHL = ;// todo;

    private static final VectorMask<Byte> shl(VectorMask<Byte> vm) {
        // todo: how to shift VectorMask<Byte> left by 1?
        throw new RuntimeException("todo");
    }

    public static void parse(byte[] json) {
        ByteVector byteVector = ByteVector.fromArray(SPECIES, json, 0);
        System.out.println(byteVector);
        var B = byteVector.eq((byte)'\\');
        var S = B.andNot(shl(B));
        // B.rearrange(SHL);

        var E = ; // todo???; evens
        var O = ; // todo???; odds
        // identify 'starts' - backslashes characters not preceded by backslashes
        var S = B &~(B << 1);
        B.;;

        //byteVector.eq();
    }
}

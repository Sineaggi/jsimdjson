package org.example;

import jdk.incubator.vector.*;

import java.math.BigDecimal;

import static jdk.incubator.vector.VectorOperators.MUL;

public class Parser {

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_512;
    private static final VectorMask<Byte> E = VectorMask.fromArray(SPECIES, makeEvens(), 0);
    private static final VectorMask<Byte> O = VectorMask.fromArray(SPECIES, makeOdds(), 0);
    private static final Vector<Byte> NEG_ONE = SPECIES.broadcast(-1);
    private static final Vector<Byte> ONE = SPECIES.broadcast(1);
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
        // detect end of a odd-length sequence of backslashes starting on an even offset
        // detail: ES gets all 'starts' that being on even offsets
        var ES = S.and(E);
        // add B to ES, yielding carries on backslash sequences with even starts
        var B1 = B.toVector().neg().toString();
        var B22 = toString (B);
        var ES1 = ES.toVector().neg();
        var ES2 = toString(ES);
        var EC = B.toVector().neg().add(ES.toVector().neg()).eq(ONE);
        //var EC = B.toVector().neg().to
        B.toVector().neg().toLongArray();
        var EC2 = carrylessAdd(B, ES);
        System.out.println(EC);

        // var S = B &~(B << 1);
        // B.;;

        //byteVector.eq();

        var Q = byteVector.eq((byte)'"');
    }

    private static VectorMask<Byte> carrylessAdd(VectorMask<Byte> m1, VectorMask<Byte> m2) {
        var m1l = m1.toLong();
        var m2l = m2.toLong();
        long out = 0;
        for (int i = 0; i < 64; i++) {
            out = out ^ m1l << 1;
            // todo: handle overflow
        }
        ((ByteVector)m1.toVector()).toArray();
        //var f1 = LongVector.fromArray(LongVector.SPECIES_512, out, 0)
        //        .reinterpretAsBytes()
        //        .eq((byte)1);
        return ByteVector.fromArray(ByteVector.SPECIES_64, out, 0)
                .eq((byte)1);
        //return f1;
    }

    public static String toString(VectorMask<?> vm) {
        var array = vm.toArray();
        char[] chars = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = array[i] ? '1' : '_';
        }
        return new String(chars);
    }
}

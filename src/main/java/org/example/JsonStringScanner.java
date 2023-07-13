package org.example;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorSpecies;

// todo: replace with __primitive class
/* __primitive */ record JsonStringBlock(
        long backslash,
        long escaped,
        long quote,
        long inString
) {
}

// todo: maybe inline this? otherwise not sure how fast it'll end up being
class Simd {
    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_512;
    public Simd(byte[] json) {
        byteVector = ByteVector.fromArray(SPECIES, json, 0);
    }
    private final ByteVector byteVector;
    public long eq(char c) {
        return byteVector.eq((byte)c).toLong();
    }
}

public class JsonStringScanner {
    private long prevInString = 0L;
    private long prevEscaped = 0L;

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_512;

    public JsonStringBlock next(Simd in) {
        long backslash = in.eq('\\');
        long escaped = findEscaped(backslash);
        long quote = in.eq('"') & ~escaped;

        long inString = prefixXor(quote) ^ prevInString;

        prevInString = inString >> 63;

        return new JsonStringBlock(
                backslash,
                escaped,
                quote,
                inString
        );
    }

    public long findEscaped(long backslash) {
        if (backslash == 0) { long escaped = prevEscaped; prevEscaped = 0; return escaped; }
        return findEscapedBranchless(backslash);
    }

    public long findEscapedBranchless(long backslash) {
        // If there was overflow, pretend the first character isn't a backslash
        backslash &= ~prevEscaped;
        long follows_escape = backslash << 1 | prevEscaped;

        // Get sequences starting on even bits by clearing out the odd series using +
        long even_bits = 0x5555555555555555L;
        long odd_sequence_starts = backslash & ~even_bits & ~follows_escape;
        long sequences_starting_on_even_bits;
        sequences_starting_on_even_bits = addOverflow(odd_sequence_starts, backslash);
        long invert_mask = sequences_starting_on_even_bits << 1; // The mask we want to return is the *escaped* bits, not escapes.

        // Mask every other backslashed character as an escaped character
        // Flip the mask for sequences that start on even bits, to correct them
        return (even_bits ^ invert_mask) & follows_escape;
    }

    /*
bool add_overflow(uint64_t value1, uint64_t value2,
                                  uint64_t *result) {
#if SIMDJSON_REGULAR_VISUAL_STUDIO
    return _addcarry_u64(0, value1, value2,
                       reinterpret_cast<unsigned __int64 *>(result));
#else
    return __builtin_uaddll_overflow(value1, value2,
                                     reinterpret_cast<unsigned long long *>(result));
#endif
}
     */

    public long addOverflow(long value1, long value2) {
        // todo: write tests for this
        prevEscaped = overflow;
        return value1 + value2;
    }

    private static long prefixXor(long bitmask) {
        // There should be no such thing with a processor supporting avx2
        // but not clmul.
        __m128i all_ones = _mm_set1_epi8('\xFF');
        __m128i result = _mm_clmulepi64_si128(_mm_set_epi64x(0ULL, bitmask), all_ones, 0);
        return _mm_cvtsi128_si64(result);
        throw new RuntimeException("not yet implemented");
    }
}

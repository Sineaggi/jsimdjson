package org.example;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public class ParserTest {
    @Test
    public void main() {
        //language=JSON
        var json = """
                {
                  "Width": 800,
                  "Height": 600,
                  "Title": "View from my room",
                  "Url": " http://ex.com/img.png",
                  "Private": false ,
                  "Thumbnail": {
                    "Url": "http://ex.com/th.png",
                    "Height": 125,
                    "Width": 100
                  },
                  "array": [
                    116,
                    943,
                    234
                  ],
                  "Owner": null
                }
                """;
        // System.out.println("hi");
        // System.out.println("hi2");
    }


    @Test
    public void to() {
        byte[] bytes = readResource("/test.json");
        System.out.println(bytes.length);
        Parser.parseViaLongs(bytes);
    }

    private static byte[] readResource(String name) {
        try (InputStream is = ParserTest.class.getResourceAsStream(name)) {
            return Objects.requireNonNull(is).readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_64;

    @Test
    public void carrylessmul() {
        var bytes = new byte[]{
                1, 0, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        ByteVector byteVector = ByteVector.fromArray(SPECIES, bytes, 0);
        System.out.println(byteVector);
        ByteVector ones = ByteVector.broadcast(ByteVector.SPECIES_64, 1);
        var ff = byteVector.lanewise(VectorOperators.XOR, ones);
        System.out.println(ff);
    }
}

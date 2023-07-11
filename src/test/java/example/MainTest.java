package example;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public class MainTest {
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

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_512;
    private static final ByteVector E = ByteVector.fromBooleanArray();
    private static final VectorShuffle SHL = ;// todo;

    private static final VectorMask<Byte> shl(VectorMask<Byte> vm) {
        // todo: goddamit
        vm
        throw new RuntimeException("onoz");
    }

    @Test
    public void to() {
        byte[] bytes = readResource("/test.json");
        System.out.println(bytes.length);
        ByteVector byteVector = ByteVector.fromArray(SPECIES, bytes, 0);
        System.out.println(byteVector);
        var B = byteVector.eq((byte)'\\');
        var S = B.andNot(shl(B));
        //B.rearrange(SHL);
        B.

        var E = ; // todo???; evens
        var O = ; // todo???; odds
        // identify 'starts' - backslashes characters not preceded by backslashes
        var S = B &~(B << 1);
        B.;;

        //byteVector.eq();
    }

    private static byte[] readResource(String name) {
        try (InputStream is = MainTest.class.getResourceAsStream(name)) {
            return Objects.requireNonNull(is).readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

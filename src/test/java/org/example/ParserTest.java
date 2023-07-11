package org.example;

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
        Parser.parse(bytes);
    }

    private static byte[] readResource(String name) {
        try (InputStream is = ParserTest.class.getResourceAsStream(name)) {
            return Objects.requireNonNull(is).readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

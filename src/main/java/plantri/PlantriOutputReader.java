package plantri;

import graph.components.NoLadderC4ECGraph;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import lombok.SneakyThrows;

public class PlantriOutputReader implements Iterable<NoLadderC4ECGraph>{

    private Scanner scanner;

    public PlantriOutputReader(final InputStreamReader reader) {
        this.scanner = new Scanner(reader);
    }

    public PlantriOutputReader(final File file) throws FileNotFoundException, UnsupportedEncodingException {
        this(new InputStreamReader(new FileInputStream(file), "ISO-8859-15"));
    }

    @Override
    public Iterator<NoLadderC4ECGraph> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return scanner.hasNext();
            }

            @SneakyThrows
            @Override
            public NoLadderC4ECGraph next() {
                final int nrOfVertices = scanner.nextInt();
                final String inputSequence = scanner.next();
                final List<String> splitInputSequence = List.of(inputSequence.split(","));

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(Integer.toString(nrOfVertices).getBytes());

                for (final String adjacentVertices : splitInputSequence) {
                    for (int i = 0; i < 3; i++) {
                        final int ord = adjacentVertices.charAt(i) - 97;
                        byteArrayOutputStream.write(" ".getBytes());
                        byteArrayOutputStream.write(Integer.toString(ord).getBytes());
                    }
                }

                return NoLadderC4ECGraph.loadFromFile(
                        new InputStreamReader(
                                new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
            }
        };
    }
}

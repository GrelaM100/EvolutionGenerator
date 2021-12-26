import Classes.Animal;
import Classes.Genotype;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenotypeTest {
    @Test
    void createRandomGenotype() {
        Genotype genotype = new Genotype();
        assertEquals(genotype.genes.length, 32);
        for(int gene : genotype.genes) {
            assertTrue(gene >= 0 && gene <= 7);
        }
    }
}

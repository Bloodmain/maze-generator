package backend.academy.maze.algorithm;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DSUTest {
    @Test
    public void disjointByDefault() {
        DSU dsu = new DSU(20);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (i != j) {
                    assertThat(dsu.leader(i)).isNotEqualTo(dsu.leader(j));
                }
            }
        }
    }

    @Test
    public void joinAll() {
        DSU dsu = new DSU(20);

        for (int i = 1; i < 20; i++) {
            dsu.unite(i - 1, i);
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                assertThat(dsu.leader(i)).isEqualTo(dsu.leader(j));
            }
        }
    }

    @Test
    public void twoUnions() {
        DSU dsu = new DSU(20);

        // even union
        for (int i = 2; i < 20; i += 2) {
            dsu.unite(i - 2, i);
        }

        // odd union
        for (int i = 3; i < 20; i += 2) {
            dsu.unite(i - 2, i);
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (i % 2 == j % 2) {
                    assertThat(dsu.leader(i)).isEqualTo(dsu.leader(j));
                } else {
                    assertThat(dsu.leader(i)).isNotEqualTo(dsu.leader(j));
                }
            }
        }
    }
}

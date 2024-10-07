package backend.academy.maze.algorithm;

/**
 * Implementation of Disjoint set union algorithm with path compression heuristic.
 * <p>
 * May achieve more performance by adding rank heuristic, but it's not implemented in sake of readability.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Disjoint-set_data_structure">DSU on wiki</a>
 */
public final class DSU {
    int[] ancestors;

    public DSU(int n) {
        ancestors = new int[n];
        // by default, every element contain in its own set.
        for (int i = 0; i < n; i++) {
            ancestors[i] = i;
        }
    }

    public int leader(int v) {
        int ancestor = ancestors[v];
        if (ancestor == v) {
            return ancestor;
        } else {
            // saving the leader to find it faster next time
            int nextAncestor = leader(ancestor);
            ancestors[v] = nextAncestor;
            return nextAncestor;
        }
    }

    public void unite(int u, int v) {
        int uLeader = leader(u);
        int vLeader = leader(v);
        ancestors[uLeader] = vLeader;
    }
}

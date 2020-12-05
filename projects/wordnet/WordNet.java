package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangkai
 */
public class WordNet {
    private final Map<Integer, String> synsetsById = new HashMap<>();
    private final Map<String, List<Integer>> synsetsMap = new HashMap<>();
    private final SAP sap;

    /**
     * Constructor takes the name of the two input files.
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new IllegalArgumentException();
        }

        if (hypernyms == null) {
            throw new IllegalArgumentException();
        }

        int vertexId = 0;
        In in = new In(synsets);
        while (in.hasNextLine()) {
            vertexId++;
            String[] strings = in.readLine().split(",");
            int id = Integer.parseInt(strings[0]);
            synsetsById.put(id, strings[1]);
            String[] words = strings[1].split(" ");
            for (String word : words) {
                if (!synsetsMap.containsKey(word)) {
                    synsetsMap.put(word, new ArrayList<>());
                }
                synsetsMap.get(word).add(id);
            }
        }

        Digraph g = new Digraph(vertexId);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] strings = in.readLine().split(",");
            int id = Integer.parseInt(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                g.addEdge(id, Integer.parseInt(strings[i]));
            }
        }

        this.sap = new SAP(g);

        detectOneRoot(g);
        detectNoCycle(g);
    }

    /**
     * Return all WordNet nouns.
     */
    public Iterable<String> nouns() {
        return new ArrayList<>(synsetsMap.keySet());
    }

    /**
     * @return whether the word is a WordNet noun.
     */
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return synsetsMap.containsKey(word);
    }

    /**
     * @return the distance between nounA and nounB (defined below).
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        List<Integer> v = this.synsetsMap.get(nounA);
        List<Integer> w = this.synsetsMap.get(nounB);
        return this.sap.length(v, w);
    }

    /**
     * @return a synset (second field of synsets.txt) that's the common ancestor of nounA and nounB
     * in the shortest ancestral path (defined below).
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        List<Integer> v = this.synsetsMap.get(nounA);
        List<Integer> w = this.synsetsMap.get(nounB);
        int ancestor = this.sap.ancestor(v, w);
        return this.synsetsById.get(ancestor);
    }

    private void detectOneRoot(Digraph g) {
        int count = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0) {
                count += 1;
            }
        }

        if (count != 1) {
            throw new IllegalArgumentException();
        }
    }

    private void detectNoCycle(Digraph g) {
        DirectedCycle cycle = new DirectedCycle(g);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
    }
}

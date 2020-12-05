package wordnet;

/**
 * @author huangkai
 */
public class Outcast {
    private final WordNet wordNet;

    /**
     * Constructor takes a WordNet object
     */
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    /**
     * Given an array of WordNet nouns, return an outcast.
     */
    public String outcast(String[] nouns) {
        int maxDistance = 0;
        String outcast = null;
        for (String noun: nouns) {
            int distance = 0;
            for (String s: nouns) {
                distance += wordNet.distance(noun, s);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = noun;
            }
        }
        return outcast;
    }
}

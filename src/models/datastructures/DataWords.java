package models.datastructures;

/**
 * Data structure for database words (table words)
 */
public record DataWords(int id, String word, String category) {
/**
     * Kontstruktor
     * @param id
     * @param word
     * @param category
     */
    public DataWords(int id, String word, String category) {
        this.id = id;
        this.word = word;
        this.category = category;
    }
    //Getters
    /**
     * Tagastab id
     * @return int
     */
    public int getId() {
        return id;
    }
    /**
     * Tagastab sõna
     * @return String
     */
    public String getWord() {
        return word;
    }
    /**
     * Tagastab kategooria
     * @return String
     */
    public String getCategory() {
        return category;
    }
}

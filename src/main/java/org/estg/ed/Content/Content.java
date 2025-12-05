package org.estg.ed.Content;

public abstract class Content {
    private int idContent;
    private boolean isSolved;

    public Content(int idContent) {
        this.idContent = idContent;
        this.isSolved = false;
    }

    /**
     * Executes the interaction between the player and the content.
     * 
     * This method must be implemented by subclasses with the specific logic
     * for each content type.
     * 
     * @param j     the current player
     * @param input the input typed by the player
     * @return true if the interaction was successful, false otherwise
     */
    //public abstract boolean interactType(Player p, String input);

    /**
     * Returns the identifier of this content.
     *
     * @return idContent
     */
    public int getIdContent() {
        return idContent;
    }

    /**
     * Indicates if the content has already been solved.
     *
     * @return true if solved, false otherwise
     */
    public boolean isSolved() {
        return isSolved;
    }

    /**
     * Changes the solved state of this content.
     * 
     * Only subclasses should use this method.
     *
     * @param isSolved
     */
    protected void setSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }
}

package org.estg.ed.Content;

import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Players.Player;

public abstract class Content {
    private final int idContent;
    private boolean solved;

    public Content(int idContent) {
        this.idContent = idContent;
        this.solved = false;
    }

    public abstract DivisionType getType();

    /**
     * Executes the interaction between the player and the content.
     * 
     * This method must be implemented by subclasses with the specific logic
     * for each content type.
     * 
     * @param p the current player
     *
     * @return true if the interaction was successful, false otherwise
     */
    public abstract boolean interactType(Player p);

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
        return solved;
    }

    /**
     * Changes the solved state of this content.
     * 
     * Only subclasses should use this method.
     *
     * @param isSolved
     */
    protected void setSolved(boolean isSolved) {
        this.solved = isSolved;
    }
}

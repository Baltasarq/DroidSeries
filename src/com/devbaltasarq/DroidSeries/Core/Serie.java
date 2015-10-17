package com.devbaltasarq.DroidSeries.Core;

/**
 * Represents a succession of episodes
 * Created by Baltasar on 17/10/2015.
 */
public class Serie {
    private int lastSeason;
    private int lastEpisode;
    private String name;

    public Serie(String name) throws IllegalArgumentException
    {
        name = name.trim();

        if ( name.isEmpty() ) {
            throw new IllegalArgumentException( "name cannot be empty" );
        }

        this.lastSeason = 1;
        this.lastEpisode = 1;
        this.name = name;
    }

    /**
     * Resets the episode number
     */
    public void resetEpisode() {
        this.lastEpisode = 1;
    }

    /**
     * Increments the last episode seen
     */
    public void incEpisode() {
        ++this.lastEpisode;
    }

    /** Descrements the last episode seen */
    public void decEpisode() {
        --this.lastEpisode;
    }

    /**
     * Increments the last episode seen-
     */
    public void incSeason() {
        ++this.lastSeason;
        this.lastEpisode = 1;
    }

    /**
     * @return the last season seen, as a number
     */
    public int getLastSeason() {
        return lastSeason;
    }

    /**
     * Changes the last season seen.
     * @param lastSeason the last season, as a number
     */
    public void setLastSeason(int lastSeason) {
        this.lastSeason = lastSeason;
    }

    /**
     * @return the last episode seen, as a number
     */
    public int getLastEpisode() {
        return lastEpisode;
    }

    /**
     * Changes the last episode seen.
     * @param lastEpisode the new episode number.
     */
    public void setLastEpisode(int lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    /**
     * @return the name of the series
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format( "%s %02dx%02d", this.getName(), this.getLastSeason(), this.getLastEpisode() );
    }
}

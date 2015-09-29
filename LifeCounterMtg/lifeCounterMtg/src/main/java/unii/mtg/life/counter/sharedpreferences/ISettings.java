package unii.mtg.life.counter.sharedpreferences;


public interface ISettings {
    /**
     * Check if this application is run first time
     *
     * @return true if app is running first time <br>
     * in other case return false
     */
    public boolean isFirstRun();

    /**
     * Set if app should behave as app run as first time
     *
     * @param isFirstRun
     */
    public void setFirstRun(boolean isFirstRun);


}

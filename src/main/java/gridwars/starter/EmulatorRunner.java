package gridwars.starter;

import cern.ais.gridwars.Emulator;

/**
 * Instantiates the example bots and starts the game emulator.
 */
public class EmulatorRunner {

    public static void main(String[] args) {
        GodParticle redBot = new GodParticle();
        GodParticle blueBot = new GodParticle();

        Emulator.playMatch(blueBot, redBot);
    }
}

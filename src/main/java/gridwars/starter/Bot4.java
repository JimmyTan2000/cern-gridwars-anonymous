package gridwars.starter;

import cern.ais.gridwars.api.Coordinates;
import cern.ais.gridwars.api.UniverseView;
import cern.ais.gridwars.api.bot.PlayerBot;
import cern.ais.gridwars.api.command.MovementCommand;

import java.util.List;

public class Bot4 implements PlayerBot {

    private static final double POPULATION_SOFT_LIMIT = 2.0;

    @Override
    public void getNextCommands(UniverseView universeView, List<MovementCommand> commandList) {
        for (Coordinates cell : universeView.getMyCells()) {
            int currentPopulation = universeView.getPopulation(cell);

            if (currentPopulation > (POPULATION_SOFT_LIMIT / (universeView.getGrowthRate() - 1))) {
                // Check neighboring cells for enemies
                if (!hasEnemiesInNeighborhood(universeView, cell)) {
                    // If no nearby enemies, expand aggressively in all directions
                    splitPopulationEquallyInAllDirections(universeView, cell, commandList);
                } else {
                    // If nearby enemies, attack the nearest enemy cell
                    getDirectionToNearestEnemy(universeView, cell, commandList);
                }
            }
        }
    }

    private boolean hasEnemiesInNeighborhood(UniverseView universeView, Coordinates cell) {
        for (int i = 1; i <= 2; i++) { // Check neighboring cells within 2 units
            if (!universeView.belongsToMe(cell.getUp(i)) ||
                    !universeView.belongsToMe(cell.getDown(i)) ||
                    !universeView.belongsToMe(cell.getLeft(i)) ||
                    !universeView.belongsToMe(cell.getRight(i))) {
                return true;
            }
        }
        return false;
    }

    private void splitPopulationEquallyInAllDirections(UniverseView universeView, Coordinates cell, List<MovementCommand> commandList) {
        int split = 4;
        int populationToSplit = universeView.getPopulation(cell) / split;
        commandList.add(new MovementCommand(cell, MovementCommand.Direction.LEFT, populationToSplit));
        commandList.add(new MovementCommand(cell, MovementCommand.Direction.RIGHT, populationToSplit));
        commandList.add(new MovementCommand(cell, MovementCommand.Direction.UP, populationToSplit));
        commandList.add(new MovementCommand(cell, MovementCommand.Direction.DOWN, populationToSplit));
    }

//    private void attackNearestEnemy(UniverseView universeView, Coordinates cell, List<MovementCommand> commandList) {
//        // Determine the direction of the nearest enemy cell
//        MovementCommand.Direction direction = getDirectionToNearestEnemy(universeView, cell, commandList);
//
//        // Attack the nearest enemy cell aggressively with half of the population
//        commandList.add(new MovementCommand(cell, direction, universeView.getPopulation(cell) / 2));
//    }

    private void getDirectionToNearestEnemy(UniverseView universeView, Coordinates cell, List<MovementCommand> commandList) {
        // Check neighboring cells for enemies and return the direction of the nearest one
        // Priority: UP > DOWN > LEFT > RIGHT
        if (!universeView.belongsToMe(cell.getUp())) {
            commandList.add(new MovementCommand(cell, MovementCommand.Direction.UP, universeView.getPopulation(cell) / 4));
        } else if (!universeView.belongsToMe(cell.getDown())) {
            commandList.add(new MovementCommand(cell, MovementCommand.Direction.DOWN, universeView.getPopulation(cell) / 4));
        } else if (!universeView.belongsToMe(cell.getLeft())) {
            commandList.add(new MovementCommand(cell, MovementCommand.Direction.LEFT, universeView.getPopulation(cell) / 4));
        } else if (!universeView.belongsToMe(cell.getRight())) {
            commandList.add(new MovementCommand(cell, MovementCommand.Direction.RIGHT, universeView.getPopulation(cell) / 4));
        } else {
            commandList.add(new MovementCommand(cell, MovementCommand.Direction.LEFT, universeView.getPopulation(cell) / 4));
            commandList.add(new MovementCommand(cell, MovementCommand.Direction.RIGHT, universeView.getPopulation(cell) / 4));
            if ((universeView.getPopulation(cell) > (POPULATION_SOFT_LIMIT / (universeView.getGrowthRate() - 1)))) {
                commandList.add(new MovementCommand(cell, MovementCommand.Direction.UP, universeView.getPopulation(cell) / 4));
                commandList.add(new MovementCommand(cell, MovementCommand.Direction.DOWN, universeView.getPopulation(cell) / 4));
            }
        }

    }
}

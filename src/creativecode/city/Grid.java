package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PApplet.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pathfinder.AshManhattan;
import pathfinder.Graph;
import pathfinder.GraphEdge;
import pathfinder.GraphNode;
import pathfinder.GraphSearch_Astar;
import pathfinder.IGraphSearch;
import processing.core.PVector;
import util.Numbers;
import creativecode.city.GridCell.CellState;
import creativecode.city.Street.DebugPath;

public class Grid {

    final static int   COLOR         = 0xff770078;

    final static float WEIGHT        = 1;

    final static float cellDimension = 20;

    GridCell[][]       cellGrid;

    Graph              pathFinderGraph;

    IGraphSearch       pathFinder;

    List<GridCell>     currentBlock  = new ArrayList<GridCell>();

    List<Street>       streets       = new ArrayList<Street>();

    final static float straightCost  = 1;

    final static float diagonalCost  = straightCost * sqrt(2);

    public Grid() {

        pathFinderGraph = new Graph();

        cellGrid = new GridCell[(int) ($.width / cellDimension)][(int) ($.height / cellDimension)];
        for (int gridX = 0; gridX < getMaxGridX(); gridX++) {

            for (int gridY = 0; gridY < getMaxGridY(); gridY++) {

                GridCell cell = new GridCell(getX(gridX), getY(gridY));
                cellGrid[gridX][gridY] = cell;
                pathFinderGraph.addNode(cell.graphNode);

                if (gridX > 0) {
                    if (gridY > 0) {
                        pathFinderGraph.addEdge(
                                cell.nodeId,
                                cellGrid[gridX - 1][gridY].nodeId,
                                straightCost,
                                straightCost);
                        pathFinderGraph.addEdge(
                                cell.nodeId,
                                cellGrid[gridX - 1][gridY - 1].nodeId,
                                diagonalCost,
                                diagonalCost);
                        pathFinderGraph.addEdge(
                                cell.nodeId,
                                cellGrid[gridX][gridY - 1].nodeId,
                                straightCost,
                                straightCost);
                    } else {
                        pathFinderGraph.addEdge(
                                cell.nodeId,
                                cellGrid[gridX - 1][gridY].nodeId,
                                straightCost,
                                straightCost);
                    }
                    if (gridY < getMaxGridY() - 1) {
                        pathFinderGraph.addEdge(
                                cell.nodeId,
                                cellGrid[gridX - 1][gridY + 1].nodeId,
                                diagonalCost,
                                diagonalCost);
                    }
                } else if (gridY > 0) {
                    pathFinderGraph.addEdge(cell.nodeId, cellGrid[gridX][gridY - 1].nodeId, straightCost, straightCost);
                }

            }
        }

        pathFinder = new GraphSearch_Astar(pathFinderGraph, new AshManhattan());
    }

    void visualizePathFinderGraph() {
        $.fill(255, 0, 0);
        $.noStroke();
        for (GraphNode node : pathFinderGraph.getNodeArray(new GraphNode[0])) {
            $.ellipse(node.xf(), node.yf(), 8, 8);
        }

        $.stroke(255, 255, 0);
        $.strokeWeight(1);
        for (GraphEdge edge : pathFinderGraph.getAllEdgeArray()) {
            $.line(edge.from().xf(), edge.from().yf(), edge.to().xf(), edge.to().yf());
        }
    }

    void draw() {
//        visualizePathFinderGraph();

//        $.stroke(COLOR);
//        $.strokeWeight(WEIGHT);
//
//        for (float x = cellDimension; x < $.width; x += cellDimension) {
//            $.line(x, 0, x, $.height);
//        }
//
//        for (float y = cellDimension; y < $.height; y += cellDimension) {
//            $.line(0, y, $.width, y);
//        }

        for (GridCell[] column : cellGrid) {
            for (GridCell cell : column) {
                $.fill(COLOR);
                $.noStroke();
                $.ellipse(cell.x, cell.y, 6, 6);

                if (cell.state == CellState.BUILT) {
                    cell.draw();
                }
            }
        }

        for (Street street : streets) {
            street.draw();
        }
    }

    float getX(int gridX) {
        return (cellDimension / 2) + gridX * cellDimension;
    }

    float getY(int gridY) {
        return (cellDimension / 2) + gridY * cellDimension;
    }

    int getGridX(float x) {
        return (int) (x / cellDimension);
    }

    int getGridY(float y) {
        return (int) (y / cellDimension);
    }

    GridCell getCell(float x, float y) {
        return cellGrid[getGridX(x)][getGridY(y)];
    }

    boolean isState(int gridX, int gridY, CellState state) {
        GridCell cell = cellGrid[gridX][gridY];
        if (cell == null) {
            return state == CellState.EMPTY;
        }
        return state == cell.state;
    }

    void changeState(int gridX, int gridY, CellState newState) {
        float x = getX(gridX);
        float y = getY(gridY);

        GridCell cell = cellGrid[gridX][gridY];
        switch (newState) {
            case BUILT:

                cell.building =
                        new Building(
                                x + $.buildPadding,
                                y + $.buildPadding,
                                cellDimension - $.buildPadding * 2,
                                cellDimension - $.buildPadding * 2);
                currentBlock.add(cell);
                pathFinderGraph.removeNode(cell.nodeId);
                break;
            case EMPTY:
                // TODO re-add the graph node with all edges
                break;
            default:
        }

        cell.state = newState;
    }

    /**
     * Schließt den momentanen Häuserblock ab und erzeugt eine Straße dazu.
     */
    void finishBlock() {
        if (currentBlock.isEmpty()) {
            return;
        }

        /*
         * Find center of block
         */
        float minX = $.width;
        float maxX = 0;
        float minY = $.height;
        float maxY = 0;

        for (GridCell cell : currentBlock) {
            if (cell.x < minX) {
                minX = cell.x;
            }
            if (cell.x > maxX) {
                maxX = cell.x;
            }
            if (cell.y < minY) {
                minY = cell.y;
            }
            if (cell.y > maxY) {
                maxY = cell.y;
            }
        }

        List<DebugPath> path = new ArrayList<DebugPath>();

        PVector blockCenter = new PVector((minX + maxX) / 2, (minY + maxY) / 2);
        path.add(new DebugPath("Block Center", getCell(blockCenter.x, blockCenter.y)));

        List<GraphNode> streetNodes = new ArrayList<GraphNode>();

        int side = Numbers.random(1, 4);

        GridCell startCell = null;
        GridCell blockStartCell = null;

        switch (side) {
            case 1:
                // Top
                startCell = cellGrid[Numbers.random(0, getMaxGridX() - 1)][0];
                blockStartCell = cellGrid[getGridX(blockCenter.x)][getGridY(minY) - 1];
                break;
            case 2:
                // Right
                startCell = cellGrid[getMaxGridX() - 1][Numbers.random(0, getMaxGridY() - 1)];
                blockStartCell = cellGrid[getGridX(maxX) + 1][getGridY(blockCenter.y)];
                break;
            case 3:
                // Bottom
                startCell = cellGrid[Numbers.random(0, getMaxGridX() - 1)][getMaxGridY() - 1];
                blockStartCell = cellGrid[getGridX(blockCenter.x)][getGridY(maxY) + 1];
                break;
            case 4:
                // Left
                startCell = cellGrid[0][Numbers.random(0, getMaxGridY() - 1)];
                blockStartCell = cellGrid[getGridX(minX) - 1][getGridY(blockCenter.y)];
                break;
            default:
                break;
        }

        path.add(new DebugPath("Start Cell", getCell(startCell.x, startCell.y)));
        path.add(new DebugPath("Block Start Cell", getCell(blockStartCell.x, blockStartCell.y)));

        pathFinder.search(startCell.nodeId, blockStartCell.nodeId);
        streetNodes.addAll(Arrays.asList(pathFinder.getRoute()));
        if (!streetNodes.isEmpty()) {
            streetNodes.remove(streetNodes.size() - 1);
        }

        int otherSide = Numbers.random(1, 4);
        if (otherSide == side) {
            otherSide += Numbers.random(1, 3);
            if (otherSide > 4) {
                otherSide -= 4;
            }
        }

        GridCell endCell = null;
        GridCell blockEndCell = null;

        switch (otherSide) {
            case 1:
                // Top
                endCell = cellGrid[Numbers.random(0, getMaxGridX() - 1)][0];
                blockEndCell = cellGrid[getGridX(blockCenter.x)][getGridY(minY) - 1];
                break;
            case 2:
                // Right
                endCell = cellGrid[getMaxGridX() - 1][Numbers.random(0, getMaxGridY() - 1)];
                blockEndCell = cellGrid[getGridX(maxX) + 1][getGridY(blockCenter.y)];
                break;
            case 3:
                // Bottom
                endCell = cellGrid[Numbers.random(0, getMaxGridX() - 1)][getMaxGridY() - 1];
                blockEndCell = cellGrid[getGridX(blockCenter.x)][getGridY(maxY) + 1];
                break;
            case 4:
                // Left
                endCell = cellGrid[0][Numbers.random(0, getMaxGridY() - 1)];
                blockEndCell = cellGrid[getGridX(minX) - 1][getGridY(blockCenter.y)];
                break;
            default:
                break;
        }

        path.add(new DebugPath("Block End Cell", getCell(blockEndCell.x, blockEndCell.y)));
        path.add(new DebugPath("End Cell", getCell(endCell.x, endCell.y)));

        pathFinder.search(blockStartCell.nodeId, blockEndCell.nodeId);
        streetNodes.addAll(Arrays.asList(pathFinder.getRoute()));
        if (!streetNodes.isEmpty()) {
            streetNodes.remove(streetNodes.size() - 1);
        }

        pathFinder.search(blockEndCell.nodeId, endCell.nodeId);
        streetNodes.addAll(Arrays.asList(pathFinder.getRoute()));

        streets.add(new Street(streetNodes, path));

        /*
         * Reset block
         */
        currentBlock = new ArrayList<GridCell>();
    }

    public int getMaxGridX() {
        return cellGrid.length;
    }

    public int getMaxGridY() {
        return cellGrid[0].length;
    }

    public void step() {
        for (GridCell[] column : cellGrid) {
            for (GridCell cell : column) {
                cell.step();
            }
        }
    }
}

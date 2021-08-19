package com.battlesnake.starter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

/**
 * This is a simple Battlesnake server written in Java.
 * <p>
 * For instructions see
 * https://github.com/BattlesnakeOfficial/starter-snake-java/README.md
 */
public class Snake {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final Handler HANDLER = new Handler();
    private static final Logger LOG = LoggerFactory.getLogger(Snake.class);

    /**
     * Main entry point.
     *
     * @param args are ignored.
     */
    public static void main(String[] args) {
        String port = System.getProperty("PORT");
        if (port != null) {
            LOG.info("Found system provided port: {}", port);
        } else {
            LOG.info("Using default port: {}", port);
            port = "8080";
        }
        port(Integer.parseInt(port));
        get("/", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/start", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/move", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/end", HANDLER::process, JSON_MAPPER::writeValueAsString);
    }

    /**
     * Handler class for dealing with the routes set up in the main method.
     */
    public static class Handler {

        /**
         * For the start/end request
         */
        private static final Map<String, String> EMPTY = new HashMap<>();

        /**
         * Generic processor that prints out the request and response from the methods.
         *
         * @param req
         * @param res
         * @return
         */
        public Map<String, String> process(Request req, Response res) {
            try {
                JsonNode parsedRequest = JSON_MAPPER.readTree(req.body());
                String uri = req.uri();
                LOG.info("{} called with: {}", uri, req.body());
                Map<String, String> snakeResponse;
                if (uri.equals("/")) {
                    snakeResponse = index();
                } else if (uri.equals("/start")) {
                    snakeResponse = start(parsedRequest);
                } else if (uri.equals("/move")) {
                    snakeResponse = move(parsedRequest);
                } else if (uri.equals("/end")) {
                    snakeResponse = end(parsedRequest);
                } else {
                    throw new IllegalAccessError("Strange call made to the snake: " + uri);
                }
                LOG.info("Responding with: {}", JSON_MAPPER.writeValueAsString(snakeResponse));
                return snakeResponse;
            } catch (Exception e) {
                LOG.warn("Something went wrong!", e);
                return null;
            }
        }


        /**
         * This method is called everytime your Battlesnake is entered into a game.
         * <p>
         * Use this method to decide how your Battlesnake is going to look on the board.
         *
         * @return a response back to the engine containing the Battlesnake setup
         * values.
         */
        public Map<String, String> index() {
            Map<String, String> response = new HashMap<>();
            response.put("apiversion", "1");
            response.put("author", "Moms Spaghetti");
            response.put("color", "#4A412A");
            response.put("head", "shades");
            response.put("tail", "pixel");
            return response;
        }

        /**
         * This method is called everytime your Battlesnake is entered into a game.
         * <p>
         * Use this method to decide how your Battlesnake is going to look on the board.
         *
         * @param startRequest a JSON data map containing the information about the game
         *                     that is about to be played.
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> start(JsonNode startRequest) {
            LOG.info("START");
            return EMPTY;
        }

        /**
         * This method is called on every turn of a game. It's how your snake decides
         * where to move.
         * <p>
         * Valid moves are "up", "down", "left", or "right".
         *
         * @param moveRequest a map containing the JSON sent to this snake. Use this
         *                    data to decide your next move.
         * @return a response back to the engine containing Battlesnake movement values.
         */
        public Map<String, String> move(JsonNode moveRequest) {
            try {
                LOG.info("Data: {}", JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(moveRequest));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            List<Coords> foodCoords = getCoordsFromNodeArray(moveRequest.get("board").get("food"));
            List<Coords> snake = getSnakeCoords(moveRequest.get("you"));
            List<Coords> otherSnakeCoords = getOtherSnakeCoords(moveRequest.get("board").get("snakes"));
            List<Coords> boardBox = getBoardBox(moveRequest.get("board"));
            List<Coords> badMoves = new ArrayList<>();
            badMoves.addAll(otherSnakeCoords);
            badMoves.addAll(boardBox);

            Coords head = getCoordFromNode(moveRequest.get("you").get("head"));
            List<Coords> possible = getPossibleMoves(badMoves, head);

            Coords targetCoords = null;
            if (!foodCoords.isEmpty() && moveRequest.get("you").get("health").asInt() <= 25) {
                targetCoords = findClosestAvailableFood(head, foodCoords, otherSnakeCoords);
            }

            String move;
            if (targetCoords != null) {
                move = head.getMove(getMoveToTarget(targetCoords, possible));
            } else {
                Random rand = new Random();
                Coords randomMove = possible.get(rand.nextInt(possible.size()));
                move = head.getMove(randomMove);
            }

            //GOALS (in order):
            //Have a stalling function - run early on to waste time and have board control
            //Have a fighting function - find a way to eliminate opponents
            //Blend all the above together to make a decent spaghetti monster


            String log1 = badMoves.stream()
                    .map(Coords::toString)
                    .collect(Collectors.joining("-", "{", "}"));
            String log2 = possible.stream()
                    .map(Coords::toString)
                    .collect(Collectors.joining("-", "{", "}"));
            LOG.info("BadMoves: ");
            LOG.info(log1);
            LOG.info("PossibleMoves: ");
            LOG.info(log2);

            LOG.info("MOVE {}", move);

            Map<String, String> response = new HashMap<>();
            response.put("move", move);
            return response;
        }

        /**
         * This method is called when a game your Battlesnake was in ends.
         * <p>
         * It is purely for informational purposes, you don't have to make any decisions
         * here.
         *
         * @param endRequest a map containing the JSON sent to this snake. Use this data
         *                   to know which game has ended
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> end(JsonNode endRequest) {

            LOG.info("END");
            return EMPTY;
        }

        private List<Coords> getPossibleMoves(List<Coords> badMoves, Coords ourHead) {
            List<Coords> result = new ArrayList<>();
            HashMap<String, Coords> badMovesMap = new HashMap<>();
            for (Coords coord : badMoves) {
                badMovesMap.put(String.format("%d:%d", coord.x, coord.y), coord);
            }

            for (Coords coord : ourHead.getSurroundingNodes()) {
                if (badMovesMap.containsKey(String.format("%d:%d", coord.x, coord.y))) continue;
                result.add(coord);
            }

            return result;
        }

        private List<Coords> getBoardBox(JsonNode board) {
            if (board.isNull() || board.isArray())
                return Collections.emptyList();

            int height = board.get("height").asInt();
            int width = board.get("width").asInt();
            List<Coords> boardBox = new ArrayList<>();

            for (int i = 0; i < height; i++) {
                boardBox.add(new Coords(-1, i));
                boardBox.add(new Coords(11, i));
            }
            for (int i = 0; i < width; i++) {
                boardBox.add(new Coords(i, -1));
                boardBox.add(new Coords(i, 11));
            }

            return boardBox;

            //board is 11 x 11
            // [width, height]
            // [-1,-1], [-1,0], ... [-1,11]
            // [-1,-1], [0,-1], ... [11,-1]
            // [11,-1], [11,0], ... [11,11]
            // [-1,11], [0,11], ... [11,11]
            // corners don't matter
        }

        private List<Coords> getSnakeCoords(JsonNode snake) {
            if (snake.isNull() || snake.isArray()) return Collections.emptyList();

            List<Coords> coords = new ArrayList<>();

            coords.addAll(getCoordsFromNodeArray(snake.get("body")));

            return coords;
        }

        private List<Coords> getOtherSnakeCoords(JsonNode snakeNodeArray) {
            if (snakeNodeArray.isNull() || !snakeNodeArray.isArray()) return Collections.emptyList();

            List<Coords> coords = new ArrayList<>();

            for (JsonNode snakeNode : snakeNodeArray) {
                coords.addAll(getCoordsFromNodeArray(snakeNode.get("body")));
            }

            return coords;
        }

        private List<Coords> getCoordsFromNodeArray(JsonNode nodeArray) {
            if (nodeArray.isNull() || !nodeArray.isArray()) return Collections.emptyList();

            List<Coords> coords = new ArrayList<>();

            for (JsonNode bodyNode : nodeArray) {
                coords.add(new Coords(bodyNode.get("x").asInt(), bodyNode.get("y").asInt()));
            }

            return coords;
        }

        private Coords getCoordFromNode(JsonNode node) {
            if (node.isNull() || node.isArray()) return null;

            return new Coords(node.get("x").asInt(), node.get("y").asInt());
        }

        private Coords findClosestAvailableFood(Coords ourSnakeHead, List<Coords> foodList, List<Coords> otherSnakes) {
            if (foodList.isEmpty()) return null;

            Coords closestAnyFoodCoords = null;
            Coords closestSafeFoodCoords = null;
            int distanceToClosestAnyFood = 0;

            for (Coords food : foodList) {
                int distanceToFood = getTotalDistanceBetweenCoords(food, ourSnakeHead);

                // Check this if this is the first food found or is the closest food
                if (distanceToClosestAnyFood == 0 || distanceToFood < distanceToClosestAnyFood) {
                    closestAnyFoodCoords = food;
                    distanceToClosestAnyFood = distanceToFood;

                    // Check if this is the closest food not closer to another snake
                    if (distanceToFood <= getOtherSnakeClosestDistanceToFood(food, otherSnakes)) {
                        closestSafeFoodCoords = food;
                    }
                }
            }

            return closestSafeFoodCoords != null ? closestSafeFoodCoords : closestAnyFoodCoords;
        }

        private int getTotalDistanceBetweenCoords(Coords coordsA, Coords coordsB) {
            return Math.abs(coordsA.x - coordsB.x) + Math.abs(coordsA.y - coordsB.y);
        }

        private int getOtherSnakeClosestDistanceToFood(Coords food, List<Coords> otherSnakes) {
            int distanceToClosestFood = 0;

            for (Coords otherSnake: otherSnakes) {
                int distanceToFood = getTotalDistanceBetweenCoords(food, otherSnake);

                if (distanceToClosestFood == 0 || distanceToFood < distanceToClosestFood) {
                    distanceToClosestFood = distanceToFood;
                }
            }

            return distanceToClosestFood;
        }

        private Coords getMoveToTarget(Coords target, List<Coords> possibleList) {
            int distanceToClosestPossible = 0;
            Coords closestPossibleCoords = null;

            for (Coords possible: possibleList) {
                int distanceToTarget = getTotalDistanceBetweenCoords(target, possible);

                if (distanceToClosestPossible == 0 || distanceToTarget < distanceToClosestPossible) {
                    closestPossibleCoords = possible;
                    distanceToClosestPossible = distanceToTarget;
                }
            }

            return closestPossibleCoords;
        }
    }
}

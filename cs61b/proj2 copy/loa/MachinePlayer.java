/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;

import static loa.Piece.*;
import static loa.Utils.*;
/** An automated Player.
 *  @author David Dai
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || board.gameOver()) {
            return heuristica(side());
        }
        int bestScore = 0;
        List<Move> legality = board.legalMoves();
        for (Move move : legality) {
            Board testBoard = new Board();
            testBoard.copyFrom(board);
            testBoard.makeMove(move);
            int newSense = sense * -1;
            int score = findMove(testBoard, depth - 1,
                    false, newSense, alpha, beta);
            if (newSense == -1) {
                if (alpha < score) {
                    alpha = score;
                    if (saveMove) {
                        _foundMove = move;
                    }
                    if (alpha >= beta) {
                        bestScore = alpha;
                        break;
                    }


                }
            } else {
                if (beta > score) {
                    beta = score;
                    if (saveMove) {
                        _foundMove = move;
                    }
                    if (alpha >= beta) {
                        bestScore = beta;
                        break;
                    }
                }

            }
        }
        return bestScore;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 3;
    }

    /** the heuristic function we deserve.
     * @param  piece is the piece we gonna use *
     * @return  the score you want */
    private int heuristica(Piece piece) {
        List<Integer> regionSizes = this.getBoard().getRegionSizes(piece);
        int numPieces = 0;
        for (int i = 0; i < regionSizes.size(); i++) {
            numPieces += regionSizes.get(i);
        }
        int numRegions = regionSizes.size();
        int biggest = regionSizes.get(0);
        return (12 - numPieces) + (12 - numRegions) + biggest;

    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}

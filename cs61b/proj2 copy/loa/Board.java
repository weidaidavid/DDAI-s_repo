/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author David Dai
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        int ind = 0;
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[0].length; j++) {
                Square a = sq(j, i);
                _board[a.index()] = contents[i][j];


            }
        }
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }

        _moves.clear();
        _moves.addAll(board._moves);
        Arrays.fill(_board, null);
        for (int i = 0; i < board._board.length; i++) {
            _board[i] = board._board[i];
        }
        _turn = board._turn;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;

        if (next != null) {
            _turn = next;
        }

    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        Square sq1 = move.getFrom();
        Square sq2 = move.getTo();
        if (get(sq2) != EMP) {
            move = move.captureMove();
        }
        _moves.add(move);
        set(sq2, get(sq1));
        set(sq1, EMP);
        _turn = _turn.opposite();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move lastMove = _moves.remove(_moves.size() - 1);
        Piece curr = get(lastMove.getTo());
        if (lastMove.isCapture()) {
            set(lastMove.getFrom(), curr);
            set(lastMove.getTo(), curr.opposite());
            _turn = _turn.opposite();
        } else {
            set(lastMove.getFrom(), curr);
            set(lastMove.getTo(), EMP);
            _turn = _turn.opposite();
        }


    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (get(from) != _turn) {
            return false;
        } else if (blocked(from, to)) {
            return false;
        } else if (!from.isValidMove(to)) {
            return false;
        } else {
            return from.distance(to) == pcsAlongAxis(from, to);
        }
    }

    /** returns number of pieces along axis of movement.
     * @param  from is the square you're from
     * @param to is the square you're going to*/
    int pcsAlongAxis(Square from, Square to) {
        int yuhDatWay = from.direction(to);
        int nahDisWay = to.direction(from);
        return pcs1d(yuhDatWay, from) + pcs1d(nahDisWay, from) - 1;
    }
    /** returns number of pieces along one axis of movement.
     * @param dir is the direction you're pointed at
     * @param st is the square your on*/
    int pcs1d(int dir, Square st) {
        if (st == null) {
            return 0;
        } else if (get(st) != EMP) {
            return 1 + pcs1d(dir, st.moveDest(dir, 1));
        } else {
            return 0 + pcs1d(dir, st.moveDest(dir, 1));
        }
    }
    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> legalMoves = new ArrayList<>();
        for (Square square : ALL_SQUARES) {
            if (get(square) == _turn) {
                for (int i = 0; i <= 7; i++) {
                    Square square3;
                    Square square1 = square.moveDest(i, 1);
                    int opp = 0;
                    if (i == 0 || i == 1 || i == 2 || i == 3) {
                        opp = i + 4;
                    } else {
                        opp = i - 4;
                    }
                    Square square2 = square.moveDest(opp, 1);
                    if (square1 == null) {
                        square3 = square2;
                    } else {
                        square3 = square1;
                    }
                    if (square3 != null) {
                        int pieces = pcsAlongAxis(square, square3);
                        Square to = square.moveDest(i, pieces);
                        if (to != null && isLegal(square, to)) {
                            legalMoves.add(Move.mv(square, to));
                        }
                    } else {
                        Square to = null;
                        if (to != null && isLegal(square, to)) {
                            legalMoves.add(Move.mv(square, to));
                        }
                    }


                }
            }

        }
        return legalMoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        _subsetsInitialized = false;
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (piecesContiguous(WP) && piecesContiguous(BP)
                    && movesMade() <= _moveLimit) {
                _winner = _turn.opposite();
                _winnerKnown = true;
            } else if (piecesContiguous(BP)) {
                _winner = BP;
                _winnerKnown = true;
            } else if (piecesContiguous(WP)) {
                _winner = WP;
                _winnerKnown = true;
            } else if (_moves.size() >= _moveLimit) {
                _winner = EMP;
                _winnerKnown = true;
            } else {
                _winner = null;
            }

        }

        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        int dir = from.direction(to);
        boolean blockkage = false;
        if (get(to) == get(from)) {
            return true;
        }
        int steps = 1;
        while (!blockkage && steps < from.distance(to)) {
            if (get(from.moveDest(dir, steps)) == get(from).opposite()) {
                blockkage = true;
            }
            steps += 1;
        }
        return blockkage;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (get(sq) == EMP) {
            return 0;
        }
        if (get(sq) != p) {
            return 0;
        }
        if (visited[sq.col()][sq.row()]) {
            return 0;
        }
        int counter = 1;
        visited[sq.col()][sq.row()] = true;
        for (Square sqr : sq.adjacent()) {
            counter += numContig(sqr, visited, p);

        }
        return counter;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[8][8];
        for (Square sqr : ALL_SQUARES) {
            int toBAdd = numContig(sqr, visited, WP);
            if (toBAdd > 0) {
                _whiteRegionSizes.add(toBAdd);
            }
        }
        for (Square sqr : ALL_SQUARES) {
            int toBAdd = numContig(sqr, visited, BP);
            if (toBAdd > 0) {
                _blackRegionSizes.add(toBAdd);
            }
        }

        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }



    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}


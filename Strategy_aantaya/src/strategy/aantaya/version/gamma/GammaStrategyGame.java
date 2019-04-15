/**
 * 
 */
package strategy.aantaya.version.gamma;

import static strategy.StrategyGame.MoveResult.BLUE_WINS;
import static strategy.StrategyGame.MoveResult.GAME_OVER;
import static strategy.StrategyGame.MoveResult.OK;
import static strategy.StrategyGame.MoveResult.RED_WINS;

import java.util.HashSet;
import java.util.Set;

import strategy.Board;
import strategy.StrategyGame;
import strategy.Piece.PieceColor;
import strategy.Piece.PieceType;
import strategy.StrategyGame.MoveResult;
import strategy.aantaya.Square;
import strategy.aantaya.version.beta.BoardImpl;

/**
 * @author Owner
 *
 */
public class GammaStrategyGame implements StrategyGame {

	private BoardImpl board;
	private boolean isRedTurn;
	private boolean gameIsOver;
	private Set<Square> redMoves = new HashSet<>();
	private Set<Square> blueMoves = new HashSet<>();
	private Square previousRedToSquare;
	private Square previousBlueToSquare;
	private int redRepetition = 0;
	private int blueRepetition = 0;
	
	public GammaStrategyGame(Board b) {
		this.board = new BoardImpl(b);
		this.isRedTurn = true;
		this.gameIsOver = false;
	}
	
	//from_row, from_column, to_row, to_column
	public MoveResult move(int fr, int fc, int tr, int tc) {
		//Every move past 16 will return game over
		if(gameIsOver) return GAME_OVER;
		
		Square squareFrom = new Square(fr, fc);
		Square squareTo = new Square(tr, tc);
		
		//If move is not valid, then check piece color. If it's blue red_wins else blue_wins
		if(!isValidMove(squareFrom, squareTo)) {
			gameIsOver = true;
			return (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? RED_WINS : BLUE_WINS;
		}
		
		//In isValidMove() we make sure the correct team is moving so now we can flip this variable
		isRedTurn = !isRedTurn;
		
		//If square is occupied, check it's color
		if(board.isSquareOccupied(squareTo)) {
			//If square is occupied by opposing team, then we go into striking mode
			if(board.getPieceAt(tr, tc).getPieceColor() != board.getPieceAt(fr, fc).getPieceColor()) {
				
				MoveResult result = strike(squareFrom, squareTo);
				
				//This means on the teams has taken the flag, so game over
				if(result != OK) {
					gameIsOver = true;
					return result;
				}
			}
			//If the square is occupied by it's own team then it's an invalid move and opposing team wins
			else {
				gameIsOver = true;
				return (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? RED_WINS : BLUE_WINS;
			}
		}
		else {//It's a valid move and square is not occupied, so move to that square
			board.movePiece(squareFrom, squareTo);
		}
		
		return OK;
	}
	
	/*
	 * In order for a move to be valid:
	 * 	1) Piece must stay within the bounds of the board
	 * 	2) Since we have no scouts in beta strat., a piece cannot move > 1 square
	 * 	3) Piece cannot move diagonally
	 * 	4) Flag cannot move
	 * 	5) Piece must move
	 * 	6) Cannot move over another piece
	*/
	private boolean isValidMove(Square squareFrom, Square squareTo) {
		int yDiff = Math.abs(squareFrom.getRow() - squareTo.getRow());
		int xDiff = Math.abs(squareFrom.getColumn() - squareTo.getColumn());
		
		//Make sure it's the team's turn that is the moving piece
		if((isRedTurn && (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE)) || 
				(!isRedTurn && (board.getTeamAtSquare(squareFrom) == PieceColor.RED))) return false;
		
		//Cannot move to a choke point
		if(isChokePoint(squareTo)) return false;
		
		//Make sure there is a piece at the from square
		if(!board.isSquareOccupied(squareFrom)) return false;
		
		//Piece must move
		if((xDiff == 0) && (yDiff == 0)) return false;
		
		//Within bounds of board
		if(!BoardImpl.isWithinBounds(squareTo)) return false;
		
		//Since no scout, pieces can't move > 1
		if((yDiff > 1) || (xDiff > 1)) return false;
		
		//Piece cannot move diagonally
		if(yDiff == xDiff) return false;
		
		//Flag and bomb cannot move
		if(!board.movablePiece(squareFrom)) return false;
		
		//Check for repetition rule
		if(violatesRepetitionRule(squareFrom, squareTo)) return false;
		
		return true;
	}	
	
	private MoveResult strike(Square squareFrom, Square squareTo) {
		int pieceFrom = rankToInt(board.getPieceAt(squareFrom).getPieceType());
		int pieceTo = rankToInt(board.getPieceAt(squareTo).getPieceType());
		
		//If piece that is being attacked is a flag then attacking team wins
		if(pieceTo == 1) {
			gameIsOver = true;
			return (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? BLUE_WINS : RED_WINS;
		}
		
		//If both pieces are same rank, both get removed from board
		if(pieceFrom == pieceTo) {
			board.removeTwoPieces(squareFrom, squareTo);
			return OK;
		}
		
		if(pieceFrom > pieceTo) board.movePiece(squareFrom, squareTo);
		else board.movePiece(squareTo, squareFrom);
		
		return OK;
	}
	
	private int rankToInt(PieceType type) {
		if(type == PieceType.MARSHAL) return 12;
		else if(type == PieceType.GENERAL) return 11;
		else if(type == PieceType.COLONEL) return 10;
		else if(type == PieceType.MAJOR) return 9;
		else if(type == PieceType.CAPTAIN) return 8;
		else if(type == PieceType.LIEUTENANT) return 7;
		else if(type == PieceType.SERGEANT) return 6;
		else if(type == PieceType.MINER) return 5;
		else if(type == PieceType.SCOUT) return 4;
		else if(type == PieceType.SPY) return 3;
		else if(type == PieceType.BOMB) return 2;
		else return 1;
	}
	
	//This can be modified for all board types and use Square objects
	private boolean isChokePoint(Square s) {
		int row = s.getRow();
		int column = s.getColumn();
		
		if((row == 2 && column == 2) || (row == 2 && column == 3) || 
				(row == 3 && column == 2) || (row == 3 && column == 3)) return true;
		else return false;
	}
	
	private boolean violatesRepetitionRule(Square squareFrom, Square squareTo) {		
		if(isRedTurn) {
			//null check is if it is the first red move of the game
			//(previousRedToSquare != squareFrom) is to check if the square I am moving from is NOT the square 
			//	that I moved to in the previous move
			if(previousRedToSquare == null || !previousRedToSquare.equals(squareFrom)) {
				previousRedToSquare = squareTo;
				redRepetition = 1;
				return false;
			}
			
			redRepetition++;
			
			if(redRepetition > 2) return true;
			
			//we are repeating, but this is only the first or second time that we have moved back and forth from
			//	the same squares
			else {
				previousRedToSquare = squareTo;
				return false;
			}
		}else {
			//null check is if it is the first blue move of the game
			//(previousRedToSquare != squareFrom) is to check if the square I am moving from is NOT the square 
			//	that I moved to in the previous move
			if(previousBlueToSquare == null || !previousBlueToSquare.equals(squareFrom)) {
				previousBlueToSquare = squareTo;
				blueRepetition = 1;
				return false;
			}
			
			blueRepetition++;
			
			if(blueRepetition > 2) return true;
			
			//we are repeating, but this is only the first or second time that we have moved back and forth from
			//	the same squares
			else {
				previousBlueToSquare = squareTo;
				return false;
			}
		}
	}
}

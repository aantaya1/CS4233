/**
 * 
 */
package strategy.aantaya.version.epsilon;

import static strategy.StrategyGame.MoveResult.BLUE_WINS;
import static strategy.StrategyGame.MoveResult.GAME_OVER;
import static strategy.StrategyGame.MoveResult.OK;
import static strategy.StrategyGame.MoveResult.RED_WINS;
import static strategy.StrategyGame.MoveResult.STRIKE_BLUE;
import static strategy.StrategyGame.MoveResult.STRIKE_RED;

import strategy.Board;
import strategy.Piece.PieceColor;
import strategy.Piece.PieceType;
import strategy.StrategyGame;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;
import strategy.aantaya.StrategyGameTemplate;

/**
 * @author Owner
 *
 */
public class EpsilonStrategyGame extends StrategyGameTemplate implements StrategyGame {

	private BoardImpl board;
	private boolean isRedTurn;
	private boolean gameIsOver;
	private Square previousRedToSquare;//square that the red team moved to in previous turn
	private Square previousRedFromSquare;//square that the red team moved from in previous turn
	private Square previousBlueToSquare;//square that the blue team moved to in previous turn
	private Square previousBlueFromSquare;//square that the blue team moved from in previous turn
	private int redRepetition = 0;
	private int blueRepetition = 0;
	private int redNumMovablePieces;
	private int blueNumMovablePieces;
	private int numTurns = 0;
	
	public EpsilonStrategyGame(Board b) {
		this.board = new BoardImpl(b);
		this.isRedTurn = true;
		this.gameIsOver = false;
		this.redNumMovablePieces = 33;
		this.blueNumMovablePieces = 33;
	}
	
	/* (non-Javadoc)
	 * @see strategy.aantaya.StrategyGameTemplate#move(int, int, int, int)
	 */
	@Override
	public MoveResult move(int fr, int fc, int tr, int tc) {
		
		if(gameIsOver) return GAME_OVER;
		
		Square squareFrom = new Square(fr, fc);
		Square squareTo = new Square(tr, tc);
		
		//Make sure there is a piece at the from square
		if(!board.isSquareOccupied(squareFrom)) 
			return (isRedTurn) ? BLUE_WINS : RED_WINS;
		
		//If move is not valid, then check piece color. If it's blue red_wins else blue_wins
		if(!isValidMove(squareFrom, squareTo)) {
			gameIsOver = true;
			return (!isRedTurn) ? RED_WINS : BLUE_WINS;
		}
		
		//In isValidMove() we make sure the correct team is moving so now we can flip this variable
		isRedTurn = !isRedTurn;
		
		MoveResult result;
		
		//If square is occupied, check it's color
		if(board.isSquareOccupied(squareTo)) {
			//If square is occupied by opposing team, then we go into striking mode
			if(board.getPieceAt(tr, tc).getPieceColor() != board.getPieceAt(fr, fc).getPieceColor()) {
				
				result = strike(squareFrom, squareTo);
				
				//This means on the teams has taken the flag, so game over
				if(result == RED_WINS || result == BLUE_WINS) gameIsOver = true;	
			}
			//If the square is occupied by it's own team then it's an invalid move and opposing team wins
			else {
				gameIsOver = true;
				return (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? RED_WINS : BLUE_WINS;
			}
		}
		else {//It's a valid move and square is not occupied, so move to that square
			board.movePiece(squareFrom, squareTo);
			result = OK;
		}
		
		if(redNumMovablePieces <= 0) return BLUE_WINS;
		else if(blueNumMovablePieces <= 0) return RED_WINS;
		
		if(numTurns++ < 2) {
			if(!isRedTurn && !board.rowHasMoveablePieces(6)) return RED_WINS;
			else if(isRedTurn && !board.rowHasMoveablePieces(3)) return BLUE_WINS;
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see strategy.aantaya.StrategyGameTemplate#isValidMove(strategy.aantaya.Square, strategy.aantaya.Square)
	 */
	@Override
	public boolean isValidMove(Square squareFrom, Square squareTo) {	
		int yDiff = Math.abs(squareFrom.getRow() - squareTo.getRow());
		int xDiff = Math.abs(squareFrom.getColumn() - squareTo.getColumn());
		
		//Make sure it's the team's turn that is moving the piece
		if(!isCorrectTeamTurn(squareFrom)) return false;
		
		//Make sure piece is not moving diagonally or more than two squares unless its a scout
		if(!PieceImpl.isValidPhysicalMove(squareFrom, squareTo, board.getPieceAt(squareFrom))) return false;

		if((board.getPieceAt(squareFrom).getPieceType() == PieceType.SCOUT) && ((yDiff > 1) || (xDiff > 1))) {
			 //Invalid move if:
			 //-moving more than three squares and path is NOT clear
			 //-moving three squares or less and path is NOT clear (except for the squareTo, which can only be an enemy piece)
			 
			if(board.isSquareOccupied(squareTo)) {
				if(!board.isClearPath(squareFrom, squareTo, false)) return false;
				if((yDiff > 3) || (xDiff > 3)) return false;
			}else {
				if(!board.isClearPath(squareFrom, squareTo, true)) return false;
			}
		}
		else if((yDiff > 1) || (xDiff > 1)) return false; //if not a scout, then piece cannot move more than one square
		
		//Cannot move to a choke point
		if(BoardImpl.isChokePoint(squareTo)) return false;
		
		//Within bounds of board
		if(!BoardImpl.isWithinBounds(squareTo)) return false;
		
		//Flag and bomb cannot move
		if(!board.movablePiece(squareFrom)) return false;
		
		//Check for repetition rule
		if(violatesRepetitionRule(squareFrom, squareTo)) return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see strategy.aantaya.StrategyGameTemplate#strike(strategy.aantaya.Square, strategy.aantaya.Square)
	 */
	@Override
	public MoveResult strike(Square squareFrom, Square squareTo) {
		int pieceFrom = PieceImpl.rankToInt(board.getPieceAt(squareFrom).getPieceType());
		int pieceTo = PieceImpl.rankToInt(board.getPieceAt(squareTo).getPieceType());
		
		//If piece that is being attacked is a flag then attacking team wins
		if(pieceTo == 1) {
			gameIsOver = true;
			return (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? BLUE_WINS : RED_WINS;
		}
		
		//if pieceTo is a bomber (rank == 2) and pieceFrom is not a miner (rank == 5)
		if(pieceTo == 2 && pieceFrom != 5) {
			if(board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) blueNumMovablePieces--;
			else redNumMovablePieces--;
			
			board.strikeBomb(squareTo);
			
			board.removeOnePiece(squareFrom);			
			return OK;
		}
		
		MoveResult m;
		
		//if pieceFrom is scout and pieceTo is greater that scout do something different
		if(pieceFrom == 4 && (pieceTo > pieceFrom)) {
			if(board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) blueNumMovablePieces--;
			else redNumMovablePieces--;
			
			m = (board.getTeamAtSquare(squareTo) == PieceColor.BLUE) ? STRIKE_BLUE : STRIKE_RED;
			board.removeOnePiece(squareFrom);
		}
		
		//greater rank wins unless its a spy (rank == 3) striking a marshal (rank == 12)
		else if((pieceFrom >= pieceTo) || ((pieceFrom == 3) && (pieceTo == 12))) {
			if(board.getTeamAtSquare(squareTo) == PieceColor.BLUE) blueNumMovablePieces--;
			else redNumMovablePieces--;
			
			m = (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? STRIKE_BLUE : STRIKE_RED;
			
			board.movePiece(squareFrom, squareTo);
		}
		//pieceFrom < pieceTo
		else {
			if(board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) blueNumMovablePieces--;
			else redNumMovablePieces--;
			
			m = (board.getTeamAtSquare(squareTo) == PieceColor.BLUE) ? STRIKE_BLUE : STRIKE_RED;
			
			board.movePiece(squareTo, squareFrom);
		}
		
		return m;
	}
	
	private boolean violatesRepetitionRule(Square squareFrom, Square squareTo) {		
		if(isRedTurn) {
			//null check is if it is the first red move of the game
			//(previousRedToSquare != squareFrom) is to check if the square we are moving from is NOT the square 
			//	that we moved to in the previous move (i.e. we are moving the same piece we moved in the last turn)
			if(previousRedToSquare == null || !previousRedToSquare.equals(squareFrom)) {
				previousRedToSquare = squareTo;
				previousRedFromSquare = squareFrom;
				redRepetition = 1;
				return false;
			}
			
			//I know we are moving the same piece we moved previous turn
			//	now check if we are moving to the piece that we came from the previous turn
			//	if so, index redRepetition, else reset redRepetition
			if(previousRedFromSquare.equals(squareTo))
				if(++redRepetition > 2) return true;
			
			/*
			 * Here either:
			 * 1) We are repeating, but this is only the first or second time that we have moved back 
			 * 		and forth from the same squares
			 * 2) We are moving to a square which is NOT the square that we came from in the previous turn
			 * 
			 * In both cases, we reset the references we have to the squares involved in this move
			 */
			previousRedToSquare = squareTo;
			previousRedFromSquare = squareFrom;
			return false;
			
		}else {
			if(previousBlueToSquare == null || !previousBlueToSquare.equals(squareFrom)) {
				previousBlueToSquare = squareTo;
				previousBlueFromSquare = squareFrom;
				blueRepetition = 1;
				return false;
			}
			
			if(previousBlueFromSquare.equals(squareTo))
				if(++blueRepetition > 2) return true;
			
			previousBlueToSquare = squareTo;
			previousBlueFromSquare = squareFrom;
			return false;
		}
	}
	
	private boolean isCorrectTeamTurn(Square s) {
		boolean blueWrong = (isRedTurn && (board.getTeamAtSquare(s) == PieceColor.BLUE));
		boolean redWrong = (!isRedTurn && (board.getTeamAtSquare(s) == PieceColor.RED));
		
		return !(blueWrong || redWrong);
	}
}

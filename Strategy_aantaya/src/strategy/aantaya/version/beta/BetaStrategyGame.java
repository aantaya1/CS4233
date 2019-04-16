/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2019 Alexander Antaya
 *******************************************************************************/
package strategy.aantaya.version.beta;

import static strategy.StrategyGame.MoveResult.BLUE_WINS;
import static strategy.StrategyGame.MoveResult.GAME_OVER;
import static strategy.StrategyGame.MoveResult.OK;
import static strategy.StrategyGame.MoveResult.RED_WINS;

import strategy.Board;
import strategy.Piece.PieceColor;
import strategy.StrategyGame;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;

/**
 * Description
 * @version Mar 18, 2019
 */

public class BetaStrategyGame implements StrategyGame {
	
	private BoardImpl board;
	private int numTurns;
	private boolean isRedTurn;
	private boolean gameIsOver;
	
	public BetaStrategyGame(Board b) {
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
		
		numTurns++;
		
		if(numTurns > 15) {
			gameIsOver = true;
			return RED_WINS;
		} else return OK;
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
		//Make sure it's the team's turn that is the moving piece
		if(!isCorrectTeamTurn(squareFrom)) return false;
		
		//Make sure there is a piece at the from square
		if(!board.isSquareOccupied(squareFrom)) return false;
		
		if(!PieceImpl.isValidPhysicalMove(squareFrom, squareTo, board.getPieceAt(squareFrom))) return false;
		
		//Within bounds of board
		if(!BoardImpl.isWithinBounds(squareTo)) return false;
		
		//Flag and bomb cannot move
		if(!board.movablePiece(squareFrom)) return false;
		
		return true;
	}	
	
	private MoveResult strike(Square squareFrom, Square squareTo) {
		int pieceFrom = PieceImpl.rankToInt(board.getPieceAt(squareFrom).getPieceType());
		int pieceTo = PieceImpl.rankToInt(board.getPieceAt(squareTo).getPieceType());
		
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
	
	private boolean isCorrectTeamTurn(Square s) {
		return (!(isRedTurn && (board.getTeamAtSquare(s) == PieceColor.BLUE)) || 
				(!isRedTurn && (board.getTeamAtSquare(s) == PieceColor.RED)));
	}
}

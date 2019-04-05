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

import strategy.StrategyGame;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;

import static strategy.StrategyGame.MoveResult.*;
import strategy.Board;
import strategy.NotImplementedException;
import strategy.Piece;
import strategy.Piece.PieceColor;

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
		this.board = (BoardImpl) b;
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
			if(board.getPieceAt(tr, tc).getPieceColor() != board.getPieceAt(tr, tc).getPieceColor()) {
				strike(squareFrom, squareTo);
				numTurns++;
				if(numTurns > 15) {
					gameIsOver = true;
					return RED_WINS;
				}
			}
			//If the square is occupied by it's own team then it's an invalid move and opposing team wins
			else {
				gameIsOver = true;
				return (board.getTeamAtSquare(squareFrom) == PieceColor.BLUE) ? RED_WINS : BLUE_WINS;
			}
		}
		else {//It's a valid move and square is not occupied, so move to that square
			
			//TODO: Move the piece on the board
			board.movePiece(squareFrom, squareTo);
			
			numTurns++;
			if(numTurns > 15) {
				gameIsOver = true;
				return RED_WINS;
			}
			else return OK;
		}
		
		//TODO:Remove this exception...it's only for development purposes
		throw new NotImplementedException("YAH YAH YAH");
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
	private boolean isValidMove(Square from, Square to) {
		int yDiff = Math.abs(from.getRow() - to.getRow());
		int xDiff = Math.abs(from.getColumn() - to.getColumn());
		
		//Make sure it's the team's turn that is the moving piece
		if((isRedTurn && (board.getTeamAtSquare(from) == PieceColor.BLUE)) || 
				(!isRedTurn && (board.getTeamAtSquare(from) == PieceColor.RED))) return false;
		
		//Make sure there is a piece at the from square
		if(!board.isSquareOccupied(from)) return false;
		
		//Piece must move
		if((xDiff == 0) && (yDiff == 0)) return false;
		
		//Within bounds of board
		if(!BoardImpl.isWithinBounds(to)) return false;
		
		//Since no scout, pieces can't move > 1
		if((yDiff > 1) || (xDiff > 1)) return false;
		
		//Piece cannot move diagonally
		if(yDiff == xDiff) return false;
		
		//Flag and bomb cannot move
		if(!board.movablePiece(from)) return false;
		
		return true;
	}	
	
	private MoveResult strike(Square from, Square to) {
	
		board.movePiece(from, to);
		
		return OK;
	}
}

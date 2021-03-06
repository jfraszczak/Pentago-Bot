/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.myplayer;

import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;

public class myPlayer extends Player {

    //oblicza wartosc bezwzgledna ale chyba finalnie z niej nawet nie korzystam xd
	public int abs(int x) {
		if(x < 0) return -x;
		else return x;
	}

    //funkcja do ewaluacji heurystyczej przyznajaca odpowiednia liczbe punktow w zaleznosci jak dobre jest ustawienie planszy
	public int evaluate(Board board) {
    	Color color = getColor();
    	Color opponentColor = getOpponent(color);
    	int N = board.getSize();
    	int W = N / 2 + (N / 2 + 1) / 2;
    	int finalScore = 0;
    	int score;

    	//my rows sprawdza wszystkie wiersze czy wystepuja po sobie nasze kulki
    	for(int x = 0; x < N; x++) {
    		for(int count = 2; count <= 5; count++) {
    			for(int start = 0; start <= N - count; start++) {
        			score = 0;
    				for(int y = start; y < start + count; y++) {
    					if(board.getState(x, y) == color) score++;
        				else {
        					score = 0;
        					break;
        				}
    				}
    				//jesli 2 pod rzad to przyznaje 10 punktow
        			if(score == count && count == 2 && (start == 0 || start == 1 || start == 3 || start == 4)) finalScore += 10;
        			//jesli 3 pod rzad i na krawedzi planszy to 500 a jesli w srodku planszy to 100
        			if(score == count && count == 3) {
        				if(start == 0 || start == 3) finalScore += 500;
        				else finalScore += 100;
        			}
        			//jesli 4 pod rzad
        			if(score == count && count == 4) finalScore += 1000;
        			//jesli 5 pod rzad to daje w pytke duzo puntkow
        			if(score == count && count == 5) finalScore += 100000;
    			}
    		}
    	}
    	for(int i = 0; i < 6; i++) {
    		for(int j = 0; j < 5; j++) {
    			if(board.getState(i,j) == Color.EMPTY && board.getState(i, j+1) == color) finalScore += 300;
    		}
    	}

    	//enemy rows  tak samo tylko ze odejmujemt punkty za korzystne pozycje kulek przeciwnika
    	for(int x = 0; x < N; x++) {
    		for(int count = 2; count <= 5; count++) {
    			for(int start = 0; start <= N - count; start++) {
        			score = 0;
    				for(int y = start; y < start + count; y++) {
    					if(board.getState(x, y) == opponentColor) score++;
        				else {
        					score = 0;
        					break;
        				}
    				}
    				if(score == count && count == 2 && (start == 0 || start == 1 || start == 3 || start == 4)) finalScore -= 10;
        			if(score == count && count == 3) {
        				if(start == 0 || start == 3) finalScore -= 500;
        				else finalScore -= 100;
        			}
        			if(score == count && count == 4) finalScore -= 1000;
        			if(score == count && count == 5) finalScore -= 100000;
    			}
    		}
    	}

    	for(int i = 0; i < 6; i++) {
    		for(int j = 0; j < 5; j++) {
    			if(board.getState(i,j) == Color.EMPTY && board.getState(i, j+1) == opponentColor) finalScore -= 200;
    		}
    	}


    	//my columns to samo tylko dla kolumn
    	for(int y = 0; y < N; y++) {
    		for(int count = 2; count <= 5; count++) {
    			for(int start = 0; start <= N - count; start++) {
        			score = 0;
    				for(int x = start; x < start + count; x++) {
    					if(board.getState(x, y) == color) score++;
        				else {
        					score = 0;
        					break;
        				}
    				}
        			if(score == count && count == 2 && (start == 0 || start == 1 || start == 3 || start == 4)) finalScore += 10;
        			if(score == count && count == 3) {
        				if(start == 0 || start == 3) finalScore += 500;
        				else finalScore += 100;
        			}
        			if(score == count && count == 4) finalScore += 1000;
        			if(score == count && count == 5) finalScore += 100000;
    			}
    		}
    	}

    	for(int j = 0; j < 6; j++) {
    		for(int i = 0; i < 5; i++) {
    			if(board.getState(i,j) == Color.EMPTY && board.getState(i+1, j) == color) finalScore += 300;
    		}
    	}

    	//enemy columns
    	for(int y = 0; y < N; y++) {
    		for(int count = 2; count <= 5; count++) {
    			for(int start = 0; start <= N - count; start++) {
        			score = 0;
    				for(int x = start; x < start + count; x++) {
    					if(board.getState(x, y) == opponentColor) score++;
        				else {
        					score = 0;
        					break;
        				}
    				}
    				if(score == count && count == 2 && (start == 0 || start == 1 || start == 3 || start == 4)) finalScore -= 10;
        			if(score == count && count == 3) {
        				if(start == 0 || start == 3) finalScore -= 500;
        				else finalScore -= 100;
        			}
        			if(score == count && count == 4) finalScore -= 1000;
        			if(score == count && count == 5) finalScore -= 100000;
    			}
    		}
    	}

    	for(int j = 0; j < 6; j++) {
    		for(int i = 0; i < 5; i++) {
    			if(board.getState(i,j) == Color.EMPTY && board.getState(i+1, j) == opponentColor) finalScore -= 200;
    		}
    	}

	    return finalScore;
	}

    //liczy liczbe juz wykonanych ruchow
    public int countMoves(Board board) {
    	int N = board.getSize();
    	int moves = 0;
    	for(int x = 0; x < N; x++) {
    		for(int y = 0; y < N; y++) {
    			if(board.getState(x, y) != Color.EMPTY) moves++;
    		}
    	}
    	return moves;
    }

    public enum Function{
    	MAX,
    	MIN
    };

    int max(int a, int b) {
    	if(a >= b) return a;
    	else return b;
    }

    int min(int a, int b) {
    	if(a <= b) return a;
    	else return b;
    }

    //algorytm alfa beta
    public int min_max(Board board, Color color, int depth, Function function, int alpha, int beta) {
    	if(depth > 0 && board.getWinner(color) == null) {
    		if(function == Function.MAX) {
        		List<Move> moves = board.getMovesFor(color);
        		for(int i = 0; i < moves.size(); i++) {
        			board.doMove(moves.get(i));
        			int score = min_max(board, getOpponent(color), depth - 1, Function.MIN, alpha, beta);
        			board.undoMove(moves.get(i));

        			alpha = max(score, alpha);
        			if(alpha >= beta) return beta;
        		}
        		return alpha;
    		}
    		else{
        		List<Move> moves = board.getMovesFor(color);
        		for(int i = 0; i < moves.size(); i++) {
        			board.doMove(moves.get(i));
        			int score = min_max(board, getOpponent(color), depth - 1, Function.MAX, alpha, beta);
        			board.undoMove(moves.get(i));

        			beta = min(score, beta);
        			if(alpha >= beta) return alpha;
        		}
        		return beta;
    		}
    	}
    	else return evaluate(board);
    }

    @Override
    public Move nextMove(Board board){
    	Color color = getColor();
    	List<Move> moves = board.getMovesFor(color);
    	Board newBoard = board.clone();

    	int max = 0, maxBranch = 0;
		int depth = 3;

		int alpha = -100000000;
		int beta = 100000000;

        //pierwsza glebokosc min maxa gdzie szukany jest najlepszy ruch (schodzimy tylko na glebokosc rowna 3)
    	for(int branch = 0; branch < moves.size(); branch++) {
    		newBoard.doMove(moves.get(branch));
    		int score = min_max(newBoard, getOpponent(color), depth - 1, Function.MIN, alpha, beta);
        	newBoard.undoMove(moves.get(branch));
        	if(max < score) {
        		max = score;
        		maxBranch = branch;
        	}
			alpha = max(score, alpha);
    	}


        return moves.get(maxBranch);
    }

    public static void main(String[] args) {}

}

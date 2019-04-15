package strategy.aantaya;

import java.util.Objects;

public class Square {

	//When we create the square, the rows/columns should be zero based indexing
	private final int column;
	private final int row;
	
	/**
	 * Default constructor
	 */
	public Square(int row, int column)
	{
		this.row = row;
		this.column = column;
	}
	
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Square))
			return false;
		Square other = (Square) obj;
		return column == other.column && row == other.row;
	}

	public boolean equals(Square s) {
		return ((s.getRow() == this.row) && (s.getColumn() == this.column)) ? true : false;
	}
}

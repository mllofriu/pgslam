package brick.utils;

import java.io.PrintStream;

import lejos.util.LUDecomposition;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

public class MatrixFloat {

	/* ------------------------
   Class variables
	 * ------------------------ */

	private static final long serialVersionUID = 5724948160773341967L;

	/** Array for internal storage of elements.
   @serial internal array storage.
	 */
	private float[][] A;

	/** Row and column dimensions.
   @serial row dimension.
   @serial column dimension.
	 */
	private int m, n;

	/* ------------------------
   Constructors
	 * ------------------------ */

	/** Construct an m-by-n MatrixFloat of zeros. 
   @param m    Number of rows.
   @param n    Number of columns.
	 */

	public MatrixFloat (int m, int n) {
		this.m = m;
		this.n = n;
		A = new float[m][n];
	}

	/** Construct an m-by-n constant MatrixFloat.
   @param m    Number of rows.
   @param n    Number of columns.
   @param s    Fill the MatrixFloat with this scalar value.
	 */

	public MatrixFloat (int m, int n, float s) {
		this.m = m;
		this.n = n;
		A = new float[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = s;
			}
		}
	}

	/** Construct a MatrixFloat from a 2-D array.
   @param A    Two-dimensional array of doubles.
   @exception  IllegalArgumentException All rows must have the same length
   @see        #constructWithCopy
	 */

	public MatrixFloat (float[][] A) throws IllegalArgumentException{
		m = A.length;
		n = A[0].length;
		for (int i = 0; i < m; i++) {
			if (A[i].length != n) {
				throw new IllegalArgumentException("All rows must have the same length.");
			}
		}
		this.A = A;
	}

	/** Construct a MatrixFloat quickly without checking arguments.
   @param A    Two-dimensional array of doubles.
   @param m    Number of rows.
   @param n    Number of colums.
	 */

	public MatrixFloat (float[][] A, int m, int n) {
		this.A = A;
		this.m = m;
		this.n = n;
	}

	/** Construct a MatrixFloat from a one-dimensional packed array
   @param vals One-dimensional array of doubles, packed by columns (ala Fortran).
   @param m    Number of rows.
   @exception  IllegalArgumentException Array length must be a multiple of m.
	 */

	public MatrixFloat (float vals[], int m) throws IllegalArgumentException {
		this.m = m;
		n = (m != 0 ? vals.length/m : 0);
		if (m*n != vals.length) {
			throw new IllegalArgumentException("Array length must be a multiple of m.");
		}
		A = new float[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = vals[i+j*m];
			}
		}
	}

	/* ------------------------
   Public Methods
	 * ------------------------ */

	/** Construct a MatrixFloat from a copy of a 2-D array.
   @param A    Two-dimensional array of doubles.
   @exception  IllegalArgumentException All rows must have the same length
	 */

	public static MatrixFloat constructWithCopy(float[][] A) throws IllegalArgumentException{
		int m = A.length;
		int n = A[0].length;
		MatrixFloat X = new MatrixFloat(m,n);
		float[][] C = X.getArray();
		for (int i = 0; i < m; i++) {
			if (A[i].length != n) {
				throw new IllegalArgumentException("All rows must have the same length.");
			}
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j];
			}
		}
		return X;
	}

	/** Make a deep copy of a MatrixFloat
	 */

	public MatrixFloat copy () {
		MatrixFloat X = new MatrixFloat(m,n);
		float[][] C = X.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j];
			}
		}
		return X;
	}

//	public MatrixFloat copy (MatrixFloat m) {
//		for (int i = 0; i < m; i++) {
//			for (int j = 0; j < n; j++) {
//				C[i][j] = A[i][j];
//			}
//		}
//		return X;
//	}

	/** Clone the MatrixFloat object.
	 */

	 public Object clone () {
		 return this.copy();
	 }

	 /** Access the internal two-dimensional array.
   @return     Pointer to the two-dimensional array of MatrixFloat elements.
	  */

	 public float[][] getArray () {
		 return A;
	 }

	 /** Copy the internal two-dimensional array.
   @return     Two-dimensional array copy of MatrixFloat elements.
	  */

	 public float[][] getArrayCopy () {
		 float[][] C = new float[m][n];
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = A[i][j];
			 }
		 }
		 return C;
	 }

	 /** Make a one-dimensional column packed copy of the internal array.
   @return     MatrixFloat elements packed in a one-dimensional array by columns.
	  */

	 public float[] getColumnPackedCopy () {
		 float[] vals = new float[m*n];
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 vals[i+j*m] = A[i][j];
			 }
		 }
		 return vals;
	 }

	 /** Make a one-dimensional row packed copy of the internal array.
   @return     MatrixFloat elements packed in a one-dimensional array by rows.
	  */

	 public float[] getRowPackedCopy () {
		 float[] vals = new float[m*n];
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 vals[i*n+j] = A[i][j];
			 }
		 }
		 return vals;
	 }

	 /** Get row dimension.
   @return     m, the number of rows.
	  */

	 public int getRowDimension () {
		 return m;
	 }

	 /** Get column dimension.
   @return     n, the number of columns.
	  */

	 public int getColumnDimension () {
		 return n;
	 }

	 /** Get a single element.
   @param i    Row index.
   @param j    Column index.
   @return     A(i,j)
	  */

	 public float get (int i, int j) {
		 return A[i][j];
	 }

	 /** Get a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param j0   Initial column index
   @param j1   Final column index
   @return     A(i0:i1,j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public MatrixFloat getMatrix (int i0, int i1, int j0, int j1) throws ArrayIndexOutOfBoundsException{
		 MatrixFloat X = new MatrixFloat(i1-i0+1,j1-j0+1);
		 float[][] B = X.getArray();
		 try {
			 for (int i = i0; i <= i1; i++) {
				 for (int j = j0; j <= j1; j++) {
					 B[i-i0][j-j0] = A[i][j];
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
		 return X;
	 }

	 /** Get a submatrix.
   @param r    Array of row indices.
   @param c    Array of column indices.
   @return     A(r(:),c(:))
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public MatrixFloat getMatrix (int[] r, int[] c) throws ArrayIndexOutOfBoundsException{
		 MatrixFloat X = new MatrixFloat(r.length,c.length);
		 float[][] B = X.getArray();
		 try {
			 for (int i = 0; i < r.length; i++) {
				 for (int j = 0; j < c.length; j++) {
					 B[i][j] = A[r[i]][c[j]];
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
		 return X;
	 }

	 /** Get a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param c    Array of column indices.
   @return     A(i0:i1,c(:))
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public MatrixFloat getMatrix (int i0, int i1, int[] c) throws ArrayIndexOutOfBoundsException{
		 MatrixFloat X = new MatrixFloat(i1-i0+1,c.length);
		 float[][] B = X.getArray();
		 try {
			 for (int i = i0; i <= i1; i++) {
				 for (int j = 0; j < c.length; j++) {
					 B[i-i0][j] = A[i][c[j]];
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
		 return X;
	 }

	 /** Get a submatrix.
   @param r    Array of row indices.
   @param j0   Initial column index
   @param j1   Final column index
   @return     A(r(:),j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public MatrixFloat getMatrix (int[] r, int j0, int j1) throws ArrayIndexOutOfBoundsException{
		 MatrixFloat X = new MatrixFloat(r.length,j1-j0+1);
		 float[][] B = X.getArray();
		 try {
			 for (int i = 0; i < r.length; i++) {
				 for (int j = j0; j <= j1; j++) {
					 B[i][j-j0] = A[r[i]][j];
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
		 return X;
	 }

	 /** Set a single element.
   @param i    Row index.
   @param j    Column index.
   @param s    A(i,j).
   @exception  ArrayIndexOutOfBoundsException
	  */

	 public void set (int i, int j, float s) {
		 A[i][j] = s;
	 }

	 /** Set a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param j0   Initial column index
   @param j1   Final column index
   @param X    A(i0:i1,j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public void setMatrix (int i0, int i1, int j0, int j1, MatrixFloat X) throws ArrayIndexOutOfBoundsException{
		 try {
			 for (int i = i0; i <= i1; i++) {
				 for (int j = j0; j <= j1; j++) {
					 A[i][j] = X.get(i-i0,j-j0);
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
	 }

	 /** Set a submatrix.
   @param r    Array of row indices.
   @param c    Array of column indices.
   @param X    A(r(:),c(:))
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public void setMatrix (int[] r, int[] c, MatrixFloat X) throws ArrayIndexOutOfBoundsException{
		 try {
			 for (int i = 0; i < r.length; i++) {
				 for (int j = 0; j < c.length; j++) {
					 A[r[i]][c[j]] = X.get(i,j);
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
	 }

	 /** Set a submatrix.
   @param r    Array of row indices.
   @param j0   Initial column index
   @param j1   Final column index
   @param X    A(r(:),j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public void setMatrix (int[] r, int j0, int j1, MatrixFloat X) throws ArrayIndexOutOfBoundsException{
		 try {
			 for (int i = 0; i < r.length; i++) {
				 for (int j = j0; j <= j1; j++) {
					 A[r[i]][j] = X.get(i,j-j0);
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
	 }

	 /** Set a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param c    Array of column indices.
   @param X    A(i0:i1,c(:))
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	  */

	 public void setMatrix (int i0, int i1, int[] c, MatrixFloat X) throws ArrayIndexOutOfBoundsException{
		 try {
			 for (int i = i0; i <= i1; i++) {
				 for (int j = 0; j < c.length; j++) {
					 A[i][c[j]] = X.get(i-i0,j);
				 }
			 }
		 } catch(ArrayIndexOutOfBoundsException e) {
			 throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		 }
	 }

	 /** MatrixFloat transpose.
   @return    A'
	  */

	 public MatrixFloat transpose () {
		 MatrixFloat X = new MatrixFloat(n,m);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[j][i] = A[i][j];
			 }
		 }
		 return X;
	 }

	 /** One norm
   @return    maximum column sum.
	  */

	 public float norm1 () {
		 float f = 0;
		 for (int j = 0; j < n; j++) {
			 float s = 0;
			 for (int i = 0; i < m; i++) {
				 s += Math.abs(A[i][j]);
			 }
			 f = Math.max(f,s);
		 }
		 return f;
	 }


	 /** Infinity norm
   @return    maximum row sum.
	  */

	 public float normInf () {
		 float f = 0;
		 for (int i = 0; i < m; i++) {
			 float s = 0;
			 for (int j = 0; j < n; j++) {
				 s += Math.abs(A[i][j]);
			 }
			 f = Math.max(f,s);
		 }
		 return f;
	 }   

	 /** Frobenius norm
   @return    sqrt of sum of squares of all elements.
	  */   

	 public float normF () {
		 float f = 0;
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 f = hypot(f,A[i][j]);
			 }
		 }
		 return f;
	 }

	 /**  Unary minus
   @return    -A
	  */

	 public MatrixFloat uminus () {
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = -A[i][j];
			 }
		 }
		 return X;
	 }

	 /** C = A + B
   @param B    another MatrixFloat
   @return     A + B
	  */

	 public MatrixFloat plus (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = A[i][j] + B.A[i][j];
			 }
		 }
		 return X;
	 }

	 /** A = A + B
   @param B    another MatrixFloat
   @return     A + B
	  */

	 public MatrixFloat plusEquals (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 A[i][j] = A[i][j] + B.A[i][j];
			 }
		 }
		 return this;
	 }

	 /** C = A - B
   @param B    another MatrixFloat
   @return     A - B
	  */

	 public MatrixFloat minus (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = A[i][j] - B.A[i][j];
			 }
		 }
		 return X;
	 }

	 /** A = A - B
   @param B    another MatrixFloat
   @return     A - B
	  */

	 public MatrixFloat minusEquals (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 A[i][j] = A[i][j] - B.A[i][j];
			 }
		 }
		 return this;
	 }

	 /** Element-by-element multiplication, C = A.*B
   @param B    another MatrixFloat
   @return     A.*B
	  */

	 public MatrixFloat arrayTimes (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = A[i][j] * B.A[i][j];
			 }
		 }
		 return X;
	 }


	 /**
	  * 
	  */
	 public MatrixFloat arrayTimesEquals (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 A[i][j] = A[i][j] * B.A[i][j];
			 }
		 }
		 return this;
	 }

	 /** Element-by-element right division, C = A./B
   @param B    another MatrixFloat
   @return     A./B
	  */

	 public MatrixFloat arrayRightDivide (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = A[i][j] / B.A[i][j];
			 }
		 }
		 return X;
	 }

	 /** Element-by-element right division in place, A = A./B
   @param B    another MatrixFloat
   @return     A./B
	  */

	 public MatrixFloat arrayRightDivideEquals (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 A[i][j] = A[i][j] / B.A[i][j];
			 }
		 }
		 return this;
	 }

	 /** Element-by-element left division, C = A.\B
   @param B    another MatrixFloat
   @return     A.\B
	  */

	 public MatrixFloat arrayLeftDivide (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = B.A[i][j] / A[i][j];
			 }
		 }
		 return X;
	 }

	 /** Element-by-element left division in place, A = A.\B
   @param B    another MatrixFloat
   @return     A.\B
	  */

	 public MatrixFloat arrayLeftDivideEquals (MatrixFloat B) {
		 checkMatrixDimensions(B);
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 A[i][j] = B.A[i][j] / A[i][j];
			 }
		 }
		 return this;
	 }

	 /** Multiply a MatrixFloat by a scalar, C = s*A
   @param s    scalar
   @return     s*A
	  */

	 public MatrixFloat times (float s) {
		 MatrixFloat X = new MatrixFloat(m,n);
		 float[][] C = X.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 C[i][j] = s*A[i][j];
			 }
		 }
		 return X;
	 }

	 /** Multiply a MatrixFloat by a scalar in place, A = s*A
   @param s    scalar
   @return     replace A by s*A
	  */

	 public MatrixFloat timesEquals (float s) {
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 A[i][j] = s*A[i][j];
			 }
		 }
		 return this;
	 }

	 /** Linear algebraic MatrixFloat multiplication, A * B
   @param B    another MatrixFloat
   @return     MatrixFloat product, A * B
   @exception  IllegalArgumentException MatrixFloat inner dimensions must agree.
	  */

	 public MatrixFloat times (MatrixFloat B) throws IllegalArgumentException{
		 if (B.m != n) {
			 throw new IllegalArgumentException("MatrixFloat inner dimensions must agree.");
		 }
		 MatrixFloat X = new MatrixFloat(m,B.n);
		 float[][] C = X.getArray();
		 float[] Bcolj = new float[n];
		 for (int j = 0; j < B.n; j++) {
			 for (int k = 0; k < n; k++) {
				 Bcolj[k] = B.A[k][j];
			 }
			 for (int i = 0; i < m; i++) {
				 float[] Arowi = A[i];
				 float s = 0;
				 for (int k = 0; k < n; k++) {
					 s += Arowi[k]*Bcolj[k];
				 }
				 C[i][j] = s;
			 }
		 }
		 return X;
	 }


	 /** MatrixFloat trace.
   @return     sum of the diagonal elements.
	  */

	 public float trace () {
		 float t = 0;
		 for (int i = 0; i < Math.min(m,n); i++) {
			 t += A[i][i];
		 }
		 return t;
	 }

	 /** Generate MatrixFloat with random elements
   @param m    Number of rows.
   @param n    Number of colums.
   @return     An m-by-n MatrixFloat with uniformly distributed random elements.
	  */

	 public static MatrixFloat random (int m, int n) {
		 MatrixFloat A = new MatrixFloat(m,n);
		 float[][] X = A.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 X[i][j] = (float) Math.random();
			 }
		 }
		 return A;
	 }

	 /** Generate identity MatrixFloat
   @param m    Number of rows.
   @param n    Number of colums.
   @return     An m-by-n MatrixFloat with ones on the diagonal and zeros elsewhere.
	  */

	 public static MatrixFloat identity (int m, int n) {
		 MatrixFloat A = new MatrixFloat(m,n);
		 float[][] X = A.getArray();
		 for (int i = 0; i < m; i++) {
			 for (int j = 0; j < n; j++) {
				 X[i][j] = (i == j ? 1.0f : 0.0f);
			 }
		 }
		 return A;
	 }

	 /** MatrixFloat inverse or pseudoinverse
   @return     inverse(A) if A is square, pseudoinverse otherwise.
	  */

	 public MatrixFloat inverse () {
		 return solve(identity(m,m));
	 }

	 /** Solve A*X = B
   @param B    right hand side
   @return     solution if A is square, least squares solution otherwise
	  */

	 public MatrixFloat solve (MatrixFloat B) {
		 // leJOS package only supports square matrices
		 return new LUDecompositionFloat(this).solve(B);
	 }
	 /*
	  * Simple version of MatrixFloat print
	  */
	 public void print(PrintStream out) {
		 for(int i=0;i<m;i++) {
			 for(int j=0;j<n;j++) {
				 System.out.print(A[i][j] + " ");
			 }
			 System.out.println("");
		 }
	 }

	 /* ------------------------
   Private Methods
	  * ------------------------ */

	 /** Check if size(A) == size(B) **/

	 private void checkMatrixDimensions (MatrixFloat B) throws IllegalArgumentException{
		 if (B.m != m || B.n != n) {
			 throw new IllegalArgumentException("MatrixFloat dimensions must agree.");
		 }
	 }

	 /**
	  * sqrt(a^2 + b^2) without under/overflow.
	  * 
	  * @param a
	  * @param b
	  * @return
	  */
	 private float hypot(float a, float b) {
		 float r;
		 if (Math.abs(a) > Math.abs(b)) {
			 r = b/a;
			 r = (float) (Math.abs(a)*Math.sqrt(1+r*r));
		 } else if (b != 0) {
			 r = a/b;
			 r = (float) (Math.abs(b)*Math.sqrt(1+r*r));
		 } else {
			 r = 0.0f;
		 }
		 return r;
	 } 
}


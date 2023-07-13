package fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import math.Complex;
import math.ComplexPolynomial;
import math.ComplexRootedPolynomial;

public class NewtonP1 {


	public static void main(String[] args) {
		
		int numThreads = Runtime.getRuntime().availableProcessors();
		int numTracks = numThreads * 4;
		if(args.length != 0) {
			if(args.length > 4)
				throw new IllegalArgumentException("Maximum number of parameters is two!");
			boolean workersExist = false;
			boolean tracksExist = false;
			for(int i = 0; i < args.length; i++) {
				String s = args[i];
				if(s.startsWith("--workers=") && !workersExist) {
					String num = s.substring(s.indexOf("=") + 1);
					numThreads = collectNum(num);
					workersExist = true;
					continue;
				}
				if(s.startsWith("--tracks=") && !tracksExist) {
					String num = s.substring(s.indexOf("=") + 1);
					numTracks = collectNum(num);
					tracksExist = true;
					continue;
				}
				if(s.equals("-w") && !workersExist) {
					if(i + 1 < args.length) {
						numThreads = collectNum(args[++i]);
						workersExist = true;
						continue;
					}
				}
				if(s.equals("-t") && !tracksExist) {
					if(i + 1 < args.length) {
						numTracks = collectNum(args[++i]);
						tracksExist = true;
						continue;
					}
				}
				System.out.println("Invalid argument(s). Maximum of two arguments allowed.\nSupported arguments"
						+ " are in form of: '--workers=N' (or shorter '-w N') AND '--tracks=K' "
						+ "(or shorter '-t K').\nEach argument should be entered at most once.");
				return;
			}
			if(numTracks <= 1) throw new IllegalArgumentException("Minimal acceptable number of tracks is 1.");
		}
		System.out.println("This program will use " + numThreads + " threads.\n");
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		int rootCount = 1;
		Scanner scanner = new Scanner(System.in);
		List<Complex> roots = new ArrayList<>();
		while(true) {
			System.out.print("Root " + rootCount + "> ");
			if(scanner.hasNextLine()) {
				String input = scanner.nextLine().trim();
				if(input.equals("done")) {
					if(rootCount < 3) {
						System.out.println("Please enter at least two roots, one root per line.");
						continue;
					} else {
						System.out.println("Image of fractal will appear shortly. Thank you.");
						break;
					}
				} 
				try {
					Complex root = takeComplex(input);
					roots.add(root);
				} catch(IllegalArgumentException ex) {
					System.out.println(ex.getMessage());
					continue;
				}
				rootCount++;
			} else {
				System.out.println("Scanner does not have next token!");
			}
		}
		scanner.close();
		Complex[] rootsArray = new Complex[roots.size()];
		roots.toArray(rootsArray);
		
		FractalViewer.show(new MojProducer(rootsArray, numThreads, numTracks));	
	}

	public static class PosaoIzracuna implements Runnable {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;
		public static PosaoIzracuna NO_JOB = new PosaoIzracuna();
		long requestNo;
		IFractalResultObserver observer;
		Complex[] roots;
		
		private PosaoIzracuna() {
		}
		
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel, long requestNo, IFractalResultObserver observer, Complex[] roots) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
			this.requestNo = requestNo;
			this.observer = observer;
			this.roots = roots;
		}
		
		@Override
		public void run() {
			int m = 16*16*16;
			int offset = 0;
			
			ComplexRootedPolynomial rootedPoly = new ComplexRootedPolynomial(
					new Complex(1, 0), this.roots
					);
//			ComplexRootedPolynomial rootedPoly = new ComplexRootedPolynomial(
//					new Complex(1, 0), new Complex(1, 0), new Complex(-1, 0), new Complex(0, 1), new Complex(0, -1)
//					);
			ComplexPolynomial poly = rootedPoly.toComplexPolynom();
			
			short[] data = new short[width * height];
			double cre;
			double cim;
			Complex c;
			Complex zn;
			Complex znold;
			int iter;
			Complex numerator;
			Complex denominator;
			Complex fraction;
			double module = 0;
			for(int y = 0; y < height; y++) {
				if(cancel.get()) break;
				for(int x = 0; x < width; x++) {
					cre = x / (width-1.0) * (reMax - reMin) + reMin;
					cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					c = new Complex(cre, cim);
					zn = c;
					iter = 0;
					do {
						numerator = poly.apply(zn);
						denominator = poly.derive().apply(zn);;
						znold = zn;
						fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = zn.sub(znold).module();
						iter++;
					} while(iter < m && module > 0.001);
					data[offset++] = (short) (rootedPoly.indexOfClosestRootFor(zn, 0.002) + 1);
				}
			}
			observer.acceptResult(data, (short)(poly.order() + 1), requestNo);
//			Mandelbrot.calculate(reMin, reMax, imMin, imMax, width, height, m, yMin, yMax, data, cancel);
		}
	}
	
	
	public static class MojProducer implements IFractalProducer {
		private Complex[] roots;
//		private int numThreads;
		private int numTracks;
		private ExecutorService pool;
		
		
		public MojProducer(Complex[] roots, int numThreads, int numTracks) {
			super();
			this.roots = roots;
//			this.numThreads = numThreads;
			this.numTracks = numTracks;
			this.pool = Executors.newFixedThreadPool(numThreads);
			
//			System.out.println("Number of threads: " + numThreads + " number of tracks: " + numTracks);
		}


		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int m = 16*16*16;
			short[] data = new short[width * height];
			final int brojTraka = this.numTracks;
			System.out.println("Koristen broj traka: " + brojTraka);
			int brojYPoTraci = height / brojTraka;
			

			
			List<Future<?>> rezultati = new ArrayList<Future<?>>();
			
			for(int i = 0; i < brojTraka; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax,
						m, data, cancel, requestNo, observer, roots);
				rezultati.add(this.pool.submit(posao));

			}
			
			for(Future<?> f : rezultati) {
				while(true) {
					try {
						f.get();
						break;
					} catch (InterruptedException | ExecutionException e) {
						System.out.println("Greska pri cekanju i/ili dohvacanju rezultata.");
						return;
					}
				}
			}

			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)m, requestNo);
		}


		@Override
		public void close() {
			
		}


		@Override
		public void setup() {
			//pool
			
		}
	}
	
	/**	Method checks whether the user's input is in form of an appropriate complex number.
	 * 	If it is, method creates an instance of Complex and returns that instance. 	
	 * 
	 * @param String input that user has entered
	 * @return Complex number 
	 * @throws IllegalArgumentException if form of complex number is not valid
	 */
	private static Complex takeComplex(String input) {
		char[] data = input.toCharArray();
		int currentIndex = 0;
		StringBuilder rCollector = new StringBuilder();
		StringBuilder iCollector = new StringBuilder();
		double real = 0;
		double imag = 0;
		boolean isMinus = false;
		if(Character.compare(data[currentIndex], '-') == 0) {
			isMinus = true; 
			currentIndex++;
		}
		while(currentIndex < data.length) {
			if(Character.isDigit(data[currentIndex])) {
				rCollector.append(data[currentIndex++]);
				continue;
			}
			break;
		}
		boolean mustHaveOperator = false;
		if(!rCollector.isEmpty()) {
			real = Double.parseDouble(rCollector.toString());
			mustHaveOperator = true;
		}
		if(isMinus && !rCollector.isEmpty()) {
			isMinus = false;
			real = -real;
		}
		currentIndex = skipBlanks(data, currentIndex);
		if(currentIndex < data.length) {
			if(Character.compare(data[currentIndex], '-') == 0 && !isMinus) {
				isMinus = true;
				currentIndex++;
			} else if(Character.compare(data[currentIndex], '+') == 0 ) {
				currentIndex++;
			} else if(mustHaveOperator) {
				invalidInput();
			}
		}
		currentIndex = skipBlanks(data, currentIndex);
		if(currentIndex < data.length) {
			if (Character.compare(data[currentIndex], 'i') == 0) {
				currentIndex++;
				imag = 1;
			} else 
				invalidInput();
		}
		currentIndex = skipBlanks(data, currentIndex);
		while(currentIndex < data.length) {
			if(Character.isDigit(data[currentIndex])) {
				iCollector.append(data[currentIndex++]);
				continue;
			} else 
				invalidInput();
		}
		if(!iCollector.isEmpty() ) 
			imag = Double.parseDouble(iCollector.toString());
		if(isMinus)
			imag = -imag;
		return new Complex(real, imag);
	}
	
	/** Private method removes all the empty characters such as space, tabulator
	 * 	and tranisitions to a new row.
	 *
	 */
	private static int skipBlanks(char[] data, int currentIndex) {
		while(currentIndex < data.length) {
			char c = data[currentIndex];
			if(c == ' ' || c == '\t' || c == '\r' || c == '\n') {
				currentIndex++;
				continue;
			}
			break;
		}
		return currentIndex;
	} 
	
	/**	Throws error with appropriate message when user's input has not been accepted. 
	 * 	Only purpose of this method is structuring more clean code.
	 * 
	 */
	private static void invalidInput() {
		throw new IllegalArgumentException("Only supported inputs are complex numbers and command 'done'.\n"
				+ "Syntax for complex numbers is of form a+ib or a-ib where parts that are zero can be dropped, but not both.");
	}
	
	/**	Checks if the given String is an appropriate integer value.
	 * 	If it is, returns that integer. If not, throw exception with
	 * 	an apropriate message.
	 * 
	 * 	@param String that is being checked
	 * 	@return integer value if given String was indeed a number
	 * 	@throws IllegalArgumentException if given String is not an
	 * 		appropriate integer
	 */
	private static int collectNum(String input) {
		int res;
		try {
			res = Integer.parseInt(input);
			return res;
		} catch (NumberFormatException ex) {
			System.out.println(input);
			throw new IllegalArgumentException("Argument should be an integer!");
		}
	}
}

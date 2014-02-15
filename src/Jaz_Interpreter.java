import java.io.*;
import java.util.Scanner;
import java.util.Stack;

public class Jaz_Interpreter {

	public static void main(String[] args) throws IOException{
		//Variable Declarations
		String inFile, line, path;
		
		//Object Declarations
		SymbolTable    ST        = new SymbolTable();
		Scanner        cIn       = new Scanner(System.in);
		JazAnalyzer    JA        = new JazAnalyzer();
		JazExecuter    JE        = new JazExecuter();
		JazSymbolTable JazST     = new JazSymbolTable();
		
		System.out.println("Enter Jaz File to Interpret: ");
		inFile = cIn.nextLine();
		path = "src/" + inFile;
		System.out.println("Reading in:" + inFile);
		
		//Populates JazSymbolTable from Jaz file
		JA.Analyze(path);
		
		JazST = JA.getJST();
		JE.execute(JazST);
		//JazST.print();
		
		//doStuff(" cool stuff");
	}
	public static void doStuff(String input){
		System.out.println("doin stuff" + input);
	}

}

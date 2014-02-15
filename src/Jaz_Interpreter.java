import java.io.*;
import java.util.Scanner;
import java.util.Stack;
/************************************
 *                                  *
 * @author Stefan Bradstreet        *
 *                                  *
 *Java Interpreter for the Abstract *
 *                                  *
 *Jaz coding language               *
 *                                  *
 ************************************/
public class Jaz_Interpreter {

	public static void main(String[] args) throws IOException{
		//Variable Declarations
		String inFile, line, path;
		
		//Object Declarations
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
	}
}

import java.util.Stack;


public class JazSymbolTable {
	Instruction[] codeSyntax;
	Instruction toReturn;
	private Stack ReturnStack;
	int tableI, size, returnVal;
	
	JazSymbolTable(){
		ReturnStack   = new Stack();
		size          = 200;
		codeSyntax    = new Instruction[size];
		tableI        = 0;
		returnVal     = 0;
	}
	//return methods
	public Instruction getLine() { 
		toReturn = codeSyntax[tableI];
		tableI++;
		return toReturn;
	}	
	//functions
	public void setTableI(int i) { tableI = i; }
	public void initReturnLine(){
		ReturnStack.push(tableI); //saves the line number for the destination method to return to
	}
	public void callReturn(){
		//used to return code to original line by tracking what method made the return call
		tableI = (int) ReturnStack.pop(); 
	}
	public void addCode(Instruction instrIn){
		if (tableI <= size);{
			codeSyntax[tableI] = instrIn;
			tableI++;
		}
	}
	public void findScope(String scopeIn){
		tableI = 0;
		while (!scopeIn.equals(codeSyntax[tableI].getScope())){
			tableI++;
		}
	}
	public void print(){
		for (int i = 0; i < tableI; i++){
			System.out.println(
				"Instruction = " + codeSyntax[i].getInstr() +
				" : Parameter = " + codeSyntax[i].getParam() +
				": Scope = " + codeSyntax[i].getScope());
		}
	}
}

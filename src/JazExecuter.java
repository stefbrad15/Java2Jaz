import java.util.Stack;
import java.math.BigInteger;

public class JazExecuter {
	private String instruction, parameter, scope, lval, toPop;
	private String [] tokenized;
	private Stack<String> memStack;
	private DataHolder DH;
	private int top, bot, codeLine;
	private Instruction curCode;
	JazExecuter(){
		memStack = new Stack();
		DH       = new DataHolder();
		codeLine = 0;
	}
	
	public void execute(JazSymbolTable TableIn){
		TableIn.setTableI(codeLine);
		curCode = TableIn.getLine();
		instruction = curCode.getInstr();
		parameter   = curCode.getParam();
		
		while(!instruction.equals("halt")){
			switch(instruction){
				//Stack Manipulation
				case   "push"   :  memStack.push(parameter); break;
				case   "lvalue" :
					memStack.push(parameter); break;
				case   "rvalue" :  
					if (DH.get(parameter)== null)
						{DH.put(parameter, "0");
						memStack.push("0");} //initializes variable and pushes 0
					else {
						memStack.push((String) DH.get(parameter));  //pushes value of memory location to stack
					} 
					break; //initializes a variable to 0
				case   "pop"    :  memStack.pop(); break;
				case   ":="     : 
					parameter = memStack.pop();
					lval      = memStack.pop();					
					DH.put(lval, parameter);  
					break;
				case   "copy"   :  memStack.push(memStack.peek()); break;
				
				//control Flow  
				case   "label"   : //step through  
					break;
				case   "goto"    :  TableIn.findScope(parameter); break;
				case   "gofalse" :  
					if (memStack.pop().equals("0")) TableIn.findScope(parameter); break;
				case   "gotrue"  : 
					if (!memStack.pop().equals("0")) TableIn.findScope(parameter); break;
				case   "halt"    :  System.exit(0); break;
				
				//Arithmetic Operators
				case   "+"      :  compTwo(instruction); break;
				case   "-"      :  compTwo(instruction); break;
				case   "*"      :  compTwo(instruction); break;
				case   "/"      :  compTwo(instruction); break;
				case   "div"    :  compTwo(instruction); break;
				
				//Logical Operators
				case   "&"      :  compTwo(instruction); break;
				case   "!"      :  //checks logical value of top of stack
					top = Integer.parseInt(memStack.pop());
					if (top == 0) top = 1; else top = 0;
					memStack.push(Integer.toString(top)); break;
				case   "|"      :  compTwo(instruction); break;
				
				//Relational Operators
				case   "<>"      :  compTwo(instruction); break;
				case   "<="      :  compTwo(instruction); break;
				case   ">="      :  compTwo(instruction); break;
				case   "<"       :  compTwo(instruction); break;
				case   ">"       :  compTwo(instruction); break;
				case   "="       :  compTwo(instruction); break;
				
				//Output
				case   "print"  :  System.out.println(memStack.peek()); break;
				case   "show"   :  
					if(parameter == null) System.out.println();
					else System.out.println(parameter); break;
				
				//Subprogram Control
				case   "begin"      :  //step through; 
					break;
				case   "end"        :  //step through; 
					break;
				case   "return"     :  TableIn.callReturn(); break;
				case   "call"       :  
					TableIn.initReturnLine();
					TableIn.findScope(parameter); break;
				
				//Error Handling
				default         :  System.out.println("Invalid instruction"); break;
			}
			//gets NextLine and steps up Symbol Table Counter
			curCode     = TableIn.getLine();
			instruction = curCode.getInstr();
			parameter   = curCode.getParam();
		}
	}
	public void compTwo(String op){
		top = Integer.parseInt(memStack.pop());
		bot= Integer.parseInt(memStack.pop());
		
		switch(op){
			//Arithmetic Operators
			case   "+"      :  top = bot + top; break;
			case   "-"      :  top = bot - top; break;
			case   "*"      :  top = bot * top; break;
			case   "/"      :  top = bot / top; break;
			case   "div"    :  top = bot % top; break;
			
			//Logical Operators
			case   "&"      :  
				if (top == 1 && top == bot) top = 1; 
				else top = 0; break;
			case   "|"      : 
				if (top == 1 || bot == 1) top = 1; 
				else top = 0; break;
			
			//Relational Operators
			case   "<>"      :  
				if (top == bot) top = 0; 
				else top = 1; break;
			case   "<="      : 
				if (top <=bot) top = 0; 
				else top = 1; break;
			case   ">="      :  
				if (top >= bot) top = 0; 
				else top = 1; break;
			case   "<"       : 
				if (top < bot) top = 0; 
				else top = 1; break;
			case   ">"       :  
				if (top > bot) top = 0; 
				else top = 1; break;
			case   "="       :  
				if (top == bot) top = 0; 
				else top = 1; break;
			
			//Default case (Should never occur)
			default         :  System.out.println("Invalid op"); break;
		}
		memStack.push(Integer.toString(top));			
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	        return true;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    
	}
}
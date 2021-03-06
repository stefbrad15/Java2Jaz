import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Stack;
import java.math.BigInteger;

public class JazExecuter {
	//variable declarations
	private String instruction, parameter, scope, lval, toPop;
	private String [] tokenized;
	private int top, bot, codeLine,level, prevLevel;
	private boolean returning, passing;
		
	//Object Declarations
	Scanner        conIn       = new Scanner(System.in);
	private Stack<String> memStack;
	private HashMap<String, variableParams> DH; 
	private variableParams var;
	private Instruction curCode;
	
	//Default Constructor
	JazExecuter(){
		memStack  = new Stack<String>();
		DH        = new HashMap<String, variableParams>();
		var       = new variableParams("",0,"");
		codeLine  = 0;
		returning = false;
		passing   = false;
	}
	

	public void execute(JazSymbolTable TableIn){
		TableIn.setTableI(codeLine);
		curCode = TableIn.getLine();
		instruction = curCode.getInstr();
		parameter   = curCode.getParam();
		scope = curCode.getScope();
		level = 0; //keeps track of begin and return call
		
		while(!instruction.equals("halt")){

			switch(instruction){
				//Stack Manipulation
				case   "push"   :  memStack.push(parameter); break;
				case   "lvalue" :
					memStack.push(parameter); break;
				case   "rvalue" :  
					//checks instance of variable when returning from a subroutine
					if (returning == true){
						var=(variableParams) DH.get( parameter + " " + prevLevel);
						memStack.push(var.getVal());
					}
					//initializes variable and pushes 0	 
					else if (DH.get(parameter + " "+ scope)== null & DH.get(parameter + " "+ level)== null){						
						if (DH.get(parameter + " "+ prevLevel) != null && passing){
							var=(variableParams) DH.get( parameter + " " + prevLevel);
							memStack.push(var.getVal());
						}
						else{
							var = new variableParams(scope,level,"0");
							DH.put(parameter+ " " +scope, var);
							var = new variableParams(scope,level,"0");
							DH.put(parameter+ " " +level, var);
							memStack.push("0");
						 }
						} 
					else {
						if( DH.get( parameter + " " + level) == null){
							var=(variableParams) DH.get( parameter + " " + scope);
						}
						else{
							var=(variableParams) DH.get( parameter + " " + level);
						}
						memStack.push(var.getVal());  //pushes value of memory location to stack
					} 
					
					break; //initializes a variable to 0
				case   "pop"    :  memStack.pop(); break;
				case   ":="     : 
					parameter = memStack.pop();
					lval      = memStack.pop();		
					var = new variableParams(scope,level,parameter);
					DH.put(lval + " " + scope, var);		//creates a local instance of a variable			
					DH.put(lval + " " + level, var);        //creates an instance by level for variable passing
					DH.put(lval + " " + level + scope, var);//creates an instance of the variable for recursion
					break;
				case   "copy"   :  memStack.push(memStack.peek()); break;

				//control Flow  
				case   "label"   : //step through  
					scope = parameter;
					break;
				case   "goto"    :  TableIn.findScope(parameter); break;
				case   "gofalse" :  
					if (memStack.pop().equals("0")) TableIn.findScope(parameter); break;
				case   "gotrue"  : 
					if (!memStack.pop().equals("0")) TableIn.findScope(parameter); break;
				case   "pause"   : 
					System.out.println("Program Paused-hit enter ");
					String pause = conIn.nextLine(); break;	
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
				case   "begin"  : 
						passing = true;
						prevLevel = level;
						level++;	
					break;
					
				case   "end"    :
					returning = false;
					//level--;
					if (level == 0){
						
						for(Entry<String, variableParams> entry : DH.entrySet()) {
						    String key = entry.getKey();
						    variableParams value = entry.getValue();
						    try{
							    if(value.getVal() != null){
								    if(value.getLevel() == prevLevel){
								    	//DH.remove(key);
								    	DH.put(key, null);
								    }
								}
						    }
						    catch(NullPointerException e){
						    	// Catching vars that have already been nulled						    	
						    }
						}
					}
					break;
				case   "return"     :  
					/*
					for(Entry<String, variableParams> entry : DH.entrySet()) {
					    String key = entry.getKey();
					    variableParams value = entry.getValue();
					    if(value.getScope()==scope){
					    	DH.put(key, null); //clears variables local to that scope
					    }
					}
					*/
					prevLevel = level;
					returning = true;
					level--;
					TableIn.callReturn(); 
					break;
				case   "call"       :  
					passing = false;
					TableIn.initReturnLine();
					TableIn.findScope(parameter); break;

				//Error Handling
				default         :  System.out.println("Invalid instruction"); break;
			}
			//gets NextLine and steps up Symbol Table Counter
			curCode     = TableIn.getLine();
			instruction = curCode.getInstr();
			parameter   = curCode.getParam();
			scope 		= curCode.getScope();
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
}

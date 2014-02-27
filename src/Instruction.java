
public class Instruction {
	private String 	instr;
	private String  param;
	private String  scope;
	private String [] tokenized;
	int whiteSpace, flag;
	
	Instruction(String line, String scopeIn){
		scope      = scopeIn;
		whiteSpace = 0;
		tokenized  = line.split(" ");
		
		do{
			instr      = tokenized[whiteSpace];
			instr = instr.replaceAll("\t","");
			whiteSpace++; 
		} while (instr.length() == 0);
		
		flag       = whiteSpace + 1; //Flag size to monitor number of tokens in a syntax line + White Space
		param       = null;
		
		if (tokenized.length == flag) param= tokenized[flag-1];
		else if (tokenized.length > flag){
			param= tokenized[flag-1];
			for(int i = flag; i < tokenized.length ; i++ ){
				param = param + " " + tokenized[i];
			}
		}
	}
	public void   setScope(String scopeIn) { scope = scopeIn; } 
	public String getInstr() { return instr; }
	public String getParam() { return param; }
	public String getScope() { return scope; }
	
}

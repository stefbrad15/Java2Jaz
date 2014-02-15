import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class JazAnalyzer {
	private String scope, instr, param;
	private int index;
	private Instruction tempIns;
	
	//object declations
	private JazSymbolTable JST = new JazSymbolTable();
	
	JazAnalyzer(){
		index = 0;
		scope = "main";
	}
	public void Analyze(String path) throws IOException{
		String line;
		
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while((line = br.readLine()) != null){
			
			if (line.length() >= 1){   
				tempIns = new Instruction(line, scope);
				//monitors scope of routines and subroutines
				instr   = tempIns.getInstr();
				param   = tempIns.getParam();
			
				if (instr.equals("label")){				
					scope = param;
					tempIns.setScope(scope);
				}
				JST.addCode(tempIns);
			}
		}
		in.close();
	}
	
	public JazSymbolTable getJST() {return JST;}
} 

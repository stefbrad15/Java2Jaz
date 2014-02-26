
public class variableParams {
	private String scope;
	private int level;
	private String val;
	
	variableParams(String scopeIn, int levelIn, String valIn){
		scope = scopeIn;
		level=levelIn;
		val=valIn;
	}
	public void setScope(String scopeIn){ scope = scopeIn; }
	public void setLevel(int levelIn)   { level = levelIn; }
	public void setVal(String valIn)    { val = valIn;     }
	
	public String getScope() { return scope; }
	public int getLevel()    { return level; }
	public String getVal()   { return val;   }
}

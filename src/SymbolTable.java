
import java.util.HashMap;
import java.util.Iterator;

public class SymbolTable {
	 private HashMap st = new HashMap();

	    public void put(String key, Object value) { st.put(key, value); }
	    public Object get(String key)             { return st.get(key); }
	    public int size()                         { return st.size();   }

	    // print all values in hash table
	    public void show() {
	        Iterator it = st.values().iterator();
	        while(it.hasNext())
	            System.out.println(it.next());
	    }
}

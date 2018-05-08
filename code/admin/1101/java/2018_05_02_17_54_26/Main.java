
import java.util.*;
import java.io
{
	static int N = 202;
	static Node[] nodes =  new Node[N];
	
	public enum COLOR {
		Black,
		White,
		None
	};
	
    public static void main (String[] args) throws IOException {
    	new Main().Run();
    }
    
    public void Run() throws IOException {
    	Scanner scan = new Scanner(new InputStreamReader(System.in));
        
        int V = 0;
        int E = 0;
        int a = 0;
        int b = 0;
        
        while ((V = scan.nextInt()) != 0) {
            E = scan.nextInt();
                        
            for (int i = 0; i < V; i++) {
            	nodes[i] = new Node();
            }
                    
            for (int i = 0; i < E; i++) {
            	a = scan.nextInt();
            	b = scan.nextInt();
            	
            	nodes[a].childs.add(b);
            	nodes[b].childs.add(a);
            }
            
            System.out.println(DFS(0) ? "BICOLORABLE." : "NOT BICOLORABLE.");
        }
    }
    
    public boolean DFS(int root) {
    	
    	Stack<Integer> stack = new Stack<Integer>();
    	stack.push(root);
    	Node current = nodes[root];
    	current.color = COLOR.Black;
    	
    	while(!stack.isEmpty()) {
    		current = nodes[stack.pop()];
    		
    		for (Integer child : current.childs) {
    			if (nodes[child].color == current.color)
    				return false;
    			
    			if (nodes[child].color == COLOR.None) {
    				nodes[child].color = current.color == COLOR.Black ? COLOR.White : COLOR.Black;
    				stack.push(child);
    			}
    		}
    	}
    	
    	return true;
    }
    
    private class Node {
    	COLOR color = COLOR.None;
    	LinkedList<Integer> childs = new LinkedList<Integer>();
    }
}

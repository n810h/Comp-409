package q1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Operations {
	public ArrayList<Op> ops = new ArrayList<>();
	
	public Operations(LinkedList<BNode> _ops) {
		Op op1,op2;
		for(BNode node : _ops) {
			op1 = new Op(node.eTime,node.getId(),"enq");
			op2 = new Op(node.dTime,node.getId(),"deq");
			ops.add(op1);
			ops.add(op2);
		}
		sort();
	}
	
	private void sort() {
		Collections.sort(ops, new Comparator<Op>() {
			@Override
			public int compare(Op o1, Op o2) {
				if(o1.stamp < o2.stamp) return -1;
				else if(o1.stamp > o2.stamp) return 1;
				else {
					if(o1.id < o2.id) return -1;
					else if(o1.id > o2.id) return 1;
					else return 0;
				}
			}
		});
	}
	
	public class Op{
		public int id;
		public long stamp;
		public String type;
		
		public Op(long _stamp, int _id, String _type) {
			stamp = _stamp;
			id = _id;
			type = _type;
		}
	}
}

package assignment1;

/*
 * date: 2017-03-25
 * author: ��ġ��(2011003155)
 * file: algorithm.java
 * comment: DFS �˰���. algorithm Ŭ������ ���.
 */

import java.util.Stack;

public class DFS extends algorithm {
	
	//expand �� ��带 �����ϴ� stack ����ü.
	Stack<Node> fringe;
	
	//DFS Ŭ������ ������. �θ� Ŭ������ map_size �ʱ�ȭ �����ڸ� ȣ�� �� stack �ʱ�ȭ.
	public DFS(int size) {
		super(size);
		
		fringe = new Stack();
		fringe.clear();
	}

	@Override
	/*
	 * ���� ��忡�� successor ��带 ������ stack�� �����ϴ� �޼ҵ�.
	 *  argument:
	 *  	Node node: ���� ���� ���. �� ��忡�� successor ������ ����.
	 */
	protected void expand(Node node) {
		
		//�̹� ��� ���� queen�� ������ ������ expand �Ұ�
		if (node.init_size >= map_size) {
			return;
		}
		
		//�������� ���� ���� ���� ���� 0����� map_size ����� queen�� ���� ���� ��带 ���� �� stack�� ����
		for (int i = 0; i < map_size; i++) {
			Node successor = new Node(node);
			
			successor.chess_map[successor.init_size++] = (short) i;
			
			fringe.push(successor);
		}
	}
	
	/*
	 * main �޼ҵ忡�� ȣ��Ǿ� ���� �˰����� �����ϴ� �޼ҵ�.
	 */
	public String solveProblem() {

		//�ʱ� ����� ����.
		Node initNode = getInitNode();
		
		//�ʱ� ��带 stack�� ����
		fringe.push(initNode);
		
		//stack�� �� ������ �˻�
		while (!fringe.empty()) {
			
			//stack�� �� �� ��带 ������ �� ����
			Node node = fringe.pop();
			
			//���� ��尡 ���� ����� ��� ���� ��� �ƴ� ��� expand
			if (goalTest(node)) {
				return getSolution(node, true);
			} else {
				expand(node);
			}
		}
		
		//��� ��带 �˻��ص� ���� ��尡 ������ ��� ���� ���
		return getSolution(initNode, false);
	}


}

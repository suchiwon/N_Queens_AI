package assignment1;

/*
 * date: 2017-03-25
 * author: ��ġ��(2011003155)
 * file: algorithm.java
 * comment: BFS �˰���. algorithm Ŭ������ ���.
 */

import java.util.LinkedList;
import java.util.Queue;

import assignment1.algorithm.Node;

public class BFS extends algorithm {
	
	//expand �� ��带 �����ϴ� queue ����ü.
	Queue<Node> fringe;

	//BFS Ŭ������ ������. �θ� Ŭ������ map_size �ʱ�ȭ �����ڸ� ȣ�� �� queue �ʱ�ȭ.
	public BFS(int size) {
		super(size);
		
		fringe = new LinkedList();
		
		fringe.clear();
	}
	
	@Override
	/*
	 * ���� ��忡�� successor ��带 ������ queue�� �����ϴ� �޼ҵ�.
	 *  argument:
	 *  	Node node: ���� ���� ���. �� ��忡�� successor ������ ����.
	 */
	protected void expand(Node node) {
		
		//�̹� ��� ���� queen�� ������ ������ expand �Ұ�
		if (node.init_size >= map_size) {
			return;
		}
				
		//�������� ���� ���� ���� ���� 0����� map_size ����� queen�� ���� ���� ��带 ���� �� queue�� ����
		for (int i = 0; i < map_size; i++) {
			Node successor = new Node(node);
			
			successor.chess_map[successor.init_size++] = (short) i;
			
			fringe.offer(successor);
		}
		
	}

	@Override
	/*
	 * main �޼ҵ忡�� ȣ��Ǿ� ���� �˰����� �����ϴ� �޼ҵ�.
	 */
	public String solveProblem() {
		//�ʱ� ����� ����.
		Node initNode = getInitNode();
				
		//�ʱ� ��带 queue�� ����
		fringe.offer(initNode);
		
		//queue�� �� ������ �˻�
		while (!fringe.isEmpty()) {
			
			//queue�� �� �� ��带 ������ �� ����
			Node node = fringe.poll();
			
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

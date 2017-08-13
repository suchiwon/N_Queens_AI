package assignment1;

/*
 * date: 2017-03-25
 * author: ��ġ��(2011003155)
 * file: algorithm.java
 * comment: �� �˰����� ������ ����� �Ǵ� �߻� Ŭ����. �������� ����ϴ� �޼ҵ�� ���⼭ �����ϰ� 
 * 	������ �˰��򿡼� �۵��� �ٸ� �޼ҵ�� ��� Ŭ�������� ����(�߻� �޼ҵ�� ó��)
 */

import java.util.ArrayList;

import assignment1.algorithm.Node;

public abstract class algorithm {
	
	//��� Ŭ������ chess_map���� ���� queen�� �������� ���� ���� �ʱ�ȭ
	public static final int NOT_INIT = -1;
	
	/*
	 * searchTree ��Ŀ��� tree, fringe�� ����� ��� Ŭ����.
	 * N-queens �������� state�� queen�� ü���ǿ� ���� �����̹Ƿ� chess map ������ queen�� ���� ������ ����� ������.
	 */
	public class Node {
		
		/*
		 * Node Ŭ���� ������. chess map�� ũ�⸦ �ʱ�ȭ�ϴ� �����ڿ� expand�� �θ� Node ������ �����ϴ� ������ 2���� ����
		 */
		public Node(int size) {
			this.chess_map = new short[size];
			
		}
		public Node(Node node) {
			this.chess_map = new short[map_size];
			
			for (int i = 0; i < map_size; i++) {
				this.chess_map[i] = node.chess_map[i];
			}
			
			this.init_size = node.init_size;
		}
		
		/*
		 * chess map ���� ���� �迭 ����. N-Queens �������� queen�� ü���ǿ��� ���� ���� ������ �� �����Ƿ�
		 * ������ �ʿ䰡 �ִ� ������ �� ���� queen �ϳ��� ��� �࿡ �����ִ����� �����ϸ� �ǹǷ� �޸��� �Һ� ���̱� ����
		 * ������ �迭�� ����. chess_map[col] = row; �� ü������ col ���� queen�� row �࿡ ������ ��.
		 * ���� ������ ���� ���� NOT_INIT = -1 �� �ʱ�ȭ. queen�� �� ù��° ������ �������� �ȴ�.
		 */	
		public short[] chess_map;
		public int init_size;			//���� ������ queen�� ����. 		
	}
	
	//chess map�� ũ��. main �޼ҵ忡�� �Է� �޾� �ʱ�ȭ
	protected int map_size;
	
	public algorithm(int size) {
		this.map_size = size;
	}
	
	/*
	 * searchTree�� �ʱ� ��� ���� �޼ҵ�. �ƹ��� queen�� �������� ���� ���·� �ʱ�ȭ
	 */
	protected Node getInitNode() {
		//�� ��� ����.
		Node node = new Node(map_size);
		
		//chess map�� �� ���� ������ ��� queen�� �������� ���� ���·� �ʱ�ȭ
		for (int i = 0; i < map_size; i++) {
			node.chess_map[i] = NOT_INIT;
		}
		
		//queen�� ������ ���� 0���� �ʱ�ȭ
		node.init_size = 0;
		
		//���� ��� ��ȯ
		return node;
	};
	
	/*
	 * ���� Node���� �� ���� queen�� �ٸ� ���� queen�� �����ϴ����� �Ǻ��ϴ� �޼ҵ�.
	 *  argument:
	 *  	Node node: �˻� ��� ���.
	 *  	int col: �浹 �˻縦 �� ��
	 */
	protected boolean isNotCollision(Node node, int col) {
		
		//�ڱ� ������ ������ ���鿡 ���� �˻�.
		for (int i = 0; i < col; i++) {
			
			/*
			 * �ٸ� ���� queen�� �ڽŰ� ���� �࿡ �����ϰų� �밢���� ������ ��� �浹 ó��.
			 * �밢���� ����� ��� ���� index ������ ���밪 ��ŭ ���� index ���̰� ���� ��� �浹 ó��.
			 */
			if (node.chess_map[i] == node.chess_map[col] || 
					Math.abs(node.chess_map[i] - node.chess_map[col]) == col - i) {
				return false;
			}
		}
		
		//�ڱ� ������ �������� ���鿡 ���� �˻�.
		for (int i = col + 1; i < map_size; i++) {
			
			/*
			 * �ٸ� ���� queen�� �ڽŰ� ���� �࿡ �����ϰų� �밢���� ������ ��� �浹 ó��.
			 * �밢���� ����� ��� ���� index ������ ���밪 ��ŭ ���� index ���̰� ���� ��� �浹 ó��.
			 */
			if (node.chess_map[i] == node.chess_map[col] || 
					Math.abs(node.chess_map[i] - node.chess_map[col]) == i - col) {
				return false;
			}
		}
		
		//������ �˻翡�� �浹�� ������ ��� �浹 ������ ��ȯ
		return true;
	}
	
	/*
	 * ���� ���°� ���� ��������� �Ǻ��ϴ� �޼ҵ�
	 *  argument:
	 *  	Node node: ���� ���θ� �Ǻ��� ���
	 */
	protected boolean goalTest(Node node) {
		
		//queen�� ���� ������ �ʾ��� ��� �ٷ� ���� ó��
		if (node.init_size < map_size) {
			return false;
		} else {
			
			/*
			 * queen�� ��� ���� ������ ��� �� ������ �ٸ� ���� �浹�� �߻����� �ʴ����� �Ǻ�
			 * �� ���̶� �浹�� ��� ���� ó��. ��� ������ �浹�� �Ͼ�� ���� ��� ���� ó��.
			 */
			for (int i = 0; i < map_size; i++) {
				if (!isNotCollision(node, i)) {
					return false;
				}
			}
			
			return true;
		}
	};
	
	/*
	 * �� ��� �˰��� Ŭ�������� ������ expand �޼ҵ�. ���� ��忡�� successor ��带 ����
	 */
	abstract protected void expand(Node node);
	
	/*
	 * N-queens ������ ���� ���ο� ���� ��� ���ڿ� ���� �� ��ȯ.
	 * ����� ���� ����� chess map ���� ��ȯ.
	 * ����� No Solution ���ڿ� ��ȯ.
	 *  argument:
	 *  	Node node: ������ ��� ���� ���.
	 *  	boolean b_goal: �������� �������� ���θ� üũ�ϴ� �÷���
	 */
	protected String getSolution(Node node, boolean b_goal) {
		
		//������ ��� ���� ����� map chess ���� ���ڿ� ��ȯ
		if (b_goal) {
			String sol = "Location :";
			
			for (int i = 0; i < map_size; i++) {
				sol += " " + node.chess_map[i];
			}
			
			return sol;
			
		//������ ��� No Solution ���ڿ� ��ȯ
		} else {
			String sol = "No Solution";
			return sol;
		}
	}
	
	//main �޼ҵ忡�� ����� �˰��� ���� �޼ҵ�. �� ��� Ŭ�������� ����.
	abstract public String solveProblem();
}

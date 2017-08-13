package assignment1;

/*
 * date: 2017-03-25
 * author: ��ġ��(2011003155)
 * file: algorithm.java
 * comment: DFID �˰���. algorithm Ŭ������ ���.
 */

import java.util.Stack;

import assignment1.algorithm.Node;

public class DFID extends algorithm {
	
	/*
	 * �� ��忡���� �����.
	 *  solv: ���� ���.
	 *  fail: ������ ����� ����.
	 *  cutoff: depth limit ���ѿ� �ɸ�
	 */
	private enum SearchResult {
		solv, fail, cutoff
	}
	
	//��� ���(������ ���)�� ������� ����� ������ ��� Ŭ����
	private class Result {
		
		public SearchResult sr;
		public Node resultNode;
	}

	public DFID(int size) {
		super(size);
	}
	
	//��ӹ��� expand. DFID������ �ٸ� ����� expand�� ��������� �� �Լ��� ����.
	protected void expand(Node node) {
		
	}

	/*
	 * overload�� expand �޼ҵ�. ��� successor�� �� ���� �����ϴ°� �ƴ϶� ���� ���� ��� �࿡ ���� ������ ��带 ����
	 *  argument:
	 *  	Node node: ���� ���� ���. �� ��忡�� successor ��� ����.
	 *  	int row: ���� ���� ������ queen�� �� ��ȣ.
	 */
	protected Node expand(Node node, int row) {
		//�̹� ��� ���� queen�� ������ ���� ��� expand ����
		if (node.init_size >= map_size) {
			return null;
		} else {
			
			//successor ��带 ���� �� ���� ���� queen�� ����.
			Node successor = new Node(node);
			successor.chess_map[successor.init_size++] = (short) row;
			
			return successor;
		}
	}
	
	/*
	 * ��� ȣ��� DFS Ž���ϴ� �޼ҵ�. depth�� limit������ Ž��.
	 *  argument:
	 *  	Node node: ���� Ž������ ���.
	 *  	int limit: depth�� ����. �� depth�� �����ϸ� �� �̻� expand ���� ����.
	 */
	private Result RecursiveDFS(Node node, int limit) {
		
		//�� ��� ���ķ� cutoff�� �Ͼ���� üũ
		boolean bCutOffOccurred = false;
		
		//�� ��尡 ���� ����� ��� ���� ��� ��ȯ
		if (goalTest(node)) {
			Result rs = new Result();
			rs.resultNode = node;
			rs.sr = SearchResult.solv;
			return rs;
			
		//������ �ƴϰ� limit�� �ɷ��� ��� cutoff ��� ��ȯ
		} else if (node.init_size >= limit) {
			Result rs = new Result();
			rs.resultNode = node;
			rs.sr = SearchResult.cutoff;
			return rs;
		
		//���� limit depth ������ ��� expand
		} else {
			
			//���� ���� �ü� �ִ� ���� ��ȣ�� 0 ���� map_size���� ��� ȣ��� Ž��
			for (int i = 0; i < map_size; i++) {
				
				//���� ���� Ư�� �࿡ ������ ������ ��� ����
				Node successor = expand(node, i);
				
				//��� ������ �������� ��� ��� �Լ� ȣ��
				if (successor != null) {
					Result rs = RecursiveDFS(successor, limit);
					
					//�ڽ� ����� ����� cutoff�� cutoff �߻� �÷��� ���.
					if (rs.sr == SearchResult.cutoff) {
						bCutOffOccurred = true;
					//�ڽ� ����� ����� ������ ��� ���� ��� ��ȯ
					} else if (rs.sr != SearchResult.fail) {
						return rs;
					}
				}
			}
			
			//cutoff�� �߻����� �� cutoff ��� ��ȯ.
			if (bCutOffOccurred == true) {
				Result rs = new Result();
				rs.resultNode = node;
				rs.sr = SearchResult.cutoff;
				return rs;
			//cutoff�� �߻����� �ʰ� ������ ������ ��� ���� ó��
			} else {
				Result rs = new Result();
				rs.resultNode = node;
				rs.sr = SearchResult.fail;
				return rs;
			}
		}
	}
	
	/*
	 * �ʱ� ��忡������ ��� DFS ȣ�� ���� �޼ҵ�.
	 *  argument:
	 *  	int limit: ���� �ִ�� Ž���� depth(=��ġ�� queen�� ��)
	 */
	private Result DepthLimitedSearch(int limit) {
		
		//�ʱ� ��带 ���� �� ��� DFS ȣ��
		Node node = getInitNode();
		
		return RecursiveDFS(node, limit);
	}
	
	@Override
	/*
	 * main �޼ҵ忡�� ȣ��Ǿ� ���� �˰����� �����ϴ� �޼ҵ�.
	 */
	public String solveProblem() {

		//�˰����� ����� ������ Ŭ���� ��ü
		Result rs;
		
		/*
		 * limit�� 0���� N + 1���� 1�� �÷����鼭 ����. cutoff ����� ��� limit ����
		 * �� ���� ��� �� ����� ���
		 */
		for (int i = 0; i <= map_size + 1; i++) {
			rs = DepthLimitedSearch(i);
			
			if (rs.sr == SearchResult.solv) {
				return getSolution(rs.resultNode, true);
			} else if (rs.sr == SearchResult.fail) {
				return getSolution(rs.resultNode, false);
			}
		}
		
		//��� Ž���ߴµ��� ����� cutoff�� ���� ó��
		return "ERROR!";
	}

}

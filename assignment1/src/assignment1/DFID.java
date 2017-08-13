package assignment1;

/*
 * date: 2017-03-25
 * author: 서치원(2011003155)
 * file: algorithm.java
 * comment: DFID 알고리즘. algorithm 클래스를 상속.
 */

import java.util.Stack;

import assignment1.algorithm.Node;

public class DFID extends algorithm {
	
	/*
	 * 각 노드에서의 결과값.
	 *  solv: 정답 노드.
	 *  fail: 문제의 결과가 오답.
	 *  cutoff: depth limit 제한에 걸림
	 */
	private enum SearchResult {
		solv, fail, cutoff
	}
	
	//결과 노드(접답일 경우)와 결과값을 멤버로 가지는 결과 클래스
	private class Result {
		
		public SearchResult sr;
		public Node resultNode;
	}

	public DFID(int size) {
		super(size);
	}
	
	//상속받은 expand. DFID에서는 다른 방식의 expand를 사용함으로 빈 함수로 구현.
	protected void expand(Node node) {
		
	}

	/*
	 * overload된 expand 메소드. 모든 successor를 한 번에 생성하는게 아니라 다음 열에 어느 행에 놓는 상태인 노드를 생성
	 *  argument:
	 *  	Node node: 현재 상태 노드. 이 노드에서 successor 노드 생성.
	 *  	int row: 다음 열에 놓여질 queen의 행 번호.
	 */
	protected Node expand(Node node, int row) {
		//이미 모든 열에 queen이 놓여져 있을 경우 expand 실패
		if (node.init_size >= map_size) {
			return null;
		} else {
			
			//successor 노드를 생성 후 다음 열에 queen을 놓음.
			Node successor = new Node(node);
			successor.chess_map[successor.init_size++] = (short) row;
			
			return successor;
		}
	}
	
	/*
	 * 재귀 호출로 DFS 탐색하는 메소드. depth를 limit까지만 탐색.
	 *  argument:
	 *  	Node node: 현재 탐색중인 노드.
	 *  	int limit: depth의 제한. 이 depth에 도달하면 더 이상 expand 하지 않음.
	 */
	private Result RecursiveDFS(Node node, int limit) {
		
		//이 노드 이후로 cutoff가 일어났는지 체크
		boolean bCutOffOccurred = false;
		
		//이 노드가 정답 노드일 경우 정답 결과 반환
		if (goalTest(node)) {
			Result rs = new Result();
			rs.resultNode = node;
			rs.sr = SearchResult.solv;
			return rs;
			
		//정답이 아니고 limit에 걸렸을 경우 cutoff 결과 반환
		} else if (node.init_size >= limit) {
			Result rs = new Result();
			rs.resultNode = node;
			rs.sr = SearchResult.cutoff;
			return rs;
		
		//아직 limit depth 이전일 경우 expand
		} else {
			
			//다음 열에 올수 있는 햄의 번호를 0 부터 map_size까지 재귀 호출로 탐색
			for (int i = 0; i < map_size; i++) {
				
				//다음 열의 특정 행에 놓여진 상태의 노드 생성
				Node successor = expand(node, i);
				
				//노드 생성에 성공했을 경우 재귀 함수 호출
				if (successor != null) {
					Result rs = RecursiveDFS(successor, limit);
					
					//자식 노드의 결과가 cutoff면 cutoff 발생 플래그 계산.
					if (rs.sr == SearchResult.cutoff) {
						bCutOffOccurred = true;
					//자식 노드의 결과가 정답일 경우 정답 노드 반환
					} else if (rs.sr != SearchResult.fail) {
						return rs;
					}
				}
			}
			
			//cutoff가 발생했을 시 cutoff 결과 반환.
			if (bCutOffOccurred == true) {
				Result rs = new Result();
				rs.resultNode = node;
				rs.sr = SearchResult.cutoff;
				return rs;
			//cutoff가 발생하지 않고 정답이 없었을 경우 오답 처리
			} else {
				Result rs = new Result();
				rs.resultNode = node;
				rs.sr = SearchResult.fail;
				return rs;
			}
		}
	}
	
	/*
	 * 초기 노드에서부터 재귀 DFS 호출 시작 메소드.
	 *  argument:
	 *  	int limit: 현재 최대로 탐색할 depth(=배치할 queen의 수)
	 */
	private Result DepthLimitedSearch(int limit) {
		
		//초기 노드를 생성 후 재귀 DFS 호출
		Node node = getInitNode();
		
		return RecursiveDFS(node, limit);
	}
	
	@Override
	/*
	 * main 메소드에서 호출되어 실제 알고리즘을 실행하는 메소드.
	 */
	public String solveProblem() {

		//알고리즘의 결과를 저장할 클래스 객체
		Result rs;
		
		/*
		 * limit를 0부터 N + 1까지 1씩 늘려가면서 진행. cutoff 결과일 경우 limit 증가
		 * 그 외의 경우 그 결과를 출력
		 */
		for (int i = 0; i <= map_size + 1; i++) {
			rs = DepthLimitedSearch(i);
			
			if (rs.sr == SearchResult.solv) {
				return getSolution(rs.resultNode, true);
			} else if (rs.sr == SearchResult.fail) {
				return getSolution(rs.resultNode, false);
			}
		}
		
		//모두 탐색했는데도 결과가 cutoff면 에러 처리
		return "ERROR!";
	}

}

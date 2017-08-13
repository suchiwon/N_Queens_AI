package assignment1;

/*
 * date: 2017-03-25
 * author: 서치원(2011003155)
 * file: algorithm.java
 * comment: 각 알고리즘의 구현에 기반이 되는 추상 클래스. 공통으로 사용하는 메소드는 여기서 구현하고 
 * 	각각의 알고리즘에서 작동이 다른 메소드는 상속 클래스에서 구현(추상 메소드로 처리)
 */

import java.util.ArrayList;

import assignment1.algorithm.Node;

public abstract class algorithm {
	
	//노드 클래스의 chess_map에서 아직 queen이 놓여지지 않은 열의 초기화
	public static final int NOT_INIT = -1;
	
	/*
	 * searchTree 방식에서 tree, fringe의 요소인 노드 클래스.
	 * N-queens 문제에서 state는 queen이 체스판에 놓인 상태이므로 chess map 정보와 queen이 놓인 갯수를 멤버로 가진다.
	 */
	public class Node {
		
		/*
		 * Node 클래스 생성자. chess map의 크기를 초기화하는 생성자와 expand시 부모 Node 정보를 복사하는 생성자 2개가 존재
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
		 * chess map 정보 저장 배열 변수. N-Queens 문제에서 queen은 체스판에서 같은 열에 존재할 수 없으므로
		 * 저장할 필요가 있는 정보는 각 열에 queen 하나가 어느 행에 놓여있는지를 저장하면 되므로 메모리의 소비를 줄이기 위해
		 * 일차원 배열로 선언. chess_map[col] = row; 은 체스판의 col 열에 queen이 row 행에 놓였단 뜻.
		 * 아직 놓이지 않은 열은 NOT_INIT = -1 로 초기화. queen은 맨 첫번째 열부터 놓여지게 된다.
		 */	
		public short[] chess_map;
		public int init_size;			//현재 놓여진 queen의 갯수. 		
	}
	
	//chess map의 크기. main 메소드에서 입력 받아 초기화
	protected int map_size;
	
	public algorithm(int size) {
		this.map_size = size;
	}
	
	/*
	 * searchTree의 초기 노드 생성 메소드. 아무런 queen도 놓여있지 않은 상태로 초기화
	 */
	protected Node getInitNode() {
		//새 노드 생성.
		Node node = new Node(map_size);
		
		//chess map의 각 열의 정보를 모두 queen이 놓여있지 않은 상태로 초기화
		for (int i = 0; i < map_size; i++) {
			node.chess_map[i] = NOT_INIT;
		}
		
		//queen이 놓여진 수를 0개로 초기화
		node.init_size = 0;
		
		//생성 노드 반환
		return node;
	};
	
	/*
	 * 현재 Node에서 각 열의 queen이 다른 열의 queen를 공격하는지를 판별하는 메소드.
	 *  argument:
	 *  	Node node: 검사 대상 노드.
	 *  	int col: 충돌 검사를 할 열
	 */
	protected boolean isNotCollision(Node node, int col) {
		
		//자기 열보다 왼쪽의 열들에 대해 검사.
		for (int i = 0; i < col; i++) {
			
			/*
			 * 다른 열의 queen이 자신과 같은 행에 존재하거나 대각선상에 존재할 경우 충돌 처리.
			 * 대각선의 계산의 경우 열의 index 차이의 절대값 만큼 행의 index 차이가 있을 경우 충돌 처리.
			 */
			if (node.chess_map[i] == node.chess_map[col] || 
					Math.abs(node.chess_map[i] - node.chess_map[col]) == col - i) {
				return false;
			}
		}
		
		//자기 열보다 오른쪽의 열들에 대해 검사.
		for (int i = col + 1; i < map_size; i++) {
			
			/*
			 * 다른 열의 queen이 자신과 같은 행에 존재하거나 대각선상에 존재할 경우 충돌 처리.
			 * 대각선의 계산의 경우 열의 index 차이의 절대값 만큼 행의 index 차이가 있을 경우 충돌 처리.
			 */
			if (node.chess_map[i] == node.chess_map[col] || 
					Math.abs(node.chess_map[i] - node.chess_map[col]) == i - col) {
				return false;
			}
		}
		
		//양쪽의 검사에서 충돌이 없었을 경우 충돌 없음을 반환
		return true;
	}
	
	/*
	 * 현재 상태가 정답 노드인지를 판별하는 메소드
	 *  argument:
	 *  	Node node: 정답 여부를 판별할 노드
	 */
	protected boolean goalTest(Node node) {
		
		//queen이 전부 놓이지 않았을 경우 바로 오답 처리
		if (node.init_size < map_size) {
			return false;
		} else {
			
			/*
			 * queen이 모든 열에 놓였을 경우 각 열에서 다른 열과 충돌이 발생하지 않는지를 판별
			 * 한 열이라도 충돌할 경우 오답 처리. 모든 열에서 충돌이 일어나지 않을 경우 정답 처리.
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
	 * 각 상속 알고리즘 클래스에서 구현할 expand 메소드. 현재 노드에서 successor 노드를 생성
	 */
	abstract protected void expand(Node node);
	
	/*
	 * N-queens 문제의 정답 여부에 따라 결과 문자열 생성 후 반환.
	 * 정답시 정답 노드의 chess map 정보 반환.
	 * 오답시 No Solution 문자열 반환.
	 *  argument:
	 *  	Node node: 정답일 경우 정답 노드.
	 *  	boolean b_goal: 정답인지 오답인지 여부를 체크하는 플래그
	 */
	protected String getSolution(Node node, boolean b_goal) {
		
		//정답일 경우 정답 노드의 map chess 정보 문자열 반환
		if (b_goal) {
			String sol = "Location :";
			
			for (int i = 0; i < map_size; i++) {
				sol += " " + node.chess_map[i];
			}
			
			return sol;
			
		//오답일 경우 No Solution 문자열 반환
		} else {
			String sol = "No Solution";
			return sol;
		}
	}
	
	//main 메소드에서 사용할 알고리즘 전개 메소드. 각 상속 클래스에서 구현.
	abstract public String solveProblem();
}

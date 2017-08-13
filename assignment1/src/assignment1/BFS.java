package assignment1;

/*
 * date: 2017-03-25
 * author: 서치원(2011003155)
 * file: algorithm.java
 * comment: BFS 알고리즘. algorithm 클래스를 상속.
 */

import java.util.LinkedList;
import java.util.Queue;

import assignment1.algorithm.Node;

public class BFS extends algorithm {
	
	//expand 된 노드를 저장하는 queue 구조체.
	Queue<Node> fringe;

	//BFS 클래스의 생성자. 부모 클래스의 map_size 초기화 생성자를 호출 후 queue 초기화.
	public BFS(int size) {
		super(size);
		
		fringe = new LinkedList();
		
		fringe.clear();
	}
	
	@Override
	/*
	 * 현재 노드에서 successor 노드를 생성해 queue에 저장하는 메소드.
	 *  argument:
	 *  	Node node: 현재 상태 노드. 이 노드에서 successor 노드들을 생성.
	 */
	protected void expand(Node node) {
		
		//이미 모든 열에 queen이 놓여져 있으면 expand 불가
		if (node.init_size >= map_size) {
			return;
		}
				
		//마지막에 놓은 열의 다음 열에 0행부터 map_size 행까지 queen을 놓은 상태 노드를 생성 후 queue에 저장
		for (int i = 0; i < map_size; i++) {
			Node successor = new Node(node);
			
			successor.chess_map[successor.init_size++] = (short) i;
			
			fringe.offer(successor);
		}
		
	}

	@Override
	/*
	 * main 메소드에서 호출되어 실제 알고리즘을 실행하는 메소드.
	 */
	public String solveProblem() {
		//초기 노드의 생성.
		Node initNode = getInitNode();
				
		//초기 노드를 queue에 저장
		fringe.offer(initNode);
		
		//queue가 빌 때까지 검사
		while (!fringe.isEmpty()) {
			
			//queue의 맨 앞 노드를 가져온 후 제거
			Node node = fringe.poll();
			
			//현재 노드가 정답 노드일 경우 정답 출력 아닐 경우 expand
			if (goalTest(node)) {
				return getSolution(node, true);
			} else {
				expand(node);
			}
		}
		
		//모든 노드를 검사해도 정답 노드가 없었을 경우 오답 출력
		return getSolution(initNode, false);
	}
}

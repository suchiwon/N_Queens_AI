package assignment3;

import java.util.Iterator;
import java.util.TreeMap;
/*
 * date: 2017-04-28
 * author: 서치원(2011003155)
 * file: CSP.java
 * comment: forward checking을 하지 않는 CSP 문제 해결 알고리즘. algorithm 클래스를 상속.
 * 			일반적인 DFS 탐색과 비슷하게 작동.
 */
public class CSP extends algorithm {

	public CSP(int queen_num) {
		super(queen_num);
	}
	
	/*
	 * 현재 assignment을 통해 constraint가 유지되는지를 검사하는 함수.
	 * argument:
	 * 	col: assign된 열의 위치.
	 * 	row: assign된 열에 놓여질 행의 위치.
	 */
	protected boolean isConstraintSatisfaction(int col, int row) {
		
		Iterator<Integer> keySetIter = this.assignment.keySet().iterator();
		
		//현재 assign되어 있는 열들을 대상으로 그 열과 충돌이 일어나는지를 검사. 한 열이라도 충돌이 일어나면 false를 반환.
		while (keySetIter.hasNext()) {
			int colVal = keySetIter.next();
			int rowVal = this.assignment.get(colVal);
			if (rowVal == row 
					|| rowVal == row - Math.abs(col - colVal) 
					|| rowVal == row + Math.abs(col - colVal)) {
				return false;
			}
		}
		
		//모든 assign되어 있는 열과 충돌이 없으면 true를 반환.
		return true;
	}
	
	/*
	 * backtrack 방식으로 정답을 탐색. 맨 왼쪽 열부터 assign하면서 이 행에 놓을 때 constraint가 지켜지는지를 검사. 
	 * 	지켜지면 다음 열로 넘어가고 안 지켜지면 backtracking 하여 다음 행에 놓고 진행.
	 */
	protected String backtrackingSearch() {
		
		//모든 열이 assign 되었으면 정답이므로 반환
		if (isAssignmentComplete() == true) {
			return this.getSolution();
		} else {
			
			//현재 assign할 열을 선택. forward checking을 안하므로 모든 열의 남은 domain수가 같으므로 가장 왼쪽 열을 선택하게 됨.
 			int variableIdx = this.selectUnAssignedVariable();
			
 			//맨 위 행부터 assign해보면서 진행.
			for (int i = 0; i < this.queen_num; ++i) {
				
				//이 행에 놓아도 constraint가 지켜질 경우 놓고 진행.
				if (isConstraintSatisfaction(variableIdx, i) == true) {
					
					//assignment에 추가 후 남은 variable에서 제거
					this.assignment.put(variableIdx, i);
					this.remainVariableSet.remove(variableIdx);
					
					//DFS로 다음 열로 넘어감.
					String result = backtrackingSearch();
					
					//결과가 정답이었을 경우 그대로 반환.
					if (result.equals(NO_SOLUTION) == false) {
						return result;
					}
					
					//다른 행으로 assign 하기 위해 이전 상태로 되돌림.
					this.assignment.remove(variableIdx);
					this.remainVariableSet.add(variableIdx);
				}
			}
			
			//전체 행을 탐색해도 답을 찾지 못했을 경우 이 assign에 맞는 정답이 없음을 반환
			return NO_SOLUTION;
		}
	}
	


	@Override
	public String solveProblem() {
		return this.backtrackingSearch();
	}

}

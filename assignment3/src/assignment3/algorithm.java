package assignment3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/*
 * date: 2017-04-28
 * author: 서치원(2011003155)
 * file: algorithm.java
 * comment: 각 알고리즘의 구현에 기반이 되는 추상 클래스. 공통으로 사용하는 메소드는 여기서 구현하고 
 * 	각각의 알고리즘에서 작동이 다른 메소드는 상속 클래스에서 구현(추상 메소드로 처리)
 */
public abstract class algorithm {
	
	/*
	 * assignment: 현재 값이 정해진 variable의 정해진 domain 내의 map list. key가 variable(=체스판의 열), value가 체스판의 열의 어느 행에 놓여있는지를 나타냄.
	 * remainVariableSet: 아직 Queen이 놓여지지 않은 열의 집합
	 */
	protected Map<Integer, Integer> assignment;	
	protected Set<Integer> remainVariableSet;
	
	/*
	 * domain: 체스판을 토대로 각 열에 가능한 행의 값을 domain으로 취급해 2차 배열로 구현. domain의 추가, 삭제를 빠르게 하기 위해 고정 배열 자료 구조를 사용.
	 * 		domain[col][row]는 col열에 domain row value라는 뜻이고 true일 경우 domain 내부에 존재, false일 경우 domain에서 삭제된 상태를 나타냄.
	 * domainLeaveSize: 각 열의 domain 내부의 value의 남은 갯수. consistent 여부의 빠른 계산을 위해 사용.
	 */
	protected boolean[][] domain;
	protected int[] domainLeaveSize;
	
	protected int queen_num;								//체스판의 한 변의 크기. queen의 수.
	
	public static final String NO_SOLUTION = "No Solution";	//정답을 못찾았을 경우에 반환할 문자열.
	static final public int NOT_INIT = -1;					//초기화 전 변수의 값 지정.
	
	/*
	 * 생성자 함수. queen 갯수를 받아 domain 배열 초기화.
	 */
	public algorithm(int queen_num) {
		this.queen_num = queen_num;
		
		assignment = new HashMap<Integer, Integer>();
		domain = new boolean[queen_num][queen_num];
		remainVariableSet = new HashSet<Integer>();
		domainLeaveSize = new int[queen_num];
		
		for (int i = 0; i < queen_num; ++i) {
			Arrays.fill(domain[i], true);
		}
		
		Arrays.fill(domainLeaveSize, queen_num);
		
		for (int i = 0; i < queen_num; ++i) {
			remainVariableSet.add(i);
		}
		
	}
	
	/*
	 * 모든 열에 queen이 consistent를 해치지 않고 놓여졌는지 확인.
	 * consistent 여부는 assignment를 정할 때 계산되고 assignment에는 같은 열의 값이 들어오지 않으므로 assignment의 갯수만 queen의 수와 같은지를 세면 된다. 
	 */
	protected boolean isAssignmentComplete() {
		
		if (assignment.size() == this.queen_num) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 다음 assign을 할 variable(열의 위치)를 정하기 위한 메소드. 가장 적은 domain 크기을 가진 variable을 선택. 동률일 경우 왼쪽 열을 선택. 
	 */
	protected int getMinimumRemainingValues() {
		
		//현재 최소 domain 크기와 열이 인덱스 저장 변수
		int minRemainSize = this.queen_num + 1;
		int valIdx = NOT_INIT;
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//아직 assign 되지 않은 variable을 탐색하면서 가장 작은 domain 크기를 가지는 열의 위치 정함.
		while (iter.hasNext()) {
			int col = iter.next();
			
			if (minRemainSize > domainLeaveSize[col]) {
				minRemainSize = domainLeaveSize[col];
				valIdx = col;
			}
		}
		
		return valIdx;
	}
	
	/*
	 * getLeastConstrainingValue 함수를 위한 각 domain의 선택이 영향을 미치는 다른 열의 domain의 크기를 계산하는 함수.
	 * argument:
	 * 	col: 검사할 열(assign할 variable)
	 *  row: 검사할 행(이 domain 값을 선택할 때 영향을 받는 domain의 갯수 계산)
	 */
	protected int getEffectDomainSize(int col, int row) {
		
		//영향을 받는 domain의 갯수 저장 변수.
		int effectSize = 0;
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//아직 assign 되지 않은 variable을 탐색하며 영향을 받는 domain 갯수 계산.
		while (iter.hasNext()) {
			int iterCol = iter.next();
			
			//자신의 열은 검사하지 않음. consistent 검사와 같은 방식으로 함.
			if (iterCol != col) {
				if (domain[iterCol][row] == true) {
					++effectSize;
				}
				
				int temp = row - Math.abs(col - iterCol);
				
				if (temp >= 0 && domain[iterCol][temp] == true) {
					++effectSize;
				}
				
				temp = row + Math.abs(col - iterCol);
				
				if (temp < queen_num && domain[iterCol][temp] == true) {
					++effectSize;
				}
			}
		}
		
		return effectSize;
	}
	
	/*
	 * variable에서 어떤 domain값으로 assign 할지를 정하는 함수. 가장 다른 variable들의 domain에 영향을 적게 주는 domain값을 선택.
	 *  같을 경우 적은 수(=위의 행)을 선택.
	 *  argument:
	 *   variableIdx: assign할 열의 인덱스.
	 */
	protected int getLeastConstrainingValue(int variableIdx) {
		
		//현재 가장 작은 영향을 주는 domain의 갯수와 그것에 맞는 domain값을 저장하는 변수
		int minConstraintSize = Integer.MAX_VALUE;
		int value = NOT_INIT;
		
		
		//domain을 전부 탐색하면서 그 중 assign이 가능한 domain을 대상으로만 계산.
		for (int i = 0; i < queen_num; ++i) {
			if (domain[variableIdx][i] == true) {
				int temp = getEffectDomainSize(variableIdx, i);
				
				if (minConstraintSize > temp) {
					minConstraintSize = temp;
					value = i;
				}
			}
		}
		
		return value;
	}
	
	/*
	 * 함수의 기능 설명을 확실하게 하기 위한 함수. assign할 domain의 값을 정하는 함수.
	 */
	protected int selectUnAssignedVariable() {
		return getMinimumRemainingValues();
	}
	
	/*
	 * 정답을 발견했을 경우 정답 문자열을 반환하는 함수.
	 */
	protected String getSolution() {
		String solution = "Location : ";
		
		for (int i = 0; i < queen_num; ++i) {
			solution += assignment.get(i) + " ";
		}
		
		return solution;
	}
	
	/*
	 * backtracking을 통해 N-queens 문제를 푸는 함수. 각 상속 클래스에 맞게 구현.
	 */
	abstract protected String backtrackingSearch();
	
	/*
	 * backtrackingSearch 함수를 통해 정답을 계산하는 함수. 실제 main 클래스에서 사용.
	 */
	abstract public String solveProblem();
	
	
}

package assignment3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
/*
 * date: 2017-04-28
 * author: 서치원(2011003155)
 * file: fowardChecking.java
 * comment: forward checking을 assign 할때마다 하는 CSP 문제 해결 알고리즘. algorithm 클래스를 상속.
 */
public class fowardChecking extends algorithm {
	
	//현재 열에서 남은 domain을 assign할 순서로 저장해놓는 리스트 변수. domain이 다른 domain에 적게 영향을 끼치는 순서대로 정렬한다.
	protected List< Pair<Integer, Integer> > domainOrder;
	
	//domainOrder의 정렬 comparator 변수. 
	protected Comparator< Pair<Integer, Integer> > comparator;
	
	//2개의 class 변수를 가지는 pair 클래스 구현.
	public class Pair<L,R> {
	    private L l;
	    private R r;
	    public Pair(L l, R r){
	        this.l = l;
	        this.r = r;
	    }
	    public L getL(){ return l; }
	    public R getR(){ return r; }
	    public void setL(L l){ this.l = l; }
	    public void setR(R r){ this.r = r; }
	}
	
	/*
	 * 생성자 함수. 정렬 함수를 지정.
	 */
	public fowardChecking(int queen_num) {
		super(queen_num);
		
		this.comparator = new Comparator< Pair<Integer, Integer> >() {

			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				// TODO Auto-generated method stub
				return o1.getL() < o2.getL() ? -1 : (o1.getL() > o2.getL() ? 1 : 0);
			}
		};
	}

	/*
	 * assign할 열에서 남은 domain들의 assign할 순서를 정하는 함수. domain값을 선택 시 영향을 끼치는 다른 domain값이 작은 것을 먼저 선택하게 한다.
	 * argument:
	 * 	variableIdx: assign할 열의 위치
	 */
	protected List< Pair<Integer, Integer> > getDomainOrder(int variableIdx) {
		List< Pair<Integer, Integer> > domainOrder = new ArrayList< Pair<Integer, Integer> >();
		
		//현재 선택이 가능한 domain값(행)에서 영향을 끼치는 domain의 갯수를 계산해 리스트에 저장.
		for (int i = 0; i < this.queen_num; ++i) {
			if (this.domain[variableIdx][i] == true) {
				int effectSize = getEffectDomainSize(variableIdx, i);
				
				domainOrder.add(new Pair(effectSize, i));
			}
		}
		
		return domainOrder;
	}
	
	/*
	 * 현재 assignment을 통해 constraint가 유지되는지를 검사하는 함수. 하나의 열이 가능한 domain을 하나도 가지고 있지 않을 경우 false 반환.
	 */
	protected boolean isConstraintSatisfaction() {
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		while (iter.hasNext()) {
			int col = iter.next();
			
			if (this.domainLeaveSize[col] <= 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * assign된 domain값을 통해 더 이상 지정할 수 없게 된 다른 열의 domain을 지우는 함수. 
	 * 계산의 감소를 위해 아직 assign되지 않은 열에만 계산.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List< Pair<Integer, Integer> > forwardCheck(int variableIdx, int row) {
		
		//backtracking을 할 때 지웠던 domain의 복원을 위해 지운 domain들을 저장하는 리스트 변수. <col, row> 형식으로 저장.
		List< Pair<Integer, Integer> > checkedDomainList = new ArrayList< Pair<Integer, Integer> >();
		
		
		//자신의 다른 행의 값을 삭제
		for (int i = 0; i < this.queen_num; ++i) {
			if (domain[variableIdx][i] == true) {
				domain[variableIdx][i] = false;
				checkedDomainList.add(new Pair(variableIdx, i));
			}
		}
		
		//assign된 열은 domain이 없으므로 0으로 처리.
		domainLeaveSize[variableIdx] = 0;
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//아직 assign되지 않은 열들에 forward checking. 충돌이 일어나는 domain을 전부 지우고 복원을 위해 저장해놓음.
		while (iter.hasNext()) {
			int iterCol = iter.next();
			
			//같은 행에 있는 domain 삭제. 삭제한 만큼 남은 domain 갯수에서 빼줌.
			if (domain[iterCol][row] == true) {
				domain[iterCol][row] = false;
				--domainLeaveSize[iterCol];
				checkedDomainList.add(new Pair(iterCol, row));
			}
				
			//대각선상에 있는 domain 삭제
			int temp = row - Math.abs(variableIdx - iterCol);
				
			if (temp >= 0 && domain[iterCol][temp] == true) {
				domain[iterCol][temp] = false;
				--domainLeaveSize[iterCol];
				checkedDomainList.add(new Pair(iterCol, temp));
			}
				
			temp = row + Math.abs(variableIdx - iterCol);
				
			if (temp < queen_num && domain[iterCol][temp] == true) {
				domain[iterCol][temp] = false;
				--domainLeaveSize[iterCol];
				checkedDomainList.add(new Pair(iterCol, temp));
			}
		}
		
		return checkedDomainList;
	}
	
	/*
	 * backtracking시 삭제했던 domain들을 복원하는 함수. 저장해놨던 domain 리스트의 탐색을 통해 복구.
	 */
	protected void rollbackForwardCheck(int variableIdx, int row, List< Pair<Integer, Integer> > checkedDomainList) {
		
		Iterator< Pair<Integer, Integer> > iter = checkedDomainList.iterator();
		
		//저장해놓은 리스트를 탐색하면서 다시 domain을 복구.
		while (iter.hasNext()) {
			Pair<Integer, Integer> point = iter.next();
			
			if (domain[point.getL()][point.getR()] == false) {
				domain[point.getL()][point.getR()] = true;
				++domainLeaveSize[point.getL()];
			}
		}
	}
	
	/*
	 * backtrack 방식으로 정답을 탐색.
	 */
	@Override
	protected String backtrackingSearch() {
		
		//모든 열이 assign 되었으면 정답 반환.
		if (isAssignmentComplete()) {
			return this.getSolution();
			
		//현재 constraint가 깨치지 않았을 경우 계속 진행
		} else if (isConstraintSatisfaction()){
			
			//assign할 열을 선택
 			int variableIdx = this.selectUnAssignedVariable();
 			
 			//assign할 domain 순서 정함. domain에 영향을 적게 끼치는 순서로 정렬.
 			this.domainOrder = this.getDomainOrder(variableIdx);
 			Collections.sort(this.domainOrder, this.comparator);
 			
 			Iterator< Pair<Integer, Integer> > iter = this.domainOrder.iterator();
			
 			//domain 정렬 순서대로 assign 시도
			while (iter.hasNext()) {
				
				//domainOrder의 행의 값 가져옴.
				Pair<Integer, Integer> point = iter.next();
				int row = point.getR();

				//assign 리스트에 추가 후 assign 대기 리스트에서는 제거
				this.assignment.put(variableIdx, row);
				this.remainVariableSet.remove(variableIdx);
				
				//forward checking 진행. 삭제한 domain 리스트를 복원을 위해 저장
				List< Pair<Integer, Integer> > checkedDomainList = this.forwardCheck(variableIdx, row);
					
				//다음 assign 진행
				String result = backtrackingSearch();
					
				//정답을 반환 받았으면 정답을 그래도 반환
				if (result.equals(NO_SOLUTION) == false) {
					return result;
				}
				
				//정답을 찾지 못했을 경우 다음 domain으로 놓기 위해 복원.
				this.rollbackForwardCheck(variableIdx, row, checkedDomainList);
					
				this.assignment.remove(variableIdx);
				this.remainVariableSet.add(variableIdx);
			}
		}
		
		return NO_SOLUTION;
	}

	@Override
	public String solveProblem() {
		return this.backtrackingSearch();
	}


}

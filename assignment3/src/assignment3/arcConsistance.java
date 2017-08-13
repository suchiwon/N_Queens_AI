package assignment3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import assignment3.fowardChecking.Pair;
/*
 * date: 2017-04-28
 * author: 서치원(2011003155)
 * file: arcConsistance.java
 * comment: forward checking,arcConsistance를 하며 진행하는 CSP 문제 해결 알고리즘. algorithm 클래스를 상속.
 */
public class arcConsistance extends fowardChecking {

	public arcConsistance(int queen_num) {
		super(queen_num);
	}
	
	/*
	 * 탐색 열과 대상 열의 사이의 domain 상태에서 consistent 를 위해 사용이 불가능한 domain을 제거하는 함수.
	 * consistent 계산은 destIdx에서 어떤 domain을 선택해도 srcIdx의 domain을 사용할 수 없게 되는 경우 그 domain을 삭제
	 * argument:
	 * 	destIdx: 자신의 domain을 상황을 통해 상대방 domain을 삭제할 열의 위치
	 * 	srcIdx: 더 이상 사용이 불가능한 domain을 삭제할 열의 위치
	 */
	private List< Pair<Integer, Integer> > rmInconsistentValue(int destIdx, int srcIdx) {
		
		//이번 검사로 지워지는 domain을 복원을 위해 저장해놓을 리스트 변수
		List< Pair<Integer, Integer> > checkedDomainList = new ArrayList< Pair<Integer, Integer> >();
		
		//srcIdx의 domain에서 destIdx의 domain 중 몇 개의 domain에 영향을 받는지를 카운트하는 배열 변수.
		int[] inconsistentCount = new int[this.queen_num];
		
		Arrays.fill(inconsistentCount, 0);
		
		//destIdx의 domain을 탐색. 가능한 domain을 선택시 srcIdx의 어느 domain에 영향을 끼치는지 카운트.
		for (int i = 0; i < this.queen_num; ++i) {
			if (domain[destIdx][i] == true) {
				if (domain[srcIdx][i] == true) {
					++inconsistentCount[i];
				}
				
				int temp = i - Math.abs(destIdx - srcIdx);
				
				if (temp >= 0 && domain[srcIdx][temp] == true) {
					++inconsistentCount[temp];
				}
				
				temp = i + Math.abs(destIdx - srcIdx);
				
				if (temp < this.queen_num && domain[srcIdx][temp] == true) {
					++inconsistentCount[temp];
				}
			}
		}
		
		//srcIdx의 domain을 탐색하며 이 domain이 현재 destIdx의 모든 domain의 선택에 영향을 받을 경우 더 이상 사용이 불가능 한 것이므로 삭제
		for (int i = 0; i < this.queen_num; ++i) {
			if (inconsistentCount[i] == domainLeaveSize[destIdx] && domain[srcIdx][i] == true) {
				domain[srcIdx][i] = false;
				--domainLeaveSize[srcIdx];
				checkedDomainList.add(new Pair(srcIdx, i));
			}
		}
		
		return checkedDomainList;
	}
	
	/*
	 * assign로 인해 domain이 변경된 것으로 불가능해진 domain을 제거하는 함수. arc consistant 방식 사용.
	 * argument:
	 * 	variableIdx: assign한 열의 번호
	 *  row: assign된 행의 번호
	 */
	private List< Pair<Integer, Integer> > AC_3(int variableIdx, int row) {
		
		//domain이 삭제되어 다른 variable에 검사를 하게 될 열의 번호 순서 큐.
		Queue<Integer> arcDestQueue = new LinkedList<Integer>();
		
		//이번 검사로 삭제한 domain들을 backtracking시 복구하기 위해 저장해놓는 리스트 변수
		List< Pair<Integer, Integer> > checkedDomainList = new ArrayList< Pair<Integer, Integer> >();
		
		//자신 열에 대해 assign된 domain을 제외한 나머지 domain을 삭제
		for (int i = 0; i < this.queen_num; ++i) {
			if (i != row && domain[variableIdx][i] == true) {
				domain[variableIdx][i] = false;
				checkedDomainList.add(new Pair(variableIdx, i));
			}
		}
		domainLeaveSize[variableIdx] = 1;
		
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//assign되어 domain이 변경된 현재 열부터 검사 시작.
		arcDestQueue.add(variableIdx);
		
		while (!arcDestQueue.isEmpty()) {
			
			int destIdx = arcDestQueue.poll();
			
			//검사 중 한 열의 domain이 남지 않게 되었다면 바로 종료.
			if (destIdx != variableIdx && this.domainLeaveSize[destIdx] <= 0) {
				return checkedDomainList;
			}
			
			//검사 대상은 현재 assign되지 않은 열들에 한해서만 해도 되므로 assign되지 않은 열의 집합 셋을 사용해 탐색
			iter = this.remainVariableSet.iterator();
			
			//N queens 문제에서 모든 열은 모든 열에게 영향을 끼치므로 전부 검사.
			while (iter.hasNext()) {
				int srcIdx = iter.next();
				
				//자기 자신은 검사 대상으로 삼지 않음
				if (destIdx != srcIdx) {
					
					//검사해 추가로 삭제된 domain을 복원을 위해 저장.
					List< Pair<Integer, Integer> > appendCheckedDomainList = rmInconsistentValue(destIdx, srcIdx);
					
					//추가로 삭제된 domain이 있다면 큐에 넣어 재검사.
					if (!appendCheckedDomainList.isEmpty()) {
						arcDestQueue.add(srcIdx);
						checkedDomainList.addAll(appendCheckedDomainList);
						
						if (this.domainLeaveSize[srcIdx] <= 0) {
							return checkedDomainList;
						}
					}
				}
			}
		}
		
		return checkedDomainList;
	}
	
	/*
	 * backtrack 방식으로 정답을 탐색.
	 */
	protected String backtrackingSearch() {
		
		//모든 열이 assign 되었으면 정답 반환.
		if (isAssignmentComplete()) {
			return this.getSolution();
			
		//현재 constraint가 깨치지 않았을 경우 계속 진행
		} else if (isConstraintSatisfaction()){
			
			//assign 할 열을 선택
 			int variableIdx = this.selectUnAssignedVariable();
 			
 			//assign 할 행의 순서를 선택해 리스트로 저장
 			this.domainOrder = this.getDomainOrder(variableIdx);	
 			Collections.sort(this.domainOrder, this.comparator);
 			
 			Iterator< Pair<Integer, Integer> > iter = this.domainOrder.iterator();
			
 			//정한 행의 순서대로 assign하고 arc consistance를 실행해 domain 제거
			while (iter.hasNext()) {
				
				Pair<Integer, Integer> point = iter.next();
				int row = point.getR();

				//assign 리스트에 추가, assign 대기 리스트에서 제거
				this.assignment.put(variableIdx, row);
				this.remainVariableSet.remove(variableIdx);
				
				//AC 알고리즘으로 domain 제거 후 복구를 위해 저장
				List< Pair<Integer, Integer> > checkedDomainList = this.AC_3(variableIdx, row);
					
				//다음 assign을 실행
				String result = backtrackingSearch();
					
				//정답 결과를 받았을 경우 정답을 반환
				if (result.equals(NO_SOLUTION) == false) {
					return result;
				}
				
				//정답이 아닐 경우 다른 domain을 놓기 위해 제거했던 domain을 되돌리고 assign에서 제거
				this.rollbackForwardCheck(variableIdx, row, checkedDomainList);			
				this.assignment.remove(variableIdx);
				this.remainVariableSet.add(variableIdx);
			}
		}
		
		return NO_SOLUTION;
	}

}

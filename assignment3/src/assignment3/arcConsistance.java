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
 * author: ��ġ��(2011003155)
 * file: arcConsistance.java
 * comment: forward checking,arcConsistance�� �ϸ� �����ϴ� CSP ���� �ذ� �˰���. algorithm Ŭ������ ���.
 */
public class arcConsistance extends fowardChecking {

	public arcConsistance(int queen_num) {
		super(queen_num);
	}
	
	/*
	 * Ž�� ���� ��� ���� ������ domain ���¿��� consistent �� ���� ����� �Ұ����� domain�� �����ϴ� �Լ�.
	 * consistent ����� destIdx���� � domain�� �����ص� srcIdx�� domain�� ����� �� ���� �Ǵ� ��� �� domain�� ����
	 * argument:
	 * 	destIdx: �ڽ��� domain�� ��Ȳ�� ���� ���� domain�� ������ ���� ��ġ
	 * 	srcIdx: �� �̻� ����� �Ұ����� domain�� ������ ���� ��ġ
	 */
	private List< Pair<Integer, Integer> > rmInconsistentValue(int destIdx, int srcIdx) {
		
		//�̹� �˻�� �������� domain�� ������ ���� �����س��� ����Ʈ ����
		List< Pair<Integer, Integer> > checkedDomainList = new ArrayList< Pair<Integer, Integer> >();
		
		//srcIdx�� domain���� destIdx�� domain �� �� ���� domain�� ������ �޴����� ī��Ʈ�ϴ� �迭 ����.
		int[] inconsistentCount = new int[this.queen_num];
		
		Arrays.fill(inconsistentCount, 0);
		
		//destIdx�� domain�� Ž��. ������ domain�� ���ý� srcIdx�� ��� domain�� ������ ��ġ���� ī��Ʈ.
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
		
		//srcIdx�� domain�� Ž���ϸ� �� domain�� ���� destIdx�� ��� domain�� ���ÿ� ������ ���� ��� �� �̻� ����� �Ұ��� �� ���̹Ƿ� ����
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
	 * assign�� ���� domain�� ����� ������ �Ұ������� domain�� �����ϴ� �Լ�. arc consistant ��� ���.
	 * argument:
	 * 	variableIdx: assign�� ���� ��ȣ
	 *  row: assign�� ���� ��ȣ
	 */
	private List< Pair<Integer, Integer> > AC_3(int variableIdx, int row) {
		
		//domain�� �����Ǿ� �ٸ� variable�� �˻縦 �ϰ� �� ���� ��ȣ ���� ť.
		Queue<Integer> arcDestQueue = new LinkedList<Integer>();
		
		//�̹� �˻�� ������ domain���� backtracking�� �����ϱ� ���� �����س��� ����Ʈ ����
		List< Pair<Integer, Integer> > checkedDomainList = new ArrayList< Pair<Integer, Integer> >();
		
		//�ڽ� ���� ���� assign�� domain�� ������ ������ domain�� ����
		for (int i = 0; i < this.queen_num; ++i) {
			if (i != row && domain[variableIdx][i] == true) {
				domain[variableIdx][i] = false;
				checkedDomainList.add(new Pair(variableIdx, i));
			}
		}
		domainLeaveSize[variableIdx] = 1;
		
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//assign�Ǿ� domain�� ����� ���� ������ �˻� ����.
		arcDestQueue.add(variableIdx);
		
		while (!arcDestQueue.isEmpty()) {
			
			int destIdx = arcDestQueue.poll();
			
			//�˻� �� �� ���� domain�� ���� �ʰ� �Ǿ��ٸ� �ٷ� ����.
			if (destIdx != variableIdx && this.domainLeaveSize[destIdx] <= 0) {
				return checkedDomainList;
			}
			
			//�˻� ����� ���� assign���� ���� ���鿡 ���ؼ��� �ص� �ǹǷ� assign���� ���� ���� ���� ���� ����� Ž��
			iter = this.remainVariableSet.iterator();
			
			//N queens �������� ��� ���� ��� ������ ������ ��ġ�Ƿ� ���� �˻�.
			while (iter.hasNext()) {
				int srcIdx = iter.next();
				
				//�ڱ� �ڽ��� �˻� ������� ���� ����
				if (destIdx != srcIdx) {
					
					//�˻��� �߰��� ������ domain�� ������ ���� ����.
					List< Pair<Integer, Integer> > appendCheckedDomainList = rmInconsistentValue(destIdx, srcIdx);
					
					//�߰��� ������ domain�� �ִٸ� ť�� �־� ��˻�.
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
	 * backtrack ������� ������ Ž��.
	 */
	protected String backtrackingSearch() {
		
		//��� ���� assign �Ǿ����� ���� ��ȯ.
		if (isAssignmentComplete()) {
			return this.getSolution();
			
		//���� constraint�� ��ġ�� �ʾ��� ��� ��� ����
		} else if (isConstraintSatisfaction()){
			
			//assign �� ���� ����
 			int variableIdx = this.selectUnAssignedVariable();
 			
 			//assign �� ���� ������ ������ ����Ʈ�� ����
 			this.domainOrder = this.getDomainOrder(variableIdx);	
 			Collections.sort(this.domainOrder, this.comparator);
 			
 			Iterator< Pair<Integer, Integer> > iter = this.domainOrder.iterator();
			
 			//���� ���� ������� assign�ϰ� arc consistance�� ������ domain ����
			while (iter.hasNext()) {
				
				Pair<Integer, Integer> point = iter.next();
				int row = point.getR();

				//assign ����Ʈ�� �߰�, assign ��� ����Ʈ���� ����
				this.assignment.put(variableIdx, row);
				this.remainVariableSet.remove(variableIdx);
				
				//AC �˰������� domain ���� �� ������ ���� ����
				List< Pair<Integer, Integer> > checkedDomainList = this.AC_3(variableIdx, row);
					
				//���� assign�� ����
				String result = backtrackingSearch();
					
				//���� ����� �޾��� ��� ������ ��ȯ
				if (result.equals(NO_SOLUTION) == false) {
					return result;
				}
				
				//������ �ƴ� ��� �ٸ� domain�� ���� ���� �����ߴ� domain�� �ǵ����� assign���� ����
				this.rollbackForwardCheck(variableIdx, row, checkedDomainList);			
				this.assignment.remove(variableIdx);
				this.remainVariableSet.add(variableIdx);
			}
		}
		
		return NO_SOLUTION;
	}

}

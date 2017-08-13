package assignment3;

import java.util.Iterator;
import java.util.TreeMap;
/*
 * date: 2017-04-28
 * author: ��ġ��(2011003155)
 * file: CSP.java
 * comment: forward checking�� ���� �ʴ� CSP ���� �ذ� �˰���. algorithm Ŭ������ ���.
 * 			�Ϲ����� DFS Ž���� ����ϰ� �۵�.
 */
public class CSP extends algorithm {

	public CSP(int queen_num) {
		super(queen_num);
	}
	
	/*
	 * ���� assignment�� ���� constraint�� �����Ǵ����� �˻��ϴ� �Լ�.
	 * argument:
	 * 	col: assign�� ���� ��ġ.
	 * 	row: assign�� ���� ������ ���� ��ġ.
	 */
	protected boolean isConstraintSatisfaction(int col, int row) {
		
		Iterator<Integer> keySetIter = this.assignment.keySet().iterator();
		
		//���� assign�Ǿ� �ִ� ������ ������� �� ���� �浹�� �Ͼ������ �˻�. �� ���̶� �浹�� �Ͼ�� false�� ��ȯ.
		while (keySetIter.hasNext()) {
			int colVal = keySetIter.next();
			int rowVal = this.assignment.get(colVal);
			if (rowVal == row 
					|| rowVal == row - Math.abs(col - colVal) 
					|| rowVal == row + Math.abs(col - colVal)) {
				return false;
			}
		}
		
		//��� assign�Ǿ� �ִ� ���� �浹�� ������ true�� ��ȯ.
		return true;
	}
	
	/*
	 * backtrack ������� ������ Ž��. �� ���� ������ assign�ϸ鼭 �� �࿡ ���� �� constraint�� ������������ �˻�. 
	 * 	�������� ���� ���� �Ѿ�� �� �������� backtracking �Ͽ� ���� �࿡ ���� ����.
	 */
	protected String backtrackingSearch() {
		
		//��� ���� assign �Ǿ����� �����̹Ƿ� ��ȯ
		if (isAssignmentComplete() == true) {
			return this.getSolution();
		} else {
			
			//���� assign�� ���� ����. forward checking�� ���ϹǷ� ��� ���� ���� domain���� �����Ƿ� ���� ���� ���� �����ϰ� ��.
 			int variableIdx = this.selectUnAssignedVariable();
			
 			//�� �� ����� assign�غ��鼭 ����.
			for (int i = 0; i < this.queen_num; ++i) {
				
				//�� �࿡ ���Ƶ� constraint�� ������ ��� ���� ����.
				if (isConstraintSatisfaction(variableIdx, i) == true) {
					
					//assignment�� �߰� �� ���� variable���� ����
					this.assignment.put(variableIdx, i);
					this.remainVariableSet.remove(variableIdx);
					
					//DFS�� ���� ���� �Ѿ.
					String result = backtrackingSearch();
					
					//����� �����̾��� ��� �״�� ��ȯ.
					if (result.equals(NO_SOLUTION) == false) {
						return result;
					}
					
					//�ٸ� ������ assign �ϱ� ���� ���� ���·� �ǵ���.
					this.assignment.remove(variableIdx);
					this.remainVariableSet.add(variableIdx);
				}
			}
			
			//��ü ���� Ž���ص� ���� ã�� ������ ��� �� assign�� �´� ������ ������ ��ȯ
			return NO_SOLUTION;
		}
	}
	


	@Override
	public String solveProblem() {
		return this.backtrackingSearch();
	}

}

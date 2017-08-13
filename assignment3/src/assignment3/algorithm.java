package assignment3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/*
 * date: 2017-04-28
 * author: ��ġ��(2011003155)
 * file: algorithm.java
 * comment: �� �˰����� ������ ����� �Ǵ� �߻� Ŭ����. �������� ����ϴ� �޼ҵ�� ���⼭ �����ϰ� 
 * 	������ �˰��򿡼� �۵��� �ٸ� �޼ҵ�� ��� Ŭ�������� ����(�߻� �޼ҵ�� ó��)
 */
public abstract class algorithm {
	
	/*
	 * assignment: ���� ���� ������ variable�� ������ domain ���� map list. key�� variable(=ü������ ��), value�� ü������ ���� ��� �࿡ �����ִ����� ��Ÿ��.
	 * remainVariableSet: ���� Queen�� �������� ���� ���� ����
	 */
	protected Map<Integer, Integer> assignment;	
	protected Set<Integer> remainVariableSet;
	
	/*
	 * domain: ü������ ���� �� ���� ������ ���� ���� domain���� ����� 2�� �迭�� ����. domain�� �߰�, ������ ������ �ϱ� ���� ���� �迭 �ڷ� ������ ���.
	 * 		domain[col][row]�� col���� domain row value��� ���̰� true�� ��� domain ���ο� ����, false�� ��� domain���� ������ ���¸� ��Ÿ��.
	 * domainLeaveSize: �� ���� domain ������ value�� ���� ����. consistent ������ ���� ����� ���� ���.
	 */
	protected boolean[][] domain;
	protected int[] domainLeaveSize;
	
	protected int queen_num;								//ü������ �� ���� ũ��. queen�� ��.
	
	public static final String NO_SOLUTION = "No Solution";	//������ ��ã���� ��쿡 ��ȯ�� ���ڿ�.
	static final public int NOT_INIT = -1;					//�ʱ�ȭ �� ������ �� ����.
	
	/*
	 * ������ �Լ�. queen ������ �޾� domain �迭 �ʱ�ȭ.
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
	 * ��� ���� queen�� consistent�� ��ġ�� �ʰ� ���������� Ȯ��.
	 * consistent ���δ� assignment�� ���� �� ���ǰ� assignment���� ���� ���� ���� ������ �����Ƿ� assignment�� ������ queen�� ���� �������� ���� �ȴ�. 
	 */
	protected boolean isAssignmentComplete() {
		
		if (assignment.size() == this.queen_num) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * ���� assign�� �� variable(���� ��ġ)�� ���ϱ� ���� �޼ҵ�. ���� ���� domain ũ���� ���� variable�� ����. ������ ��� ���� ���� ����. 
	 */
	protected int getMinimumRemainingValues() {
		
		//���� �ּ� domain ũ��� ���� �ε��� ���� ����
		int minRemainSize = this.queen_num + 1;
		int valIdx = NOT_INIT;
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//���� assign ���� ���� variable�� Ž���ϸ鼭 ���� ���� domain ũ�⸦ ������ ���� ��ġ ����.
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
	 * getLeastConstrainingValue �Լ��� ���� �� domain�� ������ ������ ��ġ�� �ٸ� ���� domain�� ũ�⸦ ����ϴ� �Լ�.
	 * argument:
	 * 	col: �˻��� ��(assign�� variable)
	 *  row: �˻��� ��(�� domain ���� ������ �� ������ �޴� domain�� ���� ���)
	 */
	protected int getEffectDomainSize(int col, int row) {
		
		//������ �޴� domain�� ���� ���� ����.
		int effectSize = 0;
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//���� assign ���� ���� variable�� Ž���ϸ� ������ �޴� domain ���� ���.
		while (iter.hasNext()) {
			int iterCol = iter.next();
			
			//�ڽ��� ���� �˻����� ����. consistent �˻�� ���� ������� ��.
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
	 * variable���� � domain������ assign ������ ���ϴ� �Լ�. ���� �ٸ� variable���� domain�� ������ ���� �ִ� domain���� ����.
	 *  ���� ��� ���� ��(=���� ��)�� ����.
	 *  argument:
	 *   variableIdx: assign�� ���� �ε���.
	 */
	protected int getLeastConstrainingValue(int variableIdx) {
		
		//���� ���� ���� ������ �ִ� domain�� ������ �װͿ� �´� domain���� �����ϴ� ����
		int minConstraintSize = Integer.MAX_VALUE;
		int value = NOT_INIT;
		
		
		//domain�� ���� Ž���ϸ鼭 �� �� assign�� ������ domain�� ������θ� ���.
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
	 * �Լ��� ��� ������ Ȯ���ϰ� �ϱ� ���� �Լ�. assign�� domain�� ���� ���ϴ� �Լ�.
	 */
	protected int selectUnAssignedVariable() {
		return getMinimumRemainingValues();
	}
	
	/*
	 * ������ �߰����� ��� ���� ���ڿ��� ��ȯ�ϴ� �Լ�.
	 */
	protected String getSolution() {
		String solution = "Location : ";
		
		for (int i = 0; i < queen_num; ++i) {
			solution += assignment.get(i) + " ";
		}
		
		return solution;
	}
	
	/*
	 * backtracking�� ���� N-queens ������ Ǫ�� �Լ�. �� ��� Ŭ������ �°� ����.
	 */
	abstract protected String backtrackingSearch();
	
	/*
	 * backtrackingSearch �Լ��� ���� ������ ����ϴ� �Լ�. ���� main Ŭ�������� ���.
	 */
	abstract public String solveProblem();
	
	
}

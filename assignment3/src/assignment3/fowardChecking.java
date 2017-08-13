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
 * author: ��ġ��(2011003155)
 * file: fowardChecking.java
 * comment: forward checking�� assign �Ҷ����� �ϴ� CSP ���� �ذ� �˰���. algorithm Ŭ������ ���.
 */
public class fowardChecking extends algorithm {
	
	//���� ������ ���� domain�� assign�� ������ �����س��� ����Ʈ ����. domain�� �ٸ� domain�� ���� ������ ��ġ�� ������� �����Ѵ�.
	protected List< Pair<Integer, Integer> > domainOrder;
	
	//domainOrder�� ���� comparator ����. 
	protected Comparator< Pair<Integer, Integer> > comparator;
	
	//2���� class ������ ������ pair Ŭ���� ����.
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
	 * ������ �Լ�. ���� �Լ��� ����.
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
	 * assign�� ������ ���� domain���� assign�� ������ ���ϴ� �Լ�. domain���� ���� �� ������ ��ġ�� �ٸ� domain���� ���� ���� ���� �����ϰ� �Ѵ�.
	 * argument:
	 * 	variableIdx: assign�� ���� ��ġ
	 */
	protected List< Pair<Integer, Integer> > getDomainOrder(int variableIdx) {
		List< Pair<Integer, Integer> > domainOrder = new ArrayList< Pair<Integer, Integer> >();
		
		//���� ������ ������ domain��(��)���� ������ ��ġ�� domain�� ������ ����� ����Ʈ�� ����.
		for (int i = 0; i < this.queen_num; ++i) {
			if (this.domain[variableIdx][i] == true) {
				int effectSize = getEffectDomainSize(variableIdx, i);
				
				domainOrder.add(new Pair(effectSize, i));
			}
		}
		
		return domainOrder;
	}
	
	/*
	 * ���� assignment�� ���� constraint�� �����Ǵ����� �˻��ϴ� �Լ�. �ϳ��� ���� ������ domain�� �ϳ��� ������ ���� ���� ��� false ��ȯ.
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
	 * assign�� domain���� ���� �� �̻� ������ �� ���� �� �ٸ� ���� domain�� ����� �Լ�. 
	 * ����� ���Ҹ� ���� ���� assign���� ���� ������ ���.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List< Pair<Integer, Integer> > forwardCheck(int variableIdx, int row) {
		
		//backtracking�� �� �� ������ domain�� ������ ���� ���� domain���� �����ϴ� ����Ʈ ����. <col, row> �������� ����.
		List< Pair<Integer, Integer> > checkedDomainList = new ArrayList< Pair<Integer, Integer> >();
		
		
		//�ڽ��� �ٸ� ���� ���� ����
		for (int i = 0; i < this.queen_num; ++i) {
			if (domain[variableIdx][i] == true) {
				domain[variableIdx][i] = false;
				checkedDomainList.add(new Pair(variableIdx, i));
			}
		}
		
		//assign�� ���� domain�� �����Ƿ� 0���� ó��.
		domainLeaveSize[variableIdx] = 0;
		
		Iterator<Integer> iter = this.remainVariableSet.iterator();
		
		//���� assign���� ���� ���鿡 forward checking. �浹�� �Ͼ�� domain�� ���� ����� ������ ���� �����س���.
		while (iter.hasNext()) {
			int iterCol = iter.next();
			
			//���� �࿡ �ִ� domain ����. ������ ��ŭ ���� domain �������� ����.
			if (domain[iterCol][row] == true) {
				domain[iterCol][row] = false;
				--domainLeaveSize[iterCol];
				checkedDomainList.add(new Pair(iterCol, row));
			}
				
			//�밢���� �ִ� domain ����
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
	 * backtracking�� �����ߴ� domain���� �����ϴ� �Լ�. �����س��� domain ����Ʈ�� Ž���� ���� ����.
	 */
	protected void rollbackForwardCheck(int variableIdx, int row, List< Pair<Integer, Integer> > checkedDomainList) {
		
		Iterator< Pair<Integer, Integer> > iter = checkedDomainList.iterator();
		
		//�����س��� ����Ʈ�� Ž���ϸ鼭 �ٽ� domain�� ����.
		while (iter.hasNext()) {
			Pair<Integer, Integer> point = iter.next();
			
			if (domain[point.getL()][point.getR()] == false) {
				domain[point.getL()][point.getR()] = true;
				++domainLeaveSize[point.getL()];
			}
		}
	}
	
	/*
	 * backtrack ������� ������ Ž��.
	 */
	@Override
	protected String backtrackingSearch() {
		
		//��� ���� assign �Ǿ����� ���� ��ȯ.
		if (isAssignmentComplete()) {
			return this.getSolution();
			
		//���� constraint�� ��ġ�� �ʾ��� ��� ��� ����
		} else if (isConstraintSatisfaction()){
			
			//assign�� ���� ����
 			int variableIdx = this.selectUnAssignedVariable();
 			
 			//assign�� domain ���� ����. domain�� ������ ���� ��ġ�� ������ ����.
 			this.domainOrder = this.getDomainOrder(variableIdx);
 			Collections.sort(this.domainOrder, this.comparator);
 			
 			Iterator< Pair<Integer, Integer> > iter = this.domainOrder.iterator();
			
 			//domain ���� ������� assign �õ�
			while (iter.hasNext()) {
				
				//domainOrder�� ���� �� ������.
				Pair<Integer, Integer> point = iter.next();
				int row = point.getR();

				//assign ����Ʈ�� �߰� �� assign ��� ����Ʈ������ ����
				this.assignment.put(variableIdx, row);
				this.remainVariableSet.remove(variableIdx);
				
				//forward checking ����. ������ domain ����Ʈ�� ������ ���� ����
				List< Pair<Integer, Integer> > checkedDomainList = this.forwardCheck(variableIdx, row);
					
				//���� assign ����
				String result = backtrackingSearch();
					
				//������ ��ȯ �޾����� ������ �׷��� ��ȯ
				if (result.equals(NO_SOLUTION) == false) {
					return result;
				}
				
				//������ ã�� ������ ��� ���� domain���� ���� ���� ����.
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

package assignment2;

import java.util.ArrayList;

/*
 * date: 2017-04-09
 * author: ��ġ��(2011003155)
 * file: hillClimbing.java
 * comment: hillClimbing �˰����� ����� ������ Ŭ����. ���� Ŭ������ State�� ������.
 * ������ ���� ó�� ����� ���� ������.
 */

public class hillClimbing {
	
	private int map_size;						//ü������ �� ���� ����. ��ġ�� Queen�� ��.
	
	private static final int MOVE_SIZE = 2;		//extend���� �� ���� Queen�� �̵��ϴ� ����� ��. ��, �Ʒ� 2���� ��츦 ������.
	private static final int UP = 0;			//���� �̵��� �ǹ�
	private static final int DOWN = 1;			//�Ʒ��� �̵��� �ǹ�
	
	//������ �޼ҵ�. ü������ ũ�⸦ �ʱ�ȭ.
	public hillClimbing(int size) {
		this.map_size = size;
	}
	
	/*
	 * �ϳ��� State ������ ������ Ŭ����. State�� ���� ������� State�� ó���� �޼ҵ�鸦 ������.
	 */
	public class State {
		
		/*
		 * chess map ���� ���� �迭 ����. N-Queens �������� queen�� ü���ǿ��� ���� ���� ������ �� �����Ƿ�
		 * ������ �ʿ䰡 �ִ� ������ �� ���� queen �ϳ��� ��� �࿡ �����ִ����� �����ϸ� �ǹǷ� �޸��� �Һ� ���̱� ����
		 * ������ �迭�� ����. chess_map[col] = row; �� ü������ col ���� queen�� row �࿡ ������ ��.
		 */	
		public int chess_map[];
		
		public int map_size;					//ü������ ũ��. hillClimbing ������ ����.
		public int h_value;						//�� state�� �޸���ƽ ��. �޸���ƽ ���� ����� ��� �޼ҵ忡�� ����.
		
		/*
		 * ������ �޼ҵ�. ü������ ũ�� �ʱ�ȭ �����ڿ� ���� ������ 2���� ����.
		 */
		public State(int size) {
			
			this.chess_map = new int[size];
			this.map_size = size;
		}
		
		public State(State state) {
			this.chess_map = new int[state.map_size];
			
			//�ʱ�ȭ�� ü���ǿ� ���� ��� state�� ü���� ���� ����
			for (int i = 0; i < state.map_size; i++) {
				this.chess_map[i] = state.chess_map[i];
			}
			
			this.map_size = state.map_size;
		}
		
		/*
		 * �˻� ���� ������ ������ Queen�� �浹�ϴ� Ƚ�� ��� �޼ҵ�. ������ ����ϰ� �˻��� �浹 �˻��� �ߺ��� ���´�.
		 * argument:
		 * 		int col: �˻��� ���� index
		 * 
		 * output:
		 * 		�浹 Ƚ��
		 */
		private int GetCollisionRightCol(int col) {
			int col_size = 0;
			
			//�˻� ���� �ٷ� ������ ����� ������ ����� �˻�
			for (int i = col + 1; i < this.map_size; i++) {
				
				//���� ���� �ְų� �밢�� �� ������ �浹 ó��
				if (this.chess_map[i] == this.chess_map[col] || 
						Math.abs(this.chess_map[i] - this.chess_map[col]) == i - col) {
					col_size++;
				}
			}
			
			return col_size;
		}
		
		/*
		 * �޸���ƽ ���� ��� �޼ҵ�. �޸���ƽ ���� Queen ���� �� �浹 Ƚ���� ����.
		 * ��� ����� �� �࿡�� �ڽ��� ������ ����� Queen���� �浹 Ƚ���� ���� ����.
		 * argument:
		 * 
		 * output:
		 * 		�޸���ƽ �� h(=Queen�� �浹 Ƚ��)
		 */
		public int CalcHeuristic() {
			int h_val = 0;						//�޸���ƽ �� ���� ����
			int temp;							//�� �࿡���� �浹 Ƚ���� ������ �ӽ� ����
			
			
			//ù�� ° ����� ������ �� ������ �ڽ��� ������ ����� �浹 Ƚ�� ����� ����.
			for (int i = 0; i < this.map_size - 1; i++) {
				temp = this.GetCollisionRightCol(i);
				h_val += temp;
			}
			
			//�޸���ƽ �� ���� ������ ����
			this.h_value = h_val;
			
			return this.h_value;
		}
		
		/*
		 * ���� state���� ���� ������ successor state�� ����.
		 * �ϳ��� �࿡�� ���� �Ʒ��� ��ĭ �̵��� state�� successor state�� �����Ѵ�.
		 * argument:
		 * 		int col: �̵��� ��.
		 * 		int move: �̵��� ������ �����ϴ� ����. 0 = UP, 1 = DOWN
		 * output:
		 * 		successor state
		 */
		public State MakeSuccessor(int col, int move) {
			
			//�� state�� �ڽ��� ������ ������ ����
			State state = new State(this);
			
			//���� �����̴� ������ ���
			if (move == hillClimbing.UP) {
				
				//�̹� �� ��ĭ�� ��� �̵��� �Ұ����ϹǷ� null state ��ȯ.
				if (state.chess_map[col] > 0) {
					state.chess_map[col]--;
				} else {
					return null;
				}
			//�Ʒ��� �����̴� ������ ���
			} else if (move == hillClimbing.DOWN) {
				//�̹� �� �Ʒ�ĭ�� ��� �̵��� �Ұ����ϹǷ� null state ��ȯ.
				if (state.chess_map[col] < state.map_size - 1) {
					state.chess_map[col]++;
				} else {
					return null;
				}
			}
			
			return state;
		}
		
		/*
		 * local optima�� �ɸ� ��� �������� �ٸ� �������� �̵��ϴ� �޼ҵ�.
		 * �������� �� ���� ������ 2ĭ �̻��� ���̸� �̵�.(1ĭ �̵��� �̹� successor���� ����Ͽ����Ƿ�)
		 * argument:
		 * 
		 * output:
		 * 
		 */
		public void RandomJump() {
			
			//������ ������ ������ �õ�
			while (true) {
				
				//�̵��� ��� �� �࿡�� �̵��� ���� ü���� ���� ������ �������� ����
				int col = MakeRandomInteger(0, this.map_size - 1);
				int row = MakeRandomInteger(0, this.map_size - 1);
				
				//2ĭ �̻��� �̵��� �ƴ� ��� �ٽ� �õ�
				if (Math.abs(this.chess_map[col] - row) > 1) {
					this.chess_map[col] = row;
					return;
				}
			}
		}
		
		/*
		 * ���� state�� goal state�� ��� �ڽ��� ü���� ������ ���ڿ��� ��ȯ. 
		 * �� ���� ��� ���� Queen�� ������ �ִ����� ��ĭ�� ��� ���·� ��ȯ.
		 * argument:
		 * 
		 * output:
		 * 		���� state�� ü���� ����.
		 */
		public String GetChessMap() {
			String string = new String();
			
			for (int i = 0; i < this.map_size; i++) {
				
				//������ ���� ��� ���⸦ ���� ����
				if (i != this.map_size - 1) {
					string += this.chess_map[i] + " ";
				} else {
					string += this.chess_map[i];
				}
			}
			
			return string;
		}
	}
	
	/*
	 * Ư�� ���������� ���� ������ ����. ���� ������ �ϰų� �̵��� successor state�� �� �ϳ��� ���� ���
	 * argument:
	 * 		int min: �ּ� ���� ��
	 * 		int max: �ִ� ���� ��
	 * output:
	 * 		�������� ������ ���� ���� ����
	 */
	private int MakeRandomInteger(int min, int max) {
		return (int) ((Math.random() * (max - min + 1)) + min);
	}
	
	/*
	 * ó�� state�� ����. �� �࿡�� �������� �� ���� Queen�� ��ġ.
	 * argument:
	 * 
	 * output:
	 * 		�������� ������ init state
	 */
	public State MakeInitState() {
		
		//state�� �ʱ�ȭ ����
		State initState = new State(this.map_size);
		
		//�� �ึ�� �������� �� ���� ���� ��ġ
		for (int i = 0; i < initState.map_size; i++) {
			initState.chess_map[i] = MakeRandomInteger(0, this.map_size - 1);
		}
		
		return initState;
	}
	
	/*
	 * N-Queens ���� �ذ� �޼ҵ�. �������� ������ state�������� �޸���ƽ ���� ���� �������� �̵��� goal state�� ����.
	 * argument:
	 *
	 * output:
	 * 		goal state�� ü���� ����.
	 */
	public String SolveProblem() {
		
		//init state ����
		State nowState = MakeInitState();
		
		int h_value;								//���� state�� �޸���ƽ �� ���� ����
		int h_successor;							//������ successor state�� �� ���� ���� �޸���ƽ �� ���� ����
		int h_temp;									//������ successor state�� �޸���ƽ ���� �ӽ÷� �����ϴ� ����
		
		/*
		 * successorList�� ������ successor �� ���� state���� ���� �޸���ƽ ���� ������ state�� �����ϴ� ����Ʈ
		 * smallestList�� successorList�� ��� �� ���� ���� �޸���ƽ ���� ������ state�� �����ϴ� ����Ʈ
		 */
		ArrayList<State> successorList = new ArrayList<State>();
		ArrayList<State> smallestList = new ArrayList<State>();
		
		//goal state�� �����Ҷ� ���� ���
		while (true) {
			
			//���� state�� �޸���ƽ �� ��� �� �ּ� �޸���ƽ ���� ����
			h_value = nowState.CalcHeuristic();
			h_successor = h_value;
			
			//�� state ���� ����Ʈ�� �ʱ�ȭ
			successorList.clear();
			smallestList.clear();
			
			//���� state�� goal state�� �ƴ� ���
			if (h_value > 0) {
				
				//�� �࿡�� ��, �Ʒ��� �̵��ϴ� ��� ��츶�� successor state�� ����
				for (int i = 0; i < nowState.map_size; i++) {
					for (int j = 0; j < MOVE_SIZE; j++) {
						State successorState = nowState.MakeSuccessor(i, j);
						
						//successor state�� �����Ǿ�����
						if (successorState != null) {												
							
							//successor state�� �޸���ƽ �� ���
							h_temp = successorState.CalcHeuristic();
							
							//successor state�� �޸���ƽ ���� ���� �ּ� �޸���ƽ ������ ������ ����Ʈ�� �߰�
							if (h_temp < h_successor) {
								h_successor = h_temp;
								successorList.add(successorState);
							}
						}
					}
				}
				
				//successor ����Ʈ���� ���� ���� �޸���ƽ ���� ������ state�� ����
				for (int i = 0; i < successorList.size(); i++) {
					if (successorList.get(i).h_value == h_successor) {
						smallestList.add(successorList.get(i));
					}
				}
				
				//���� state���� �� ���� �޸���ƽ ���� ������ state�� �̵����� ���� ��� ���� ����
				if (successorList.isEmpty()) {
					nowState.RandomJump();
				} else {
					
					int random_idx;
					
					//���� ���� �޸���ƽ ���� ������ state�� �� �ϳ��� ������ �̵�
					random_idx = MakeRandomInteger(0, smallestList.size() - 1);
						
					nowState = new State(smallestList.get(random_idx));
				}
			} else {
				//goal state�� ���޽� ���� ���
				return nowState.GetChessMap();
			}
		}
	}
}

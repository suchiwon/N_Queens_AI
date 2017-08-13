package assignment2;

import java.util.ArrayList;

/*
 * date: 2017-04-09
 * author: 서치원(2011003155)
 * file: hillClimbing.java
 * comment: hillClimbing 알고리즘의 기능을 구현한 클래스. 내부 클래스로 State를 가진다.
 * 과제의 문제 처리 기능을 전부 가진다.
 */

public class hillClimbing {
	
	private int map_size;						//체스판의 한 변의 길이. 배치할 Queen의 수.
	
	private static final int MOVE_SIZE = 2;		//extend에서 한 열의 Queen이 이동하는 경우의 수. 위, 아래 2개의 경우를 가진다.
	private static final int UP = 0;			//위로 이동을 의미
	private static final int DOWN = 1;			//아래로 이동을 의미
	
	//생성자 메소드. 체스판의 크기를 초기화.
	public hillClimbing(int size) {
		this.map_size = size;
	}
	
	/*
	 * 하나의 State 정보를 저장할 클래스. State의 정보 변수들과 State를 처리할 메소드들를 가진다.
	 */
	public class State {
		
		/*
		 * chess map 정보 저장 배열 변수. N-Queens 문제에서 queen은 체스판에서 같은 열에 존재할 수 없으므로
		 * 저장할 필요가 있는 정보는 각 열에 queen 하나가 어느 행에 놓여있는지를 저장하면 되므로 메모리의 소비를 줄이기 위해
		 * 일차원 배열로 선언. chess_map[col] = row; 은 체스판의 col 열에 queen이 row 행에 놓였단 뜻.
		 */	
		public int chess_map[];
		
		public int map_size;					//체스판의 크기. hillClimbing 변수와 같음.
		public int h_value;						//이 state의 휴리스틱 값. 휴리스틱 값의 계산은 계산 메소드에서 설명.
		
		/*
		 * 생성자 메소드. 체스판의 크기 초기화 생성자와 복사 생성자 2개가 존재.
		 */
		public State(int size) {
			
			this.chess_map = new int[size];
			this.map_size = size;
		}
		
		public State(State state) {
			this.chess_map = new int[state.map_size];
			
			//초기화한 체스판에 복사 대상 state의 체스판 값을 복사
			for (int i = 0; i < state.map_size; i++) {
				this.chess_map[i] = state.chess_map[i];
			}
			
			this.map_size = state.map_size;
		}
		
		/*
		 * 검사 행의 오른쪽 열들의 Queen과 충돌하는 횟수 계산 메소드. 오른쪽 행들하고만 검사해 충돌 검사의 중복을 막는다.
		 * argument:
		 * 		int col: 검사할 행의 index
		 * 
		 * output:
		 * 		충돌 횟수
		 */
		private int GetCollisionRightCol(int col) {
			int col_size = 0;
			
			//검사 행의 바로 오른쪽 행부터 마지막 행까지 검사
			for (int i = col + 1; i < this.map_size; i++) {
				
				//같은 열에 있거나 대각선 상에 있으면 충돌 처리
				if (this.chess_map[i] == this.chess_map[col] || 
						Math.abs(this.chess_map[i] - this.chess_map[col]) == i - col) {
					col_size++;
				}
			}
			
			return col_size;
		}
		
		/*
		 * 휴리스틱 값의 계산 메소드. 휴리스틱 값은 Queen 간의 총 충돌 횟수로 정의.
		 * 계산 방식은 각 행에서 자신의 오른쪽 행들의 Queen과의 충돌 횟수를 전부 더함.
		 * argument:
		 * 
		 * output:
		 * 		휴리스틱 값 h(=Queen의 충돌 횟수)
		 */
		public int CalcHeuristic() {
			int h_val = 0;						//휴리스틱 값 저장 변수
			int temp;							//각 행에서의 충돌 횟수를 저장할 임시 변수
			
			
			//첫번 째 행부터 마지막 행 전까지 자신의 오른쪽 행과의 충돌 횟수 계산해 누산.
			for (int i = 0; i < this.map_size - 1; i++) {
				temp = this.GetCollisionRightCol(i);
				h_val += temp;
			}
			
			//휴리스틱 값 저장 변수에 저장
			this.h_value = h_val;
			
			return this.h_value;
		}
		
		/*
		 * 현재 state에서 진행 가능한 successor state를 생성.
		 * 하나의 행에서 위나 아래로 한칸 이동한 state를 successor state로 정의한다.
		 * argument:
		 * 		int col: 이동할 행.
		 * 		int move: 이동할 방향을 지정하는 변수. 0 = UP, 1 = DOWN
		 * output:
		 * 		successor state
		 */
		public State MakeSuccessor(int col, int move) {
			
			//새 state를 자신의 값으로 복사해 생성
			State state = new State(this);
			
			//위로 움직이는 상태일 경우
			if (move == hillClimbing.UP) {
				
				//이미 맨 위칸일 경우 이동이 불가능하므로 null state 반환.
				if (state.chess_map[col] > 0) {
					state.chess_map[col]--;
				} else {
					return null;
				}
			//아래로 움직이는 상태일 경우
			} else if (move == hillClimbing.DOWN) {
				//이미 맨 아래칸일 경우 이동이 불가능하므로 null state 반환.
				if (state.chess_map[col] < state.map_size - 1) {
					state.chess_map[col]++;
				} else {
					return null;
				}
			}
			
			return state;
		}
		
		/*
		 * local optima에 걸릴 경우 랜덤으로 다른 지점으로 이동하는 메소드.
		 * 랜덤으로 한 행을 선택해 2칸 이상의 길이를 이동.(1칸 이동은 이미 successor에서 계산하였으므로)
		 * argument:
		 * 
		 * output:
		 * 
		 */
		public void RandomJump() {
			
			//점프가 성공할 때까지 시도
			while (true) {
				
				//이동할 행과 그 행에서 이동할 열을 체스판 범위 내에서 랜덤으로 선택
				int col = MakeRandomInteger(0, this.map_size - 1);
				int row = MakeRandomInteger(0, this.map_size - 1);
				
				//2칸 이상의 이동이 아닐 경우 다시 시도
				if (Math.abs(this.chess_map[col] - row) > 1) {
					this.chess_map[col] = row;
					return;
				}
			}
		}
		
		/*
		 * 현재 state가 goal state일 경우 자신의 체스판 정보를 문자열로 반환. 
		 * 각 행의 어느 열에 Queen이 놓여져 있는지를 한칸씩 띄운 형태로 반환.
		 * argument:
		 * 
		 * output:
		 * 		현재 state의 체스판 정보.
		 */
		public String GetChessMap() {
			String string = new String();
			
			for (int i = 0; i < this.map_size; i++) {
				
				//마지막 행일 경우 띄어쓰기를 하지 않음
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
	 * 특정 범위에서의 랜덤 정수값 생성. 랜덤 점프를 하거나 이동할 successor state들 중 하나를 고를때 사용
	 * argument:
	 * 		int min: 최소 범위 값
	 * 		int max: 최대 범위 값
	 * output:
	 * 		랜덤으로 생성된 범위 내의 정수
	 */
	private int MakeRandomInteger(int min, int max) {
		return (int) ((Math.random() * (max - min + 1)) + min);
	}
	
	/*
	 * 처음 state를 생성. 각 행에서 랜덤으로 한 열에 Queen을 배치.
	 * argument:
	 * 
	 * output:
	 * 		랜덤으로 생성된 init state
	 */
	public State MakeInitState() {
		
		//state를 초기화 생성
		State initState = new State(this.map_size);
		
		//각 행마다 랜덤으로 열 값을 정해 배치
		for (int i = 0; i < initState.map_size; i++) {
			initState.chess_map[i] = MakeRandomInteger(0, this.map_size - 1);
		}
		
		return initState;
	}
	
	/*
	 * N-Queens 문제 해결 메소드. 랜덤으로 생성한 state에서부터 휴리스틱 값이 적은 방향으로 이동해 goal state에 도달.
	 * argument:
	 *
	 * output:
	 * 		goal state의 체스판 상태.
	 */
	public String SolveProblem() {
		
		//init state 생성
		State nowState = MakeInitState();
		
		int h_value;								//현재 state의 휴리스틱 값 저장 변수
		int h_successor;							//생성된 successor state들 중 가장 작은 휴리스틱 값 저장 변수
		int h_temp;									//생성된 successor state의 휴리스틱 값을 임시로 저장하는 변수
		
		/*
		 * successorList는 생성된 successor 중 현재 state보다 작은 휴리스틱 값을 가지는 state를 저장하는 리스트
		 * smallestList는 successorList의 요소 중 가장 작은 휴리스틱 값을 가지는 state를 저장하는 리스트
		 */
		ArrayList<State> successorList = new ArrayList<State>();
		ArrayList<State> smallestList = new ArrayList<State>();
		
		//goal state에 도달할때 까지 계산
		while (true) {
			
			//현재 state의 휴리스틱 값 계산 후 최소 휴리스틱 값에 저장
			h_value = nowState.CalcHeuristic();
			h_successor = h_value;
			
			//각 state 저장 리스트의 초기화
			successorList.clear();
			smallestList.clear();
			
			//현재 state가 goal state가 아닐 경우
			if (h_value > 0) {
				
				//각 행에서 위, 아래로 이동하는 모든 경우마다 successor state를 생성
				for (int i = 0; i < nowState.map_size; i++) {
					for (int j = 0; j < MOVE_SIZE; j++) {
						State successorState = nowState.MakeSuccessor(i, j);
						
						//successor state가 생성되었으면
						if (successorState != null) {												
							
							//successor state의 휴리스틱 값 계산
							h_temp = successorState.CalcHeuristic();
							
							//successor state의 휴리스틱 값이 현재 최소 휴리스틱 값보다 작으면 리스트에 추가
							if (h_temp < h_successor) {
								h_successor = h_temp;
								successorList.add(successorState);
							}
						}
					}
				}
				
				//successor 리스트에서 가장 작은 휴리스틱 값을 가지는 state만 선별
				for (int i = 0; i < successorList.size(); i++) {
					if (successorList.get(i).h_value == h_successor) {
						smallestList.add(successorList.get(i));
					}
				}
				
				//현재 state에서 더 작은 휴리스틱 값을 가지는 state로 이동하지 못할 경우 랜덤 점프
				if (successorList.isEmpty()) {
					nowState.RandomJump();
				} else {
					
					int random_idx;
					
					//가장 작은 휴리스틱 값을 가지는 state들 중 하나를 선택해 이동
					random_idx = MakeRandomInteger(0, smallestList.size() - 1);
						
					nowState = new State(smallestList.get(random_idx));
				}
			} else {
				//goal state에 도달시 정답 출력
				return nowState.GetChessMap();
			}
		}
	}
}

package assignment5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/*
 * date: 2017-06-11
 * author: 서치원(2011003155)
 * file: Genetic.java
 * comment: Genetic Algorithm을 구현한 클래스.
 */
public class Genetic {
	
	/*
	 * 하나의 Gene 객체. 체스판의 상황을 저장하는 클래스. 매 세대마다 일정 갯수의 Gene의 fitness를 계산해 진화시킴.
	 */
	private class Gene {
		public List<Integer> genes;				//체스판의 상황. 리스트의 idx가 열, idx의 value가 그 열의 어느 행에 놓여있는지를 나타냄.
												//즉, genes의 길이는 항상 체스판의 한 열의 길이와 같다
		public int collisionCountArr[];			//각 열의 Queen이 다른 열의 Queen과 충돌한 횟수를 저장. Mutation시 Mutate시킬 열의 우선순위를 정하는데 사용.
		public int fitness;						//이 객체의 점수. Queen 사이의 충돌 횟수를 저장. 즉, fitness는 낮을수록 좋고 0이면 정답임.
		private int size;						//체스판의 사이즈
		
		private boolean isCalced;				//fitness를 계산한 상태를 저장. 다음 세대에 그대로 넘어간 부모 객체의 경우 중복 계산을 막음.
		
		public List<Integer> collisionIdxList;	//충돌이 일어난 열들의 리스트. Mutation시 이 중에서 선택해 변경함.
		
		/*
		 * 최초 세대의 Gene 생성자. 모든 객체를 랜덤으로 생성함. 단, 체스판의 배치에서 다른 열에 같은 숫자가 나오지 않게 리스트를 셔플하는 방식으로 생성.
		 */
		public Gene(int size) {
			
			this.size = size;
			
			this.genes = new ArrayList<Integer>();
			
			//체스판 상태를 0, 1, 2, ..., N 으로 배치해 같은 행에 놓이는 경우가 없게 함
			for (int i = 0; i < size; ++i) {
				this.genes.add(i);
			}
			
			//배치된 체스판을 셔플해 랜덤으로 객체를 생성
			Collections.shuffle(this.genes);
			
			this.collisionCountArr = new int[size];
			
			this.collisionIdxList = new ArrayList<Integer>();
			
			this.fitness = Integer.MAX_VALUE;
			
			this.isCalced = false;
		}
		
		/*
		 * 부모로 부터 변형된 다음 세대 Gene의 생성자. 부모에서 변형된 genes(체스판 상태)를 복사해 생성.
		 */
		public Gene(int size, List<Integer> genes) {
			this.size = size;
			this.genes = new ArrayList<Integer>(genes);
			this.fitness = Integer.MAX_VALUE;
			this.collisionCountArr = new int[size];
			this.collisionIdxList = new ArrayList<Integer>();
			
			this.isCalced = false;
		}
		
		/*
		 * 자신의 fitness 계산. fitness는 Queen의 총 충돌 횟수로 계산하며 작을 수록 좋고 0 일 경우 충돌이 없으므로 정답
		 */
		public int calcFitness() {
			
			//이미 계산을 한 상태인 경우(전 세대의 부모 객체일 경우) 계산을 다시 하지 않고 그대로 반환
			if (this.isCalced) {
				return this.fitness;
			} else {
				
				//계산했음을 알리는 변수 저장
				this.isCalced = true;
				
				Integer genesArr[] = this.genes.toArray(new Integer[this.size]);
				
				//각 열의 충돌 횟수 초기화
				for (int i = 0; i < this.size; ++i) {
					collisionCountArr[i] = 0;
				}
				
				int count = 0;
				
				//각 열에서 다른 열과의 충돌 여부 판단
				for (int i = 0; i < this.size; ++i) {
					for (int j = i + 1; j < this.size; ++j) {
						if (genesArr[i] == genesArr[j] ||
								Math.abs(genesArr[j] - genesArr[i]) == j - i) {
							
							//충돌 시 각 열의 충돌 횟수와 총 충돌 횟수 증가
							++count;
							++collisionCountArr[i];
							++collisionCountArr[j];
						}
					}
				}
				
				this.fitness = count;
				
				return count;
			}
		}
		
		/*
		 * 자신의 충돌이 일어난 열의 리스트 반환 함수. Mutation에서 이용
		 */
		public List<Integer> getCrossoverPointList() {
			
			//이미 계산한 상태라면 바로 반환
			if (this.collisionIdxList.size() != 0) {
				return this.collisionIdxList;
			} else {
				for (int i = 0; i < this.size; ++i) {
					if (collisionCountArr[i] > 0) {
						this.collisionIdxList.add(i);
					}
				}
				return this.collisionIdxList;
			}
		}
		
		/*
		 * 자신이 정답 객체(fitness가 0인 경우)이면 정답 문자열을 반환
		 */
		public String getSolution() {
			String solution = "Location : ";
			
			for (int i = 0; i < this.size; ++i) {
				solution += this.genes.get(i) + " ";
			}
			
			return solution;
		}
	}
	
	private List<Gene> geneList;							//한 세대의 객체의 리스트
	
	private int queenSize;									//Queen의 갯수
	
	private Comparator<Gene> comparator;					//객체 리스트를 fitness에 따라 정렬하기 위한 비교 클래스 
	
	/*
	 * Genetic Algorithm의 각 전략 요소를 정하는 상수.
	 */
	private static int POPULATION_SIZE = 100;				//한 세대에서의 객체 수
	
	/*
	 * 각 부모의 변형 객체를 생성하는 비율. 세 상수가 합쳐서 1.0이 되어야 한다.
	 * 각 방식을 이용해 POPULATION_SIZE에 곱한 수만큼의 객체를 생성.
	 */
	private static double MUTATION_RATE = 0.9;				//다음 세대의 객체의 Mutation의 비율.
	private static double COPIED_RATE = 0.1;				//다음 세대에 그대로 넘어가는 부모 객체의 비율	
	private static double CROSSOVER_RATE = 0.0;				//다음 세대의 객체의 Crossover의 비율
	
	private static double CROSSOVER_POINT_RATE = 0.00;		//Crossover에서 몇 개의 열을 변경할지 정하는 비율. queenSize와 곱해 사용.
	private static double MUTATION_POINT_RATE = 0.00;		//Mutation에서 몇 개의 열을 변경할지 정하는 비율. queenSize와 곱해 사용.
	
	/*
	 * Genetic Algorithm 해결 클래스의 생성자
	 */
	public Genetic(int size) {
		this.queenSize = size;
		
		this.comparator = new Comparator<Gene>() {

			@Override
			public int compare(Gene g1, Gene g2) {
				if (g1.fitness < g2.fitness) {
					return -1;
				} else if (g1.fitness > g2.fitness) {
					return 1;
				} else {
					return 0;
				}
			}		
		};
	}
	
	/*
	 * 최초의 세대의 객체 생성 함수. 한 세대의 객체 수 만큼 랜덤으로 생성해 리스트에 더함.
	 */
	private void initialGeneration() {
		
		this.geneList = new ArrayList<Gene>();
		
		for (int i = 0; i < POPULATION_SIZE; ++i) {
			Gene gene = new Gene(this.queenSize);
			
			this.geneList.add(gene);
		}
	}
	
	@SuppressWarnings("static-access")
	/*
	 * 한 세대에서의 부모 객체를 정하는 함수. 각 객체의 fitness를 계산해 일정 상위 비율을 부모로 정함. 이때 정답 객체가 존재하면 그 객체만 반환해 정답이 존재함을 알림
	 */
	private List<Gene> getParentsList() {
		
		//리스트를 순환하면서 fitness를 계산. fitness가 0인 객체가 나올 경우 그 객체만 반환
		for (int i = 0; i < this.geneList.size(); ++i) {
			if(this.geneList.get(i).calcFitness() == 0) {
				List<Gene> oneList = new ArrayList<Gene>();
				oneList.add(this.geneList.get(i));
				
				return oneList;
			}
		}
		
		//객체의 리스트를 fitness를 기준으로 정렬
		Collections.sort(this.geneList, this.comparator);
		
		//상위 객체 이외의 객체를 리스트에서 삭제. sublist를 사용할 경우 메모리가 쌓이므로 한 리스트에서 삭제하는 것으로 선택.
		for (int i = 0; i < (int) (this.POPULATION_SIZE * (1.0 - this.COPIED_RATE)); ++i) {
			this.geneList.remove(this.geneList.size() - 1);
		}
		
		return this.geneList;
	}
	
	/*
	 * 부모에서 crossover를 통해 새 객체를 생성하는 함수. 부모의 열을 랜덤으로 선택해 교환.
	 * 성능이 별로 좋지 못해 현재는 crossover_rate를 0으로 해 사용하지 않음.
	 */
	private Gene crossover(Gene father, Gene mother) {
		
		//몇개의 열을 바꿀지를 계산. 상수를 통해 계산
		int crossoverPoint = (int) (this.queenSize * this.CROSSOVER_POINT_RATE);
		
		//만약 계산한 값이 0인 경우 적어도 1개를 바꾸도록 함
		if (crossoverPoint == 0) {
			crossoverPoint = 1;
		}
		
		//변경 기준은 어비 쪽의 gene으로 정함. 어비의 값을 변경하지 않고 genes을 변경하기 위해 복사.
		List<Integer> crossoverGenes = new ArrayList<Integer>(father.genes);
		
		/*
		 * 기존에는 충돌이 일어난 열끼리 교환했으나 그럴 경우 성능이 잘 나오지 않아 완전 랜덤 변경으로 정함.
		 */
		/*
		List<Integer> fatherPointList = father.getCrossoverPointList();
		List<Integer> motherPointList = mother.getCrossoverPointList();
		*/
		
		Random random = new Random();
		
		//변경할 열의 갯수만큼 열의 위치를 랜덤으로 정해 어비의 genes을 어미의 genes의 값으로 변경
		for (int i = 0; i < crossoverPoint; ++i) {
			
			/*
			int fatherIdx = fatherPointList.get(random.nextInt(fatherPointList.size()));
			int motherIdx = motherPointList.get(random.nextInt(motherPointList.size()));
			*/
			int fatherIdx = random.nextInt(father.size);
			int motherIdx = random.nextInt(mother.size);
			
			crossoverGenes.set(fatherIdx, mother.genes.get(motherIdx));
		}
		
		//변경된 genes을 이용해 자식 객체 생성 후 반환
		Gene child = new Gene(father.size, crossoverGenes);
		
		return child;
	}
	
	/*
	 * crossover를 통한 객체의 리스트를 반환하는 함수. 다음 세대의 객체로 보냄. 현재는 crossover의 기능이 좋지 않아 rate를 0으로 해 사용하지 않음.
	 */
	private List<Gene> getGenesToCrossover(List<Gene> parentsList) {
		List<Gene> crossoverList = new ArrayList<Gene>();
		Random random = new Random();
		
		//생성할 crossover의 비율을 통해 객체 수를 계산해 그 만큼 생성 후 리스트에 더함. 현재 rate는 0이므로 객체를 생성하지 않음.
		for (int i = 0; i < (int) (this.POPULATION_SIZE * this.CROSSOVER_RATE); ++i) {
			Gene father = parentsList.get(random.nextInt(parentsList.size()));
			Gene mother = parentsList.get(random.nextInt(parentsList.size()));
			
			crossoverList.add(crossover(father,mother));
		}
		
		return crossoverList;
	}
	
	@SuppressWarnings("static-access")
	/*
	 * mutation을 통해 새 객체를 생성하는 함수. 부모가 이미 다른 열에 같은 값을 가지지 않게 되어 있는 상태이므로 
	 * 자신의 열을 교환해 여전히 다른 열에 같은 값을 가지지 않게 하는 일종의 자가 crossover의 형식으로 작동.
	 * isRandom 인자가 true일 경우 교환 하는 열의 위치를 완전 랜덤으로
	 * 	false일 경우 한쪽의 열을 충돌이 일어난 열 중에 선택
	 */
	private Gene mutation(Gene original, boolean isRandom) {
		
		//변경할 열의 갯수를 상수를 통해 계산
		int mutationPoint = (int) (this.queenSize * this.MUTATION_POINT_RATE);
		
		//계산한 값이 0인 경우 적어도 하나는 변경하게 함
		if (mutationPoint == 0) {
			mutationPoint = 1;
		}
		
		Random random = new Random();
		
		//부모의 값을 변경하지 않고 genes을 변경하기 위해 복사.
		List<Integer> genes = new ArrayList<Integer>(original.genes);
		
		//충돌이 일어난 열만을 고르기 위한 리스트
		List<Integer> pointList = original.getCrossoverPointList();
		
		//완전 랜덤일 경우 두 열을 체스판 구역 내에서 랜덤으로 선택해 서로 바꿈
		if (isRandom) {
			for (int i = 0; i < mutationPoint; ++i) {
				int row1 = random.nextInt(original.size);
				int row2 = random.nextInt(original.size);
				
				int col1 = genes.get(row1);
				int col2 = genes.get(row2);
				
				genes.set(row1, col2);
				genes.set(row2, col1);
			}
			
	    //아닐 경우 한쪽의 열은 충돌이 일어난 열로 선택해 변경
		} else {
			for (int i = 0; i < mutationPoint; ++i) {
				int row1 = pointList.get(random.nextInt(pointList.size()));
				int row2 = random.nextInt(original.size);
				
				int col1 = genes.get(row1);
				int col2 = genes.get(row2);
				
				genes.set(row1, col2);
				genes.set(row2, col1);
			}
		}
		
		//변경된 genes을 이용해 자식 객체를 생성
		Gene child = new Gene(original.size, genes);
		
		return child;
	}
	
	/*
	 * Mutation을 통해 다음 세대의 객체들을 생성해 리스트로 반환하는 함수. 현재 모든 자식 객체를 mutation을 통해 생성.
	 */
	private List<Gene> getGenesToMutation(List<Gene> parentsList) {
		List<Gene> mutationList = new ArrayList<Gene>();
		Random random = new Random();
		
		int mutationSize = (int) (this.POPULATION_SIZE * this.MUTATION_RATE);
		
		//mutation의 비율을 통해 생성할 객체의 수를 계산. 그 중 절반은 완전 랜덤으로 절반은 충돌한 열에서 고르도록 함.
		for (int i = 0; i < mutationSize/2; ++i) {
			Gene original = parentsList.get(random.nextInt(parentsList.size()));
			
			mutationList.add(mutation(original, true));
			mutationList.add(mutation(original, false));
		}
		
		return mutationList;
	}
	
	/*
	 * main 함수에서 쓰이는 문제 해결 함수. 정답 문자열을 반환.
	 */
	public String solveProblem() {
		
		//최초 세대의 객체를 랜덤으로 생성
		initialGeneration();
		
		//현재 몇 세대인지를 저장하는 변수 초기화
		int generationCount = 0;
		
		//정답이 나오는 세대가 나올 때까지 진행
		while (true) {
			
			//다음 세대에 그대로 사용할 부모 객체 리스트 저장
			List<Gene> nextGenesList = getParentsList();
			
			generationCount++;
			
			//만약 받은 객체의 갯수가 1개일 경우 정답임을 간주하고 정답을 반환
			if (nextGenesList.size() == 1) {
				System.out.println("generation:" + generationCount + "   fitness:" + nextGenesList.get(0).fitness + "\n");
				return nextGenesList.get(0).getSolution();
			} else {
				
				//각 자식 객체 생성 방식을 사용해 자식을 받아 리스트에 저장. crossover는 성능상 사용하지 않음
				//List<Gene> crossoverList = getGenesToCrossover(nextGenesList);
				List<Gene> mutationList = getGenesToMutation(nextGenesList);
				
				//this.geneList.addAll(crossoverList);
				this.geneList.addAll(mutationList);
				
				System.out.println("generation:" + generationCount + "   fitness:" + this.geneList.get(0).fitness + "\n");
			}
		}
	}
}

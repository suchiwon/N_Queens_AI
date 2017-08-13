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
 * author: ��ġ��(2011003155)
 * file: Genetic.java
 * comment: Genetic Algorithm�� ������ Ŭ����.
 */
public class Genetic {
	
	/*
	 * �ϳ��� Gene ��ü. ü������ ��Ȳ�� �����ϴ� Ŭ����. �� ���븶�� ���� ������ Gene�� fitness�� ����� ��ȭ��Ŵ.
	 */
	private class Gene {
		public List<Integer> genes;				//ü������ ��Ȳ. ����Ʈ�� idx�� ��, idx�� value�� �� ���� ��� �࿡ �����ִ����� ��Ÿ��.
												//��, genes�� ���̴� �׻� ü������ �� ���� ���̿� ����
		public int collisionCountArr[];			//�� ���� Queen�� �ٸ� ���� Queen�� �浹�� Ƚ���� ����. Mutation�� Mutate��ų ���� �켱������ ���ϴµ� ���.
		public int fitness;						//�� ��ü�� ����. Queen ������ �浹 Ƚ���� ����. ��, fitness�� �������� ���� 0�̸� ������.
		private int size;						//ü������ ������
		
		private boolean isCalced;				//fitness�� ����� ���¸� ����. ���� ���뿡 �״�� �Ѿ �θ� ��ü�� ��� �ߺ� ����� ����.
		
		public List<Integer> collisionIdxList;	//�浹�� �Ͼ ������ ����Ʈ. Mutation�� �� �߿��� ������ ������.
		
		/*
		 * ���� ������ Gene ������. ��� ��ü�� �������� ������. ��, ü������ ��ġ���� �ٸ� ���� ���� ���ڰ� ������ �ʰ� ����Ʈ�� �����ϴ� ������� ����.
		 */
		public Gene(int size) {
			
			this.size = size;
			
			this.genes = new ArrayList<Integer>();
			
			//ü���� ���¸� 0, 1, 2, ..., N ���� ��ġ�� ���� �࿡ ���̴� ��찡 ���� ��
			for (int i = 0; i < size; ++i) {
				this.genes.add(i);
			}
			
			//��ġ�� ü������ ������ �������� ��ü�� ����
			Collections.shuffle(this.genes);
			
			this.collisionCountArr = new int[size];
			
			this.collisionIdxList = new ArrayList<Integer>();
			
			this.fitness = Integer.MAX_VALUE;
			
			this.isCalced = false;
		}
		
		/*
		 * �θ�� ���� ������ ���� ���� Gene�� ������. �θ𿡼� ������ genes(ü���� ����)�� ������ ����.
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
		 * �ڽ��� fitness ���. fitness�� Queen�� �� �浹 Ƚ���� ����ϸ� ���� ���� ���� 0 �� ��� �浹�� �����Ƿ� ����
		 */
		public int calcFitness() {
			
			//�̹� ����� �� ������ ���(�� ������ �θ� ��ü�� ���) ����� �ٽ� ���� �ʰ� �״�� ��ȯ
			if (this.isCalced) {
				return this.fitness;
			} else {
				
				//��������� �˸��� ���� ����
				this.isCalced = true;
				
				Integer genesArr[] = this.genes.toArray(new Integer[this.size]);
				
				//�� ���� �浹 Ƚ�� �ʱ�ȭ
				for (int i = 0; i < this.size; ++i) {
					collisionCountArr[i] = 0;
				}
				
				int count = 0;
				
				//�� ������ �ٸ� ������ �浹 ���� �Ǵ�
				for (int i = 0; i < this.size; ++i) {
					for (int j = i + 1; j < this.size; ++j) {
						if (genesArr[i] == genesArr[j] ||
								Math.abs(genesArr[j] - genesArr[i]) == j - i) {
							
							//�浹 �� �� ���� �浹 Ƚ���� �� �浹 Ƚ�� ����
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
		 * �ڽ��� �浹�� �Ͼ ���� ����Ʈ ��ȯ �Լ�. Mutation���� �̿�
		 */
		public List<Integer> getCrossoverPointList() {
			
			//�̹� ����� ���¶�� �ٷ� ��ȯ
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
		 * �ڽ��� ���� ��ü(fitness�� 0�� ���)�̸� ���� ���ڿ��� ��ȯ
		 */
		public String getSolution() {
			String solution = "Location : ";
			
			for (int i = 0; i < this.size; ++i) {
				solution += this.genes.get(i) + " ";
			}
			
			return solution;
		}
	}
	
	private List<Gene> geneList;							//�� ������ ��ü�� ����Ʈ
	
	private int queenSize;									//Queen�� ����
	
	private Comparator<Gene> comparator;					//��ü ����Ʈ�� fitness�� ���� �����ϱ� ���� �� Ŭ���� 
	
	/*
	 * Genetic Algorithm�� �� ���� ��Ҹ� ���ϴ� ���.
	 */
	private static int POPULATION_SIZE = 100;				//�� ���뿡���� ��ü ��
	
	/*
	 * �� �θ��� ���� ��ü�� �����ϴ� ����. �� ����� ���ļ� 1.0�� �Ǿ�� �Ѵ�.
	 * �� ����� �̿��� POPULATION_SIZE�� ���� ����ŭ�� ��ü�� ����.
	 */
	private static double MUTATION_RATE = 0.9;				//���� ������ ��ü�� Mutation�� ����.
	private static double COPIED_RATE = 0.1;				//���� ���뿡 �״�� �Ѿ�� �θ� ��ü�� ����	
	private static double CROSSOVER_RATE = 0.0;				//���� ������ ��ü�� Crossover�� ����
	
	private static double CROSSOVER_POINT_RATE = 0.00;		//Crossover���� �� ���� ���� �������� ���ϴ� ����. queenSize�� ���� ���.
	private static double MUTATION_POINT_RATE = 0.00;		//Mutation���� �� ���� ���� �������� ���ϴ� ����. queenSize�� ���� ���.
	
	/*
	 * Genetic Algorithm �ذ� Ŭ������ ������
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
	 * ������ ������ ��ü ���� �Լ�. �� ������ ��ü �� ��ŭ �������� ������ ����Ʈ�� ����.
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
	 * �� ���뿡���� �θ� ��ü�� ���ϴ� �Լ�. �� ��ü�� fitness�� ����� ���� ���� ������ �θ�� ����. �̶� ���� ��ü�� �����ϸ� �� ��ü�� ��ȯ�� ������ �������� �˸�
	 */
	private List<Gene> getParentsList() {
		
		//����Ʈ�� ��ȯ�ϸ鼭 fitness�� ���. fitness�� 0�� ��ü�� ���� ��� �� ��ü�� ��ȯ
		for (int i = 0; i < this.geneList.size(); ++i) {
			if(this.geneList.get(i).calcFitness() == 0) {
				List<Gene> oneList = new ArrayList<Gene>();
				oneList.add(this.geneList.get(i));
				
				return oneList;
			}
		}
		
		//��ü�� ����Ʈ�� fitness�� �������� ����
		Collections.sort(this.geneList, this.comparator);
		
		//���� ��ü �̿��� ��ü�� ����Ʈ���� ����. sublist�� ����� ��� �޸𸮰� ���̹Ƿ� �� ����Ʈ���� �����ϴ� ������ ����.
		for (int i = 0; i < (int) (this.POPULATION_SIZE * (1.0 - this.COPIED_RATE)); ++i) {
			this.geneList.remove(this.geneList.size() - 1);
		}
		
		return this.geneList;
	}
	
	/*
	 * �θ𿡼� crossover�� ���� �� ��ü�� �����ϴ� �Լ�. �θ��� ���� �������� ������ ��ȯ.
	 * ������ ���� ���� ���� ����� crossover_rate�� 0���� �� ������� ����.
	 */
	private Gene crossover(Gene father, Gene mother) {
		
		//��� ���� �ٲ����� ���. ����� ���� ���
		int crossoverPoint = (int) (this.queenSize * this.CROSSOVER_POINT_RATE);
		
		//���� ����� ���� 0�� ��� ��� 1���� �ٲٵ��� ��
		if (crossoverPoint == 0) {
			crossoverPoint = 1;
		}
		
		//���� ������ ��� ���� gene���� ����. ����� ���� �������� �ʰ� genes�� �����ϱ� ���� ����.
		List<Integer> crossoverGenes = new ArrayList<Integer>(father.genes);
		
		/*
		 * �������� �浹�� �Ͼ ������ ��ȯ������ �׷� ��� ������ �� ������ �ʾ� ���� ���� �������� ����.
		 */
		/*
		List<Integer> fatherPointList = father.getCrossoverPointList();
		List<Integer> motherPointList = mother.getCrossoverPointList();
		*/
		
		Random random = new Random();
		
		//������ ���� ������ŭ ���� ��ġ�� �������� ���� ����� genes�� ����� genes�� ������ ����
		for (int i = 0; i < crossoverPoint; ++i) {
			
			/*
			int fatherIdx = fatherPointList.get(random.nextInt(fatherPointList.size()));
			int motherIdx = motherPointList.get(random.nextInt(motherPointList.size()));
			*/
			int fatherIdx = random.nextInt(father.size);
			int motherIdx = random.nextInt(mother.size);
			
			crossoverGenes.set(fatherIdx, mother.genes.get(motherIdx));
		}
		
		//����� genes�� �̿��� �ڽ� ��ü ���� �� ��ȯ
		Gene child = new Gene(father.size, crossoverGenes);
		
		return child;
	}
	
	/*
	 * crossover�� ���� ��ü�� ����Ʈ�� ��ȯ�ϴ� �Լ�. ���� ������ ��ü�� ����. ����� crossover�� ����� ���� �ʾ� rate�� 0���� �� ������� ����.
	 */
	private List<Gene> getGenesToCrossover(List<Gene> parentsList) {
		List<Gene> crossoverList = new ArrayList<Gene>();
		Random random = new Random();
		
		//������ crossover�� ������ ���� ��ü ���� ����� �� ��ŭ ���� �� ����Ʈ�� ����. ���� rate�� 0�̹Ƿ� ��ü�� �������� ����.
		for (int i = 0; i < (int) (this.POPULATION_SIZE * this.CROSSOVER_RATE); ++i) {
			Gene father = parentsList.get(random.nextInt(parentsList.size()));
			Gene mother = parentsList.get(random.nextInt(parentsList.size()));
			
			crossoverList.add(crossover(father,mother));
		}
		
		return crossoverList;
	}
	
	@SuppressWarnings("static-access")
	/*
	 * mutation�� ���� �� ��ü�� �����ϴ� �Լ�. �θ� �̹� �ٸ� ���� ���� ���� ������ �ʰ� �Ǿ� �ִ� �����̹Ƿ� 
	 * �ڽ��� ���� ��ȯ�� ������ �ٸ� ���� ���� ���� ������ �ʰ� �ϴ� ������ �ڰ� crossover�� �������� �۵�.
	 * isRandom ���ڰ� true�� ��� ��ȯ �ϴ� ���� ��ġ�� ���� ��������
	 * 	false�� ��� ������ ���� �浹�� �Ͼ �� �߿� ����
	 */
	private Gene mutation(Gene original, boolean isRandom) {
		
		//������ ���� ������ ����� ���� ���
		int mutationPoint = (int) (this.queenSize * this.MUTATION_POINT_RATE);
		
		//����� ���� 0�� ��� ��� �ϳ��� �����ϰ� ��
		if (mutationPoint == 0) {
			mutationPoint = 1;
		}
		
		Random random = new Random();
		
		//�θ��� ���� �������� �ʰ� genes�� �����ϱ� ���� ����.
		List<Integer> genes = new ArrayList<Integer>(original.genes);
		
		//�浹�� �Ͼ ������ ���� ���� ����Ʈ
		List<Integer> pointList = original.getCrossoverPointList();
		
		//���� ������ ��� �� ���� ü���� ���� ������ �������� ������ ���� �ٲ�
		if (isRandom) {
			for (int i = 0; i < mutationPoint; ++i) {
				int row1 = random.nextInt(original.size);
				int row2 = random.nextInt(original.size);
				
				int col1 = genes.get(row1);
				int col2 = genes.get(row2);
				
				genes.set(row1, col2);
				genes.set(row2, col1);
			}
			
	    //�ƴ� ��� ������ ���� �浹�� �Ͼ ���� ������ ����
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
		
		//����� genes�� �̿��� �ڽ� ��ü�� ����
		Gene child = new Gene(original.size, genes);
		
		return child;
	}
	
	/*
	 * Mutation�� ���� ���� ������ ��ü���� ������ ����Ʈ�� ��ȯ�ϴ� �Լ�. ���� ��� �ڽ� ��ü�� mutation�� ���� ����.
	 */
	private List<Gene> getGenesToMutation(List<Gene> parentsList) {
		List<Gene> mutationList = new ArrayList<Gene>();
		Random random = new Random();
		
		int mutationSize = (int) (this.POPULATION_SIZE * this.MUTATION_RATE);
		
		//mutation�� ������ ���� ������ ��ü�� ���� ���. �� �� ������ ���� �������� ������ �浹�� ������ ������ ��.
		for (int i = 0; i < mutationSize/2; ++i) {
			Gene original = parentsList.get(random.nextInt(parentsList.size()));
			
			mutationList.add(mutation(original, true));
			mutationList.add(mutation(original, false));
		}
		
		return mutationList;
	}
	
	/*
	 * main �Լ����� ���̴� ���� �ذ� �Լ�. ���� ���ڿ��� ��ȯ.
	 */
	public String solveProblem() {
		
		//���� ������ ��ü�� �������� ����
		initialGeneration();
		
		//���� �� ���������� �����ϴ� ���� �ʱ�ȭ
		int generationCount = 0;
		
		//������ ������ ���밡 ���� ������ ����
		while (true) {
			
			//���� ���뿡 �״�� ����� �θ� ��ü ����Ʈ ����
			List<Gene> nextGenesList = getParentsList();
			
			generationCount++;
			
			//���� ���� ��ü�� ������ 1���� ��� �������� �����ϰ� ������ ��ȯ
			if (nextGenesList.size() == 1) {
				System.out.println("generation:" + generationCount + "   fitness:" + nextGenesList.get(0).fitness + "\n");
				return nextGenesList.get(0).getSolution();
			} else {
				
				//�� �ڽ� ��ü ���� ����� ����� �ڽ��� �޾� ����Ʈ�� ����. crossover�� ���ɻ� ������� ����
				//List<Gene> crossoverList = getGenesToCrossover(nextGenesList);
				List<Gene> mutationList = getGenesToMutation(nextGenesList);
				
				//this.geneList.addAll(crossoverList);
				this.geneList.addAll(mutationList);
				
				System.out.println("generation:" + generationCount + "   fitness:" + this.geneList.get(0).fitness + "\n");
			}
		}
	}
}

package assignment2;

import java.io.FileWriter;
import java.io.IOException;
/*
 * date: 2017-04-09
 * author: ��ġ��(2011003155)
 * file: main.java
 * comment: N-queens ������ ó���ϴ� ����� hillClimbing ����� �����ϴ� ���� �ڵ� �κ�
 * 	���� �� arguments �� queen�� ����(=ü������ ũ��), ��� ������ ������ ���� ��θ� �Է� �޴´�.
 */
public class main {

	public static void main(String[] args) {
		
		//�Է� ���� arguments�� ���̰� 2�� �ƴϸ� �ٷ� ����
		if (args.length != 2) {
			System.out.println("please input [queen num] [filePath]");
			return;
		}
				
		int queen_num;						//���� �������� ���� queen�� ����
		String filePath = args[1] + "\\";	//��� ������ ������ ���� ��� ���ڿ�
		FileWriter fw;						//��� ������ ���� ������ �Է��� FileWriter Ŭ����
		try {
					
			/*
			 * �Է¹��� queen ���ڸ� ������ ���� �� ��� ������ ���� �Ǵ� ��.
			 * ó�� ���� ���� �߻���(ù�� ° argument�� ���ڰ� �ƴ϶����) ���� ó�� �� ����
			 */
			queen_num = Integer.parseInt(args[0]);
			fw = new FileWriter(filePath + "Result" + queen_num + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
					
			System.out.println("please input proper value...");
			return;
		}

		try {
					
			//������ �˰����� ��� �ð��� ������ ����. ���� �ð��� ���� �ð��� ����� ���̸� ���� ���
			long startTime = System.currentTimeMillis();
			
			hillClimbing problem = new hillClimbing(queen_num);
					
			//DFS �˰����� ����� ��� ���Ͽ� �Է�
			fw.write("Hill Climbing>\r\n");
			fw.write(problem.SolveProblem() + "\r\n");
					
			//�˰����� ���� �ð��� �����ϴ� ����.
			long endTime = System.currentTimeMillis();
					
		//���� �ð��� ���� �ð��� ���� �˰����� �ð� ����� ��� ���Ͽ� �Է�
		fw.write("Total Elipsed Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
						
		//��� ����� �Է� �� ��� ���� �ݱ�
		fw.close();
					
		//��� ���� �Է� �Ϸ� �޼����� ���
		System.out.println("make answer file success!!");
					
		} catch (IOException e) {	
			//��� ������ �Է� ���� ���� �߻��� ó��
			e.printStackTrace();
		}
	}
}

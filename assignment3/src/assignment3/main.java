package assignment3;

import java.io.FileWriter;
import java.io.IOException;

import assignment3.algorithm;
import assignment3.CSP;
/*
 * date: 2017-03-25
 * author: ��ġ��(2011003155)
 * file: main.java
 * comment: N-queens ������ ó���ϴ� ��� �� CSP�� backtracking, forward checking, arc consistance 3������ ����� ������� �����ϴ� ���� �ڵ� �κ�
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
						
			//�Ϲ� CSP �˰��� Ŭ���� ����
			algorithm problem = new CSP(queen_num);
						
			//CSP �˰����� ����� ��� ���Ͽ� �Է�
			fw.write("Standard CSP>\r\n");
			//fw.write(problem.solveProblem() + "\r\n");
						
			//�˰����� ���� �ð��� �����ϴ� ����.
			long endTime = System.currentTimeMillis();
						
			//���� �ð��� ���� �ð��� ���� �˰����� �ð� ����� ��� ���Ͽ� �Է�
			fw.write("Total Elapsed Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
			//forward checking �˰����� ���� CSP �˰����� ���� ����� ���� ó��
			startTime = System.currentTimeMillis();
						
			problem = new fowardChecking(queen_num);
						
			fw.write("CSP with Forward Checking>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
						
			endTime = System.currentTimeMillis();
						
			fw.write("Total Elapsed Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
						
			//arc consistance �˰����� ���� CSP �˰����� ���� ����� ���� ó��
			startTime = System.currentTimeMillis();
						
			problem = new arcConsistance(queen_num);
						
			fw.write("CSP with Arc Consistency>\r\n");
			//fw.write(problem.solveProblem() + "\r\n");
						
			endTime = System.currentTimeMillis();
					
			fw.write("Total Elapsed Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
						
			//��� ����� �Է� �� ��� ���� �ݱ�
			fw.close();
						
			//��� ���� �Է� �Ϸ� �޼����� ���
			System.out.println("make answer file success!!");
						
		} catch (IOException e) {
						
			//��� ������ �Է� ���� ���� �߻��� ó��
			e.printStackTrace();
		}		// TODO Auto-generated method stub
	}

}

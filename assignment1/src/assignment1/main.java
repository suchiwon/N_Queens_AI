package assignment1;

/*
 * date: 2017-03-25
 * author: ��ġ��(2011003155)
 * file: main.java
 * comment: N-queens ������ ó���ϴ� ����� DFS, BFS, DFID �� ���� ����� ������� �����ϴ� ���� �ڵ� �κ�
 * 	���� �� arguments �� queen�� ����(=ü������ ũ��), ��� ������ ������ ���� ��θ� �Է� �޴´�.
 */

import java.io.FileWriter;
import java.io.IOException;

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
			
			//DFS �˰��� Ŭ���� ����
			algorithm problem = new DFS(queen_num);
			
			//DFS �˰����� ����� ��� ���Ͽ� �Է�
			fw.write("DFS>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
			
			//�˰����� ���� �ð��� �����ϴ� ����.
			long endTime = System.currentTimeMillis();
			
			//���� �ð��� ���� �ð��� ���� �˰����� �ð� ����� ��� ���Ͽ� �Է�
			fw.write("Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
			//BFS �˰����� ���� DFS �˰����� ���� ����� ���� ó��
			startTime = System.currentTimeMillis();
			
			problem = new BFS(queen_num);
			
			fw.write("BFS>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
			
			endTime = System.currentTimeMillis();
			
			fw.write("Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
			//DFID �˰����� ���� DFS �˰����� ���� ����� ���� ó��
			startTime = System.currentTimeMillis();
			
			problem = new DFID(queen_num);
			
			fw.write("DFID>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
			
			endTime = System.currentTimeMillis();
			
			fw.write("Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
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

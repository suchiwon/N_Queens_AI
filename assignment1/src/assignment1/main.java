package assignment1;

/*
 * date: 2017-03-25
 * author: 서치원(2011003155)
 * file: main.java
 * comment: N-queens 문제를 처리하는 방식인 DFS, BFS, DFID 세 개의 방식을 순서대로 실행하는 메인 코드 부분
 * 	실행 시 arguments 로 queen의 숫자(=체스판의 크기), 결과 파일을 저장할 절대 경로를 입력 받는다.
 */

import java.io.FileWriter;
import java.io.IOException;

public class main {

	public static void main(String[] args) {
		
		//입력 받은 arguments의 길이가 2가 아니면 바로 종료
		if (args.length != 2) {
			System.out.println("please input [queen num] [filePath]");
			return;
		}
		
		int queen_num;						//서로 공격하지 않을 queen의 갯수
		String filePath = args[1] + "\\";	//결과 파일을 저장할 절대 경로 문자열
		FileWriter fw;						//결과 파일을 열어 내용을 입력할 FileWriter 클래스
		try {
			
			/*
			 * 입력받은 queen 숫자를 변수에 대입 후 결과 파일을 생성 또는 염.
			 * 처리 도중 에러 발생시(첫번 째 argument가 숫자가 아니라던가) 예외 처리 후 종료
			 */
			queen_num = Integer.parseInt(args[0]);
			fw = new FileWriter(filePath + "Result" + queen_num + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
			
			System.out.println("please input proper value...");
			return;
		}

		try {
			
			//각각의 알고리즘의 경과 시간을 저장할 변수. 종료 시간과 시작 시간을 계산해 차이를 통해 계산
			long startTime = System.currentTimeMillis();
			
			//DFS 알고리즘 클래스 생성
			algorithm problem = new DFS(queen_num);
			
			//DFS 알고리즘의 결과를 결과 파일에 입력
			fw.write("DFS>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
			
			//알고리즘의 종료 시간을 저장하는 변수.
			long endTime = System.currentTimeMillis();
			
			//종료 시간에 시작 시간을 빼서 알고리즘의 시간 계산해 결과 파일에 입력
			fw.write("Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
			//BFS 알고리즘을 위의 DFS 알고리즘의 실행 방법과 같이 처리
			startTime = System.currentTimeMillis();
			
			problem = new BFS(queen_num);
			
			fw.write("BFS>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
			
			endTime = System.currentTimeMillis();
			
			fw.write("Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
			//DFID 알고리즘을 위의 DFS 알고리즘의 실행 밥법과 같이 처리
			startTime = System.currentTimeMillis();
			
			problem = new DFID(queen_num);
			
			fw.write("DFID>\r\n");
			fw.write(problem.solveProblem() + "\r\n");
			
			endTime = System.currentTimeMillis();
			
			fw.write("Time : " + (endTime - startTime)/1000.0f + "\r\n\r\n");
			
			//모든 결과의 입력 후 결과 파일 닫기
			fw.close();
			
			//결과 파일 입력 완료 메세지를 출력
			System.out.println("make answer file success!!");
			
		} catch (IOException e) {
			
			//결과 파일의 입력 도중 예외 발생시 처리
			e.printStackTrace();
		}
	}
}

package paasta.demo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import paasta.demo.dto.mongo.friendListDTO;

public class testClass {

	public static void main(String[] args) {
		//List<friendListDTO> rList = new ArrayList<friendListDTO>();
		//String[] strArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
		String data = "Optional[JRIkFyYVIRMqBjcPPQw8DT0KPREkHSoTKhhJ,"
				+ "JRIlESIVLBUsADICNg47DDsIOBQhGC8WLx1X,"
				+ "JRQjEyQTKhMkET0OPQk4CTAFPQ8jFi8YIRgqZQ,"
				+ "JRYhECMbIhcgDDsDMwMxBDYCLhsiFSwVJ2E,"
				+ "JRYhFCMbKBotATMFPQU1DDoKPxMmHygRKBpU]";
		String [] rep = data.replaceAll("\\[", "")
				.replaceAll("\\]", "")
				.substring(8)
				.split(",");
		for(String e : rep) {
			System.out.println("data : " + e);
		}
	}
	// => 자리 랜덤 배정
	public void random() {
		int num = 3; // => 학생수
		String[] arrName = {};
		String[] seat = {};
		String[] temp = new String[num];

		List<String> list = Arrays.asList(arrName);
		Collections.shuffle(list);
		list.toArray(arrName);
		List<String> listTwo = Arrays.asList(seat);
		Collections.shuffle(listTwo);
		listTwo.toArray(seat);

		for (int i = 0; i < temp.length; i++) {
			temp[i] = list.get(i) + " -> " + listTwo.get(i);
		}

		System.out.println(Arrays.toString(temp));
	}

}

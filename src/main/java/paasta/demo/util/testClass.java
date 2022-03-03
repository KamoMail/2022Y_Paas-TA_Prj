package paasta.demo.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class testClass {

	public static void main(String[] args) {
		String [] arrName = {"김지현", "이정훈", "최윤성"};
		String [] seat = {"2", "4", "6", "14", "17", "25", "29", "33", "35"};
		String [] temp = new String[3];
		
		List<String> list = Arrays.asList(arrName);
		Collections.shuffle(list);
		list.toArray(arrName);
		List<String> listTwo = Arrays.asList(seat);
		Collections.shuffle(listTwo);
		listTwo.toArray(seat);
		
		for(int i = 0; i < temp.length; i++) {
			temp[i] = list.get(i) + " -> " + listTwo.get(i);
		}
		
		System.out.println(Arrays.toString(temp));
	}

}

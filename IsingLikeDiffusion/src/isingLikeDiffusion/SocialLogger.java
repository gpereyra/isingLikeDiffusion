package isingLikeDiffusion;

import java.util.Date;


public class SocialLogger {
	public static Long debug(Object o, String msg){
		return debug(o,msg,null);
	}

	public static long debug(Object o, String msg, Long initialTime) {
		Date d = new Date();
		String tolog = d + " " + o.getClass() + "["+o.hashCode()+"]";
		if (initialTime != null){
			tolog = tolog + " - ( "+ (initialTime-d.getTime()) +"ms )"; 
		}
		System.out.println(tolog + msg);
		return d.getTime();
	}
}

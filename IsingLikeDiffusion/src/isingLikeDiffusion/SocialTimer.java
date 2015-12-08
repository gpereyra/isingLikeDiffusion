package isingLikeDiffusion;

public class SocialTimer {
	private static long maxTime;
	private static long invocations;
	public static void time(Long time) {
		if (time != null && time > maxTime){
			maxTime = time;
		}
		invocations++;
	}
	public static void print() {
		SocialLogger.debug(SocialTimer.class, "max time:" + maxTime +"ms ; invocations:"+invocations);
	}
}

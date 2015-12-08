package isingLikeDiffusion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class Register {
	private static Register register;
	private Map<ModelAnalytic, CircularFifoBuffer> analytics;

	private Register() {
		initAnalytics();
	}
	
	public static Register getInstance(){
		if (register == null){
			register = new Register();
		}
		return register;
	}
	
	public void takeReading(ModelAnalytic a, BigDecimal reading){
		analytics.get(a).add(reading);
	}
	
	public BigDecimal readAverage(ModelAnalytic a) {
		BigDecimal sum = BigDecimal.ZERO;
		int i = 0;
		for (Object reading : analytics.get(a)) {
			BigDecimal r = (BigDecimal) reading;
			sum = sum.add(r);
			i++;
		}
		return sum.divide(BigDecimal.valueOf(i), 2, RoundingMode.HALF_UP);
	}
	
	public Boolean getIsStable(ModelAnalytic a, BigDecimal currentReading){
		if (this.analytics.get(a).size() < 3){
			return Boolean.FALSE;
		} else {
			return readAverage(a).subtract(currentReading).abs().compareTo(BigDecimal.valueOf(0.01)) < 0;
		}
	}
	
	private void initAnalytics() {
		analytics = new HashMap<ModelAnalytic, CircularFifoBuffer>();
		analytics.put(ModelAnalytic.ADOPTION_COUNT, new CircularFifoBuffer(3));
	}

	public void clear() {
		this.initAnalytics();
	}
	
}
